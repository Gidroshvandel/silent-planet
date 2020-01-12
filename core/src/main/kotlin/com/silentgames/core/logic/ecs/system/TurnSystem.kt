package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Active
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.MovedSuccess
import com.silentgames.core.logic.ecs.component.TurnToMove
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.extractTransports

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : System {

    private var firstInit = true

    override fun onEngineAttach(engine: EngineEcs) {
        engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
        engine.onProcessingChanged = { processing ->
            if (!processing && engine.gameState.isTurnEnd()) {
                engine.gameState.endTurn()
                engine.gameState.turn.turnCount()
                engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
                engine.gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
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
            onTurnChanged.invoke(gameState.turn.currentTurnFraction)
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<MovedSuccess>()) {
            unit.removeComponent(TurnToMove::class.java)
        }
    }

    private fun GameState.isTurnEnd() =
            getAllFractionUnits(turn.currentTurnFraction).find {
                it.hasComponent<Active>() && !it.hasComponent<TurnToMove>()
            } != null

}