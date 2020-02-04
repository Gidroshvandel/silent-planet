package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.MotionType
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class MoveUnitToShipSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<FractionsType>()?.let {
            notNull(unit.getComponent<Route>()?.paths,
                    unit.getCurrentPosition(),
                    gameState.getCapitalShipPosition(it)?.currentPosition,
                    unit,
                    ::moveUnitToShip)
        }
    }

    private fun moveUnitToShip(paths: List<Motion>, currentPosition: Axis, shipPosition: Axis, unit: UnitEcs) {

        if (currentPosition != shipPosition && !currentPosition.inGroundBorders() && paths.lastOrNull()?.motionType == MotionType.TELEPORT) {
            CoreLogger.logDebug(
                    this::class.simpleName ?: "",
                    "${unit.getComponent<Description>()?.name} target $shipPosition"
            )
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(shipPosition))
        }
    }

    private fun Axis.inGroundBorders(): Boolean {
        return x <= Constants.verticalCountOfGroundCells &&
                x >= 1 &&
                y <= Constants.horizontalCountOfGroundCells &&
                y >= 1
    }
}