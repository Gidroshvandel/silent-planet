package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.effects.CaptureEffect
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.ShipNotFoundException
import com.silentgames.silent_planet.utils.getSpaceShip

fun GameMatrixHelper.buyBack(
        player: Player,
        onSuccess: () -> Unit,
        onFailure: (missingAmount: Int) -> Unit
) {
    buyBack(Entity(player, currentXY), onSuccess, onFailure)
}

fun GameMatrixHelper.buyBack(
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

fun GameMatrixHelper.captureEnemyUnit(entity: Entity<Player>, enemyEntity: Entity<Player>) {
    moveOnBoardAllyShip(entity)
    CaptureEffect(enemyEntity.entity, entity.entity.fraction.fractionsType)
    moveOnBoardFractionShip(enemyEntity, entity.entity.fraction.fractionsType)
}

fun GameMatrixHelper.getEnemy(axis: Axis): Entity<Player>? {
    val enemy = getCell(axis).entityType.getEnemy(currentTurnFraction)
    return if (enemy != null) Entity(enemy, axis) else null
}

private fun MutableList<EntityType>.getEnemy(currentFraction: FractionsType) =
        find { !it.isDead && it.fraction.fractionsType != currentFraction && it is Player } as? Player

private fun GameMatrixHelper.moveOnBoard(current: Entity<Player>, spaceShip: SpaceShip) {
    spaceShip.crystals = spaceShip.crystals + current.entity.crystals
    spaceShip.playersOnBord.add(current.entity.apply { crystals = 0 })
    this.deletePlayer(current)
}

fun GameMatrixHelper.moveOnBoardAllyShip(current: Entity<Player>) {
    moveOnBoard(current, findFractionShip(current.entity.fraction.fractionsType).entity)
}

private fun GameMatrixHelper.moveOnBoardFractionShip(current: Entity<Player>, fractionsType: FractionsType) {
    moveOnBoard(current, findFractionShip(fractionsType).entity)
}

private fun GameMatrixHelper.findFractionShip(fractionsType: FractionsType): Entity<SpaceShip> {
    var spaceShip: SpaceShip? = null
    gameMatrix.forEachIndexed { x, arrayOfCells ->
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

private fun GameMatrixHelper.deletePlayer(current: Entity<Player>) {
    this.getCell(current.axis).entityType.deletePlayer(current.entity)
}

private fun MutableList<EntityType>.deletePlayer(player: Player) {
    if (!remove(player)) {
        getSpaceShip()?.playersOnBord?.remove(player)
    }
}