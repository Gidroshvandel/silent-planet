package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.Engine
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.Turn
import com.silentgames.silent_planet.logic.ecs.component.TurnToMove
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.ecs.extractTransports
import com.silentgames.silent_planet.model.fractions.FractionsType

class TurnSystem(private val onTurnChanged: (FractionsType) -> kotlin.Unit) : System {

    private var firstInit = true

    private var currentFractionsType: FractionsType? = null
    private var turn: Turn? = null

    override fun onEngineAttach(engine: Engine) {
        engine.onProcessingChanged = { processing ->
            if (!processing && !engine.gameState.hasActiveUnits()) {
                turn?.turnCount()
                engine.gameState.makeCurrentFractionTurnUnitsActive()
                turn?.currentTurnFraction?.let { onTurnChanged.invoke(it) }
            }
        }
    }

    override fun execute(gameState: GameState) {
        if (firstInit) {
            firstInit = false
            gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
        }
    }

    override fun execute(gameState: GameState, unit: Unit) {
        if (gameState.moveSuccess) {
            gameState.unitMap.extractTransports().forEach {
                it.removeComponent(TurnToMove::class.java)
            }
        }
        unit.getComponent<FractionsType>()?.let { nextTurn(it, gameState) }
    }

    private fun GameState.hasActiveUnits() =
            unitMap.extractTransports().find { it.hasComponent<TurnToMove>() } != null

    private fun nextTurn(fractionsType: FractionsType, gameState: GameState) {
        currentFractionsType = fractionsType
        turn = gameState.turn
    }

}