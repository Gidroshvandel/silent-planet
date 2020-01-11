package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.ArtificialIntelligence
import com.silentgames.silent_planet.logic.ecs.component.Goal
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.TargetPosition
import com.silentgames.silent_planet.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.findPathToCell
import com.silentgames.silent_planet.logic.getAvailableMoveDistancePositionList
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType

class AiSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        if (unit.hasComponent<ArtificialIntelligence>()) {
            process(unit, gameState)
        }
    }

    private fun process(unit: Unit, gameState: GameState) {
        gameState.getCurrentUnitCell(unit) { cell ->
            when {
                cell.getCrystalsCount() > 0 -> {
                    unit.addComponent(AddCrystalEvent(cell.getCrystalsCount()))
                    gameState.getSpaceShipPosition(unit)?.let {
                        unit.addComponent(Goal(it))
                    }
                }
                unit.getCrystalsCount() > 0 -> {
                    gameState.getSpaceShipPosition(unit)?.let {
                        unit.addComponent(Goal(it))
                    }
                }
                else -> {
                    gameState.chooseCellToMove(unit)?.let {
                        unit.addComponent(TargetPosition(it))
                    }
                }
            }
        }
    }

    private fun GameState.getSpaceShipPosition(unit: Unit): Axis? {
        val unitFractionsType = unit.getComponent<FractionsType>() ?: return null
        return getCapitalShipPosition(unitFractionsType)?.currentPosition ?: return null
    }

    private fun GameState.chooseCellToMove(unit: Unit): Axis? {
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

    private fun GameState.getCellsAtMoveDistance(position: Axis) =
            getAvailableMoveDistancePositionList(position).mapNotNull { this.getCell(it) }

    private fun List<Cell>.getCanMoveCells() = filter { it.getComponent<MovingMode>() == MovingMode.WALK }

    private fun List<Cell>.getVisibleCells() = this.filter { it.isVisible() }

    private fun List<Cell>.getCellsWithCrystals() = filter { it.getCrystalsCount() > 0 }

}