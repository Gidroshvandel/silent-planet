package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.MovementCoordinatesComponent
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class MovementCoordinatesSystem : CellSystem() {
    override fun execute(gameState: GameState, unit: CellEcs) {
        unit.getCurrentPosition()?.let {
            notNull(gameState.getUnits(it), unit, ::movementCoordinates)
        }
    }

    private fun movementCoordinates(units: List<UnitEcs>, cell: CellEcs) {
        units.forEach {
            val position = cell.getComponent<MovementCoordinatesComponent>()?.axis
            if (position != null) {
                CoreLogger.logDebug(
                        this::class.simpleName ?: "",
                        "${cell.getComponent<Description>()?.name} target $position"
                )
                it.addComponent(Teleport())
                it.addComponent(TargetPosition(position))
            }
        }
    }
}