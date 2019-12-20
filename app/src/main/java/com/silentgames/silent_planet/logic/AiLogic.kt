package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.*
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.FractionsType

fun GameMatrix.moveAi(player: EntityPosition<Player>): Boolean {
    return tryMovePlayer(player)
}

fun GameMatrix.choosePlayerToMove(fractionsType: FractionsType): EntityPosition<Player>? {
    val spaceShip = this.getShipPosition(fractionsType)
    val list = this.getCellListByFraction(fractionsType)
    val playerOnGroundCell = list.getPlayerCell()
    val playerOnGround = list.getPlayerCell()?.entityType?.getActive()?.getPlayer()
    if (playerOnGroundCell != null && playerOnGround != null && playerOnGround.isCanMove) {
        return EntityPosition(playerOnGround, playerOnGroundCell.cellType.position)
    } else {
        val playerFromShip = spaceShip.entity.playersOnBord.firstOrNull()
        if (playerFromShip != null) {
            return EntityPosition(playerFromShip, spaceShip.position)
        }
    }
    return null
}

private fun GameMatrix.tryMovePlayer(playerPosition: EntityPosition<Player>): Boolean {
    val targetCell = this.chooseCellToMove(playerPosition)
    return this.tryMovePlayer(targetCell.cellType.position, playerPosition.entity)
}

private fun GameMatrix.chooseCellToMove(playerPosition: EntityPosition<Player>): Cell {
    val cellsAtMoveDistance = this.getCellsAtMoveDistance(playerPosition.position).getCanMoveCells()
    val visibleCells = cellsAtMoveDistance.getVisibleCells()
    return (if (visibleCells.isNotEmpty()) {
        val cellsWithCrystals = visibleCells.getCellsWithCrystals()
        if (cellsWithCrystals.isNotEmpty()) {
            cellsWithCrystals
        } else {
            cellsAtMoveDistance
        }
    } else {
        cellsAtMoveDistance
    }).random()
}

private fun List<Cell>.getCanMoveCells() = filter { it.cellType.isCanMove }

private fun List<Cell>.getCellsWithCrystals() =
        filter { it.cellType.crystals > 0 }

private fun GameMatrix.getCellsAtMoveDistance(position: Axis) =
        getAvailableMoveDistancePositionList(position).map { this.getCell(it) }

private fun List<Cell>.getVisibleCells() = this.filter { it.cellType.isVisible }

private fun List<Cell>.getPlayerCell() = this.find { it.entityType.getPlayer() != null }

private fun List<Cell>.getSpaceShipCell() = this.find { it.entityType.getPlayer() != null }

private fun List<EntityType>.getPlayer() = this.filterIsInstance<Player>().firstOrNull()

private fun List<EntityType>.getActive() = this.filter { it.isCanMove }

private fun GameMatrix.getCellListByFraction(fractionsType: FractionsType) =
        flatten().filter { cell ->
            cell.entityType.find { it.fraction.fractionsType == fractionsType } != null
        }