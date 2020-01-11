package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.findPathToCell
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType

class AiPlayerSystem() : System {

    fun GameState.choosePlayerToMove(fractionsType: FractionsType): Unit? {
        val capitalShip = this.getCapitalShip(fractionsType)
        val list = this.unitMap.filter { it.getComponent<FractionsType>() == fractionsType }
        val playerOnGround = list.firstOrNull()
        if (playerOnGround != null) {
            return playerOnGround
        } else {
            capitalShip?.getComponent<Transport>()?.getFirstPlayerFromTransport(fractionsType)?.let {
                return it
            }
        }
        return null
    }

    private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): Unit? =
            this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

    override fun execute(gameState: GameState, unit: Unit) {
        if (unit.hasComponent<ArtificialIntelligence>()) {
            process(unit, gameState)
        }
    }

    private fun process(unit: Unit, gameState: GameState) {
        gameState.getCurrentUnitCell(unit) { cell ->
            val goal = unit.getComponent<Goal>()
            val goalCell = goal?.axis?.let { gameState.getCell(it) }
            if ((goalCell == null || !goalCell.isActualGoal()) && (goalCell != null || goal == null)) {
                unit.removeComponent(Goal::class.java)
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
    }

    private fun Cell.isActualGoal(): Boolean {
        return (isVisible() && getComponent<Crystal>()?.count ?: 0 > 0 || isHide())
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

    private fun List<Cell>.getCanMoveCells() = filter { it.getComponent<MovingMode>() == MovingMode.WALK }

    private fun List<Cell>.getVisibleCells() = this.filter { it.isVisible() }

    private fun List<Cell>.getCellsWithCrystals() = filter { it.getCrystalsCount() > 0 }

}