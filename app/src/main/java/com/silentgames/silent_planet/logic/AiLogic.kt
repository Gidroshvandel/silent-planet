package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.*
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.FractionsType

fun GameMatrix.choosePlayerToMove(fractionsType: FractionsType): EntityPosition<Player>? {
    val spaceShip = this.getShipPosition(fractionsType)
    val list = this.getCellListByFraction(fractionsType)
    val playerOnGroundCell = list.getPlayerListCell().getWithActivePlayer().firstOrNull()
    val playerOnGround = playerOnGroundCell?.entityType?.getActive()?.getPlayer()
    if (playerOnGroundCell != null && playerOnGround != null) {
        return EntityPosition(playerOnGround, playerOnGroundCell.cellType.position)
    } else {
        val playerFromShip = spaceShip.entity.getFirstPlayerFromShip()
        if (playerFromShip != null) {
            return EntityPosition(playerFromShip, spaceShip.position)
        }
    }
    return null
}

fun GameMatrix.moveAi(player: EntityPosition<Player>): Boolean {
    val currentCell = this.getCell(player.position)
    return if (currentCell.cellType.crystals > 0) {
        player.entity.addCrystals(currentCell.cellType.takeAllCrystals())
        this.tryReturnToShip(player)
    } else if (player.entity.crystals > 0) {
        this.tryReturnToShip(player)
    } else {
        this.tryFindCrystals(player)
    }
}

private fun SpaceShip.getFirstPlayerFromShip(): Player? =
        this.playersOnBord.firstOrNull { it.fraction.fractionsType == this.fraction.fractionsType }

private fun GameMatrix.tryReturnToShip(player: EntityPosition<Player>): Boolean {
    val spaceShip = this.getShipPosition(player.entity.fraction.fractionsType)
    val path = this.findPath(player.position, spaceShip.position, player.entity)
    return if (path.isNotEmpty()) {
        this.tryMovePlayer(path.last(), player.entity)
    } else {
        false
    }
}

private fun GameMatrix.tryFindCrystals(playerPosition: EntityPosition<Player>): Boolean {
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

private fun List<Cell>.getPlayerListCell() = this.filter { it.entityType.getPlayer() != null }

private fun List<Cell>.getSpaceShipCell() = this.find { it.entityType.getPlayer() != null }

private fun List<EntityType>.getPlayer() = this.filterIsInstance<Player>().firstOrNull()

private fun List<EntityType>.getActive() = this.filter { it.isCanMove }

private fun List<Cell>.getWithActivePlayer() = this.filter { it.entityType.filter { it.isCanMove }.isNotEmpty() }

private fun GameMatrix.getCellListByFraction(fractionsType: FractionsType) =
        flatten().filter { cell ->
            cell.entityType.find { it.fraction.fractionsType == fractionsType } != null
        }