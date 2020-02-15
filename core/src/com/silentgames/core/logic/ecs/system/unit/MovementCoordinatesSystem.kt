package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.MovementCoordinatesComponent
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.event.TeleportEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentUnitCell
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.utils.notNull

class MovementCoordinatesSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "MovementCoordinatesSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            notNull(gameState, unit, cell, ::movementCoordinates)
        }
    }

    private fun movementCoordinates(gameState: GameState, unit: UnitEcs, cell: CellEcs) {
        val position = cell.getComponent<MovementCoordinatesComponent>()?.axis
        if (position != null) {
            CoreLogger.logDebug(SYSTEM_TAG, "${cell.getName()} target $position")
            gameState.addEvent(TeleportEvent(position, unit))
        }
    }
}