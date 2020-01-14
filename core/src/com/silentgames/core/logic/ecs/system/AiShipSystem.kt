package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.ArtificialIntelligence
import com.silentgames.core.logic.ecs.component.CapitalShip
import com.silentgames.core.logic.ecs.component.MovingMode
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class AiShipSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (gameState.turn.canTurn && !unit.hasComponent<TargetPosition>()
                && unit.hasComponent<ArtificialIntelligence>()
                && unit.hasComponent<CapitalShip>()) {
            unit.getMoveShipPosition(gameState)?.let {
                unit.addComponent(TargetPosition(it))
            }
        }
    }

    private fun UnitEcs.getMoveShipPosition(gameState: GameState): Axis? {
        val shipPosition = getCurrentPosition() ?: return null
        val cellsAtMoveDistance = gameState.getCellsAtMoveDistance(shipPosition).getCanMoveCells(this).filter {
            val position = it.getCurrentPosition()
            position != null && gameState.getUnit(position) == null
        }
        return if (cellsAtMoveDistance.isNotEmpty()) {
            cellsAtMoveDistance.random().getCurrentPosition()
        } else {
            null
        }
    }

    private fun List<CellEcs>.getCanMoveCells(unit: UnitEcs) = filter {
        unit.getComponent<MovingMode>() == it.getComponent<MovingMode>()
    }

}