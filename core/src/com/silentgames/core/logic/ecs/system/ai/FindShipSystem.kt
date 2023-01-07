package com.silentgames.core.logic.ecs.system.ai

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.ArtificialIntelligence
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Goal
import com.silentgames.core.logic.ecs.component.MovingMode
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentUnitCell
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.logic.ecs.system.isCrystalBagFull
import com.silentgames.core.logic.ecs.system.unit.UnitSystem

class FindShipSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "FindShipSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            if (unit.hasComponent<ArtificialIntelligence>() && unit.isCrystalBagFull() && unit.isPlayer()) {
                gameState.getSpaceShipPosition(unit)?.let {
                    CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} added goal $it")
                    unit.addComponent(Goal(it))
                }
            }
        }
    }

    private fun GameState.getSpaceShipPosition(unit: UnitEcs): Axis? {
        val unitFractionsType = unit.getComponent<FractionsType>() ?: return null
        return getCapitalShipPosition(unitFractionsType)?.currentPosition ?: return null
    }

    private fun UnitEcs.isPlayer(): Boolean =
        this.getComponent<MovingMode>() == MovingMode.WALK
}
