package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.choosePlayerToMove
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.findPathToCell

class AiPlayerSystem(private val aiFractionList: List<FractionsType>) : UnitSystem() {

//    fun GameState.choosePlayerToMove(fractionsType: FractionsType): UnitEcs? {
//        val capitalShip = this.getCapitalShip(fractionsType)
//        val list = this.unitMap.filter { it.getComponent<FractionsType>() == fractionsType }
//        val playerOnGround = list.firstOrNull()
//        if (playerOnGround != null) {
//            return playerOnGround
//        } else {
//            capitalShip?.getComponent<Transport>()?.getFirstPlayerFromTransport(fractionsType)?.let {
//                return it
//            }
//        }
//        return null
//    }

    private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): UnitEcs? =
            this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

    override fun execute(gameState: GameState) {
        if (gameState.turn.canTurn && aiFractionList.contains(gameState.turn.currentTurnFraction)) {
            val unit = gameState.choosePlayerToMove(gameState.turn.currentTurnFraction)
                    ?: gameState.getCapitalShip(gameState.turn.currentTurnFraction)
            unit?.addComponent(ArtificialIntelligence())
            unit?.let { execute(gameState, it) }
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<ArtificialIntelligence>() && !unit.hasComponent<CapitalShip>()) {
            process(unit, gameState)
        }
    }

    private fun process(unit: UnitEcs, gameState: GameState) {
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

    private fun CellEcs.isActualGoal(): Boolean {
        return (isVisible() && getComponent<Crystal>()?.count ?: 0 > 0 || isHide())
    }

    private fun GameState.getSpaceShipPosition(unit: UnitEcs): Axis? {
        val unitFractionsType = unit.getComponent<FractionsType>() ?: return null
        return getCapitalShipPosition(unitFractionsType)?.currentPosition ?: return null
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

}