package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TurnSystem(private val onTurnChanged: (FractionsType) -> Unit) : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "TurnSystem"
    }

    override fun onEngineAttach(engine: EngineEcs) {
        if (engine.gameState.isFirstGameStarting()) {
            engine.gameState.makeCurrentFractionTurnUnitsCanTurn()
        }
        engine.gameState.turn.currentFraction.let { onTurnChanged.invoke(it) }
    }

    private fun GameState.isFirstGameStarting() = this.turn.count == 0 && this.isTurnEnd()

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<Moving>()) {
            gameState.finishTurn(unit)
        }
    }

    override fun execute(gameState: GameState) {
        super.execute(gameState)
        if (gameState.isTurnEnd() && gameState.isMovingFinish()) {
            gameState.turn.nextTurn()
            gameState.makeCurrentFractionTurnUnitsCanTurn()
            gameState.turn.currentFraction.let { onTurnChanged.invoke(it) }
            CoreLogger.logDebug(SYSTEM_TAG, gameState.turn.currentFraction.name)
        }
    }

    private fun GameState.makeCurrentFractionTurnUnitsCanTurn() {
        makeUnitsCanTurn(turn.currentFraction)
    }

    private fun GameState.makeUnitsCanTurn(fractionsType: FractionsType) {
        unitMap.filter {
            it.getComponent<FractionsType>() == fractionsType
                    && it.hasComponent<Active>()
                    && it.hasComponent<TurnMode>()
        }.forEach {
            it.addComponent(CanTurn())
        }
    }

}

fun GameState.finishTurn(unit: UnitEcs) {
    val turnMode = unit.getComponent<TurnMode>()
    if (turnMode != null) {
        unitMap.filter {
            it.getComponent<TurnMode>()?.groupType == turnMode.groupType
        }.forEach {
            it.removeComponent(CanTurn::class.java)
        }
    }
}

fun GameState.finishTurn() {
    unitMap.forEach { unit ->
        unit.removeComponent(CanTurn::class.java)
    }
}