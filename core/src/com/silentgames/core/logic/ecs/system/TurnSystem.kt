package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : UnitSystem() {

    override fun onEngineAttach(engine: EngineEcs) {
        engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
        engine.gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
    }

    private fun GameState.endTurn() {
        unitMap.forEach {
            it.removeComponent(TurnToMove::class.java)
            it.removeComponent(TurnEnd::class.java)
        }
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)
        if (!gameState.turn.canTurn && gameState.unitMap.find { it.hasComponent<Moving>() } == null) {
            gameState.endTurn()
            gameState.turn.turnCount()
            gameState.makeCurrentFractionTurnUnitsCanTurn()
            gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<MovedSuccess>()) {
            unit.addComponent(TurnEnd())
            unit.removeComponent(TurnToMove::class.java)
        }
        if (gameState.isTurnEnd()) {
            gameState.turn.endTurn()
        }
    }

    private fun GameState.isTurnEnd() =
            getAllFractionUnits(turn.currentTurnFraction).find {
                it.hasComponent<TurnEnd>()
            } != null

}