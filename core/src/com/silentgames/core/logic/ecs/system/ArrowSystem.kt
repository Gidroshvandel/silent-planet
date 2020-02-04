package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Arrow
import com.silentgames.core.logic.ecs.component.ArrowMode.DIRECT
import com.silentgames.core.logic.ecs.component.ArrowMode.SLANTING
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.MovementCoordinatesComponent
import com.silentgames.core.logic.ecs.component.RotateAngle
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.utils.notNull

class ArrowSystem : CellSystem() {
    override fun execute(gameState: GameState, unit: CellEcs) {
        unit.getCurrentPosition()?.let {
            notNull(unit.getComponent(),
                    it,
                    unit,
                    ::move
            )
        }
    }

    private fun move(arrow: Arrow, cellPosition: Axis, cell: CellEcs) {
        val target = arrow.calculateTargetPosition(cellPosition)
        val correctTarget = this.getDestinationCorrect(target)
        CoreLogger.logDebug(
                this::class.simpleName ?: "",
                "${cell.getComponent<Description>()?.name} target $correctTarget"
        )
        cell.addComponent(MovementCoordinatesComponent(correctTarget))
    }

    private fun getDestinationCorrect(destination: Axis): Axis {
        return when {
            destination.y < 0 -> Axis(destination.x, 0)
            destination.y > (Constants.verticalCountOfCells - 1) -> Axis(destination.x, Constants.verticalCountOfCells - 1)
            destination.x < 0 -> Axis(0, destination.y)
            destination.x > (Constants.horizontalCountOfCells - 1) -> Axis(destination.x, Constants.horizontalCountOfCells - 1)

            else -> destination
        }
    }

    private fun Arrow.calculateTargetPosition(position: Axis) =
            when (arrowMode) {
                DIRECT -> rotateAngle.calculateDirectTargetPosition(position, distance)
                SLANTING -> rotateAngle.calculateSlantingTargetPosition(position, distance)
            }

    private fun RotateAngle.calculateSlantingTargetPosition(position: Axis, distance: Int): Axis =
            when (this) {
                RotateAngle.DEGREES0 -> {
                    Axis(
                            position.x + distance,
                            position.y - distance
                    )
                }
                RotateAngle.DEGREES90 -> {
                    Axis(
                            position.x + distance,
                            position.y + distance
                    )
                }
                RotateAngle.DEGREES180 -> {
                    Axis(
                            position.x - distance,
                            position.y + distance
                    )
                }
                RotateAngle.DEGREES270 -> {
                    Axis(
                            position.x - distance,
                            position.y - distance
                    )
                }
            }

    private fun RotateAngle.calculateDirectTargetPosition(position: Axis, distance: Int): Axis =
            when (this) {
                RotateAngle.DEGREES0 -> {
                    Axis(
                            position.x,
                            position.y - distance
                    )
                }
                RotateAngle.DEGREES90 -> {
                    Axis(
                            position.x + distance,
                            position.y
                    )
                }
                RotateAngle.DEGREES180 -> {
                    Axis(
                            position.x,
                            position.y + distance
                    )
                }
                RotateAngle.DEGREES270 -> {
                    Axis(
                            position.x - distance,
                            position.y
                    )
                }
            }

}