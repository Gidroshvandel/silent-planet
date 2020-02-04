package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class SaveUnitFromSpaceSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<FractionsType>()?.let {
            notNull(unit.getCurrentPosition(),
                    gameState.getCapitalShipPosition(it)?.currentPosition,
                    unit,
                    ::moveUnitToShip)
        }
    }

    private fun moveUnitToShip(currentPosition: Axis, shipPosition: Axis, unit: UnitEcs) {
        if (currentPosition != shipPosition && !currentPosition.inGroundBorders()) {
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