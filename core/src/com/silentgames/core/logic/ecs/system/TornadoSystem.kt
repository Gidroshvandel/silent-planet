package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.MovementCoordinatesComponent
import com.silentgames.core.logic.ecs.component.Tornado
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.system.TornadoSystem.GameZone.*
import com.silentgames.core.utils.notNull

class TornadoSystem : CellSystem() {

    companion object {
        private const val SYSTEM_TAG = "TornadoSystem"
    }

    private enum class GameZone(val axis: Axis) {
        UP_LEFT(Axis(1, 1)),
        UP_RIGHT(Axis(1, Constants.horizontalCountOfGroundCells)),
        BOTTOM_LEFT(Axis(Constants.verticalCountOfGroundCells, 1)),
        BOTTOM_RIGHT(Axis(Constants.verticalCountOfGroundCells, Constants.horizontalCountOfGroundCells))
    }

    override fun execute(gameState: GameState, unit: CellEcs) {
        unit.getComponent<Tornado>()?.let {
            notNull(unit.getCurrentPosition(),
                    unit,
                    ::tornado)
        }
    }

    private fun tornado(position: Axis, cell: CellEcs) {
        val isAnglePosition = values().toList().map { it.axis }.contains(position)
        val axis = if (isAnglePosition) this.getSpacePosition(position) else this.getAnglePosition(position)
        if (axis != null) {
//            CoreLogger.logDebug(SYSTEM_TAG, "${cell.getName()} moved to $axis")
            cell.addComponent(MovementCoordinatesComponent(axis))
        }
    }

    private fun getSpacePosition(position: Axis): Axis? {
        return when (position) {
            UP_LEFT.axis -> Axis(0, 0)
            UP_RIGHT.axis -> Axis(0, Constants.horizontalCountOfCells - 1)
            BOTTOM_LEFT.axis -> Axis(Constants.verticalCountOfCells - 1, 0)
            BOTTOM_RIGHT.axis -> Axis(Constants.verticalCountOfCells - 1, Constants.horizontalCountOfCells - 1)
            else -> null
        }
    }

    private fun getAnglePosition(position: Axis): Axis? {
        val middlePosition = Axis(
                Constants.horizontalCountOfGroundCells / 2,
                Constants.verticalCountOfGroundCells / 2
        )
        return when {
            position.x <= middlePosition.x && position.y <= middlePosition.y -> UP_LEFT.axis
            position.x <= middlePosition.x && position.y > middlePosition.y -> UP_RIGHT.axis
            position.x > middlePosition.x && position.y <= middlePosition.y -> BOTTOM_LEFT.axis
            position.x > middlePosition.x && position.y > middlePosition.y -> BOTTOM_RIGHT.axis

            else -> null
        }
    }
}