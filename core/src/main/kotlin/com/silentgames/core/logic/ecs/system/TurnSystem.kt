package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Turn
import com.silentgames.core.logic.ecs.component.Active
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.MovedSuccess
import com.silentgames.core.logic.ecs.component.TurnToMove
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.extractTransports

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : System {

    private var firstInit = true

    private var currentFractionsType: FractionsType? = null
    private var turn: Turn? = null

    override fun onEngineAttach(engine: EngineEcs) {
        engine.onProcessingChanged = { processing ->
            if (!processing && engine.gameState.isTurnEnd()) {
                engine.gameState.endTurn()
                turn?.turnCount()
                engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
                turn?.currentTurnFraction?.let { onTurnChanged.invoke(it) }
            }
        }
    }

    private fun GameState.endTurn() {
        unitMap.extractTransports().forEach {
            it.removeComponent(TurnToMove::class.java)
        }
    }

    override fun execute(gameState: GameState) {
        if (firstInit) {
            firstInit = false
            gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<MovedSuccess>()) {
            unit.removeComponent(TurnToMove::class.java)
        }
        unit.getComponent<FractionsType>()?.let { nextTurn(it, gameState) }
    }

    private fun GameState.isTurnEnd() =
            getAllFractionUnits(turn.currentTurnFraction).find {
                it.hasComponent<Active>() && !it.hasComponent<TurnToMove>()
            } != null

    private fun nextTurn(fractionsType: FractionsType, gameState: GameState) {
        currentFractionsType = fractionsType
        turn = gameState.turn
    }

}