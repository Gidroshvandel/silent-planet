package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.entity.event.TeleportEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentPosition
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.utils.notNull

class SaveUnitFromSpaceSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "SaveUnitFromSpaceSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<FractionsType>()?.let {
            notNull(
                    gameState,
                    unit.getCurrentPosition(),
                    gameState.getCapitalShipPosition(it)?.currentPosition,
                    unit,
                    ::moveUnitToShip
            )
        }
    }

    private fun moveUnitToShip(gameState: GameState, currentPosition: Axis, shipPosition: Axis, unit: UnitEcs) {
        if (currentPosition != shipPosition && !currentPosition.inGroundBorders()) {
            CoreLogger.logDebug(SYSTEM_TAG, "${unit.getName()} with position ${unit.getCurrentPosition()} moved to target $shipPosition")
            gameState.addEvent(TeleportEvent(shipPosition, unit))
        }
    }

    private fun Axis.inGroundBorders(): Boolean {
        return x <= Constants.verticalCountOfGroundCells &&
                x >= 1 &&
                y <= Constants.horizontalCountOfGroundCells &&
                y >= 1
    }
}