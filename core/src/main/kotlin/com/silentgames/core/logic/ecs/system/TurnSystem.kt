package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.MovedSuccess
import com.silentgames.core.logic.ecs.component.TurnEnd
import com.silentgames.core.logic.ecs.component.TurnToMove
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.extractTransports

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : System {

    private var firstInit = true

    override fun onEngineAttach(engine: EngineEcs) {
        engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
        engine.onProcessingChanged = { processing ->
            if (firstInit && !processing) {
                firstInit = false
                onTurnChanged.invoke(engine.gameState.turn.currentTurnFraction)
            } else if (!processing && engine.gameState.isTurnEnd()) {
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
            it.removeComponent(TurnEnd::class.java)
        }
    }

    override fun execute(gameState: GameState) {

    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<MovedSuccess>()) {
            unit.addComponent(TurnEnd())
            unit.removeComponent(TurnToMove::class.java)
        }
    }

    private fun GameState.isTurnEnd() =
            getAllFractionUnits(turn.currentTurnFraction).find {
                it.hasComponent<TurnEnd>()
            } != null

}