package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CanTurn
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.MovedSuccess
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "TurnSystem"
    }

    override fun onEngineAttach(engine: EngineEcs) {
        engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
        engine.gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<MovedSuccess>()) {
            unit.removeComponent(CanTurn::class.java)
        }
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)
        if (gameState.isTurnEnd() && gameState.unitMap.find { it.hasComponent<MovedSuccess>() } == null) {
            gameState.endTurn()
            gameState.turn.nextTurn()
            gameState.makeCurrentFractionTurnUnitsCanTurn()
            gameState.turn.currentTurnFraction.let { onTurnChanged.invoke(it) }
            CoreLogger.logDebug(SYSTEM_TAG, gameState.turn.currentTurnFraction.name)
        }
    }

    private fun GameState.endTurn() {
        unitMap.forEach {
            it.removeComponent(CanTurn::class.java)
        }
    }

}