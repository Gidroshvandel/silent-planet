package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.MovementCoordinatesComponent
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class MovementCoordinatesSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "MovementCoordinatesSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            notNull(unit, cell, ::movementCoordinates)
        }
    }

    private fun movementCoordinates(unit: UnitEcs, cell: CellEcs) {
        val position = cell.getComponent<MovementCoordinatesComponent>()?.axis
        if (position != null) {
            CoreLogger.logDebug(SYSTEM_TAG, "${cell.getName()} target $position")
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(position))
        }
    }
}