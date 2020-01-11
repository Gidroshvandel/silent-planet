package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.*
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.FractionsType

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

fun GameMatrix.moveAi(player: EntityPosition<Player>): Boolean {
    val currentCell = this.getCell(player.position)
    return when {
        player.entity.isGoalActual(this) -> {
            this.moveToGoal(player)
        }
        currentCell.cellType.crystals > 0 -> {
            player.entity.addCrystals(currentCell.cellType.takeAllCrystals())
            this.tryReturnToShip(player)
        }
        player.entity.crystals > 0 -> {
            this.tryReturnToShip(player)
        }
        else -> {
            this.tryFindCrystals(player)
        }
    }
}

fun GameMatrix.moveAiShip(fractionsType: FractionsType): Boolean {
    val shipPosition = this.getShipPosition(fractionsType)
    val cellsAtMoveDistance = this.getCellsAtMoveDistance(shipPosition.position).getCanFlyCells()
    val targetCell = cellsAtMoveDistance.random()
    return this.canMoveShip(targetCell.position, shipPosition.entity) {
        this.moveShip(targetCell.position, shipPosition.entity)
    }
}

private fun EntityType.isGoalActual(matrix: GameMatrix): Boolean {
    val goal = this.goal
    return if (goal != null
            && (matrix.getCell(goal).cellType.isVisible && matrix.getCell(goal).cellType.crystals > 0
                    || !matrix.getCell(goal).cellType.isVisible)) {
        true
    } else {
        this.goal = null
        false
    }
}

private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): Unit? =
        this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

private fun GameMatrix.tryReturnToShip(player: EntityPosition<Player>): Boolean {
//    val spaceShip = this.getShipPosition(player.entity.fraction.fractionsType)
//    val path = this.findPath(player.position, spaceShip.position, player.entity)
//    return if (path.isNotEmpty()) {
//        this.tryMovePlayer(path.last(), player.entity)
//    } else {
//        false
//    }
    return false
}

private fun GameMatrix.tryFindCrystals(playerPosition: EntityPosition<Player>): Boolean {
    val targetCell = this.chooseCellToMove(playerPosition)
    if (targetCell != null) {
        return this.tryMovePlayer(targetCell.cellType.position, playerPosition.entity)
    } else {
        return this.moveToGoal(playerPosition)
    }
}

private fun GameMatrix.chooseCellToMove(playerPosition: EntityPosition<Player>): Cell? {
//    val cellsAtMoveDistance = this.getCellsAtMoveDistance(playerPosition.position).getCanMoveCells()
//    val visibleCells = cellsAtMoveDistance.getVisibleCells()
//    return (if (visibleCells.isNotEmpty()) {
//        val cellsWithCrystals = visibleCells.getCellsWithCrystals()
//        if (cellsWithCrystals.isNotEmpty()) {
//            cellsWithCrystals
//        } else {
//            val path = this.findPathToCell(playerPosition) { !it.cellType.isVisible && it.cellType.isCanMove }
//            if (path.isNotEmpty()) {
//                playerPosition.entity.goal = path.first()
//                null
//            } else {
//                cellsAtMoveDistance
//            }
//        }
//    } else {
//        cellsAtMoveDistance
//    })?.random()
    return null
}

private fun GameMatrix.moveToGoal(playerPosition: EntityPosition<Player>): Boolean {
//    val goal = playerPosition.entity.goal
//    if (goal != null) {
//        val path = this.findPath(playerPosition.position, goal, playerPosition.entity)
//        if (path.isNotEmpty()) {
//            if (path.last() == goal) {
//                playerPosition.entity.goal = null
//            }
//            return if (this.tryMovePlayer(path.last(), playerPosition.entity)) {
//                true
//            } else {
//                playerPosition.entity.goal = null
//                false
//            }
//        }
//    }
    return false
}

private fun List<Cell>.getCanMoveCells() = filter { it.cellType.isCanMove }

private fun List<Cell>.getCanFlyCells() = filter { it.cellType.isCanFly }

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