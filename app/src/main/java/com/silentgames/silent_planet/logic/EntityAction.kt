package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.effects.CaptureEffect
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.model.getCell
import com.silentgames.silent_planet.model.getShip
import com.silentgames.silent_planet.utils.ShipNotFoundException
import com.silentgames.silent_planet.utils.getSpaceShip
import com.silentgames.silent_planet.utils.isSpaceShipBelongFraction

fun GameMatrix.moveEntity(target: Axis, entityAxis: Axis, entity: EntityType): Boolean {
    return if (targetIsAvailable(target, entityAxis)) {
        when (entity) {
            is SpaceShip -> moveShip(target, Entity(entity, entityAxis))
            is Player -> movePlayer(target, Entity(entity, entityAxis))
            else -> false
        }
    } else {
        false
    }
}


fun GameMatrix.moveShip(target: Axis, spaceShip: Entity<SpaceShip>): Boolean {
    val targetCell = this.getCell(target)
    return if (spaceShip.entity.isCanFly
            && targetCell.cellType.isCanFly
            && !this.isSpaceShip(target)) {
        moveShipTo(target, spaceShip)
        true
    } else {
        false
    }
}

private fun GameMatrix.moveShipTo(target: Axis, spaceShip: Entity<SpaceShip>) {
    val targetCell = this.getCell(target)
    val currentCell = this.getCell(spaceShip.axis)
    targetCell.entityType.add(spaceShip.entity)
    targetCell.cellType.isVisible = true
    currentCell.entityType.remove(spaceShip.entity)
}

fun GameMatrix.targetIsAvailable(target: Axis, current: Axis) =
        isMoveAtDistance(target, current)
                && this.isNowPlaying(current, TurnHandler.fractionType)

private fun isMoveAtDistance(target: Axis, current: Axis): Boolean {
    if (!(current.x == target.x && current.y == target.y)) {
        for (i in 0..2) {
            for (j in 0..2) {
                if (current.x + j - 1 == target.x && current.y + i - 1 == target.y) {
                    return true
                }
            }
        }
    }
    return false
}

private fun GameMatrix.isNowPlaying(axis: Axis, fractionsType: FractionsType): Boolean {
    return this.getCell(axis).entityType.firstOrNull {
        it.fraction.fractionsType == fractionsType
    } != null
}

fun GameMatrix.movePlayer(target: Axis, player: Entity<Player>): Boolean {
    val targetCell = this.getCell(target)
    return if (targetCell.cellType.isCanMove && player.entity.isCanMove) {
        val enemy = this.getEnemy(target, TurnHandler.fractionType)
        if (enemy != null) {
            this.captureEnemyUnit(player, enemy)
            true
        } else {
            this.movePlayerTo(target, player)
            true
        }
    } else if (isSpaceShip(target) && targetCell.entityType.isSpaceShipBelongFraction(player.entity)) {
        this.moveOnBoardAllyShip(player)
        true
    } else {
        false
    }
}

private fun GameMatrix.isSpaceShip(axis: Axis): Boolean {
    return this.getCell(axis).entityType.getSpaceShip() != null
}

fun GameMatrix.movePlayerTo(target: Axis, player: Entity<Player>) {
    val targetCell = this.getCell(target)
    targetCell.entityType.add(player.entity)
    targetCell.cellType.isVisible = true
    deletePlayer(player)
}

fun GameMatrix.buyBack(
        entity: Entity<Player>,
        onSuccess: () -> Unit,
        onFailure: (missingAmount: Int) -> Unit
) {
    val playerShip = getShip(entity.entity.fraction.fractionsType)
    val captureEffect = entity.entity.getEffect<CaptureEffect>()
    if (playerShip.crystals >= captureEffect?.buybackPrice ?: 0) {
        playerShip.crystals = playerShip.crystals - (captureEffect?.buybackPrice ?: 0)
        captureEffect?.remove()
        moveOnBoard(entity, playerShip)
        onSuccess.invoke()
    } else {
        onFailure.invoke(captureEffect?.buybackPrice?.minus(playerShip.crystals) ?: 0)
    }
}

fun GameMatrix.captureEnemyUnit(entity: Entity<Player>, enemyEntity: Entity<Player>) {
    moveOnBoardAllyShip(entity)
    CaptureEffect(enemyEntity.entity, entity.entity.fraction.fractionsType)
    moveOnBoardFractionShip(enemyEntity, entity.entity.fraction.fractionsType)
}

fun GameMatrix.getEnemy(axis: Axis, currentFraction: FractionsType): Entity<Player>? {
    val enemy = getCell(axis).entityType.getEnemy(currentFraction)
    return if (enemy != null) Entity(enemy, axis) else null
}

private fun MutableList<EntityType>.getEnemy(currentFraction: FractionsType) =
        find { !it.isDead && it.fraction.fractionsType != currentFraction && it is Player } as? Player

private fun GameMatrix.moveOnBoard(current: Entity<Player>, spaceShip: SpaceShip) {
    spaceShip.crystals = spaceShip.crystals + current.entity.crystals
    spaceShip.playersOnBord.add(current.entity.apply { crystals = 0 })
    this.deletePlayer(current)
}

fun GameMatrix.moveOnBoardAllyShip(current: Entity<Player>) {
    moveOnBoard(current, findFractionShip(current.entity.fraction.fractionsType).entity)
}

private fun GameMatrix.moveOnBoardFractionShip(current: Entity<Player>, fractionsType: FractionsType) {
    moveOnBoard(current, findFractionShip(fractionsType).entity)
}

private fun GameMatrix.findFractionShip(fractionsType: FractionsType): Entity<SpaceShip> {
    var spaceShip: SpaceShip? = null
    this.forEachIndexed { x, arrayOfCells ->
        val y = arrayOfCells.indexOfFirst {
            spaceShip = it.entityType.getSpaceShip()
            if (spaceShip?.fraction?.fractionsType == fractionsType) {
                true
            } else {
                spaceShip = null
                false
            }
        }
        spaceShip?.let {
            if (y != -1) {
                return Entity(it, Axis(x, y))
            }
        }
    }
    throw ShipNotFoundException()
}

private fun GameMatrix.deletePlayer(current: Entity<Player>) {
    this.getCell(current.axis).entityType.deletePlayer(current.entity)
}

private fun MutableList<EntityType>.deletePlayer(player: Player) {
    if (!remove(player)) {
        getSpaceShip()?.playersOnBord?.remove(player)
    }
}