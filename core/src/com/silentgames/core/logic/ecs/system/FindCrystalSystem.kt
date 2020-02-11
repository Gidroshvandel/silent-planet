package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.findPathToCell

class FindCrystalSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<ArtificialIntelligence>() && unit.isFindCrystalMode() && unit.isPlayer()) {
            gameState.chooseCellToMove(unit)?.let {
                unit.addComponent(TargetPosition(it))
            }
        }
    }

    private fun GameState.chooseCellToMove(unit: UnitEcs): Axis? {
        val position = unit.getCurrentPosition() ?: return null
        val cellsAtMoveDistance = this.getCellsAtMoveDistance(position).getCanMoveCells()
        val visibleCells = cellsAtMoveDistance.getVisibleCells()
        return (if (visibleCells.isNotEmpty()) {
            val cellsWithCrystals = visibleCells.getCellsWithCrystals()
            if (cellsWithCrystals.isNotEmpty()) {
                cellsWithCrystals
            } else {
                val path = this.findPathToCell(unit) { it.isHide() && it.getComponent<MovingMode>() == MovingMode.WALK }
                if (path.isNotEmpty()) {
                    unit.addComponent(Goal(path.first()))
                    null
                } else {
                    cellsAtMoveDistance
                }
            }
        } else {
            cellsAtMoveDistance
        })?.random()?.getCurrentPosition()
    }

    private fun List<CellEcs>.getCanMoveCells() = filter { it.getComponent<MovingMode>() == MovingMode.WALK }

    private fun List<CellEcs>.getVisibleCells() = this.filter { it.isVisible() }

    private fun List<CellEcs>.getCellsWithCrystals() = filter { it.getCrystalsCount() > 0 }

    private fun UnitEcs.isFindCrystalMode(): Boolean =
            this.getComponent<CrystalBag>()?.isFull == false && !this.hasComponent<Goal>()

    private fun UnitEcs.isPlayer(): Boolean =
            this.getComponent<MovingMode>() == MovingMode.WALK
}