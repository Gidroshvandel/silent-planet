package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.EngineEcs
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.Turn
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.TurnToMove
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs
import com.silentgames.silent_planet.logic.ecs.extractTransports

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : System {

    private var firstInit = true

    private var currentFractionsType: FractionsType? = null
    private var turn: Turn? = null

    override fun onEngineAttach(engine: EngineEcs) {
        engine.onProcessingChanged = { processing ->
            if (!processing && !engine.gameState.hasCanTurnUnits()) {
                turn?.turnCount()
                engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
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

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (gameState.moveSuccess) {
            gameState.unitMap.extractTransports().forEach {
                it.removeComponent(TurnToMove::class.java)
            }
        }
        unit.getComponent<FractionsType>()?.let { nextTurn(it, gameState) }
    }

    private fun GameState.hasCanTurnUnits() =
            unitMap.extractTransports().find { it.hasComponent<TurnToMove>() } != null

    private fun nextTurn(fractionsType: FractionsType, gameState: GameState) {
        currentFractionsType = fractionsType
        turn = gameState.turn
    }

}