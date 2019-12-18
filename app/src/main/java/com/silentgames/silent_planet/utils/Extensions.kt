package com.silentgames.silent_planet.utils

import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip

fun MutableList<EntityType>.removePlayerByName(name: String) {
    getPlayerByName(name)?.let { this.remove(it) }
}

fun MutableList<EntityType>.getEntityList(): MutableList<EntityType> {
    val entityTypeList = mutableListOf<EntityType>()
    this.forEach {
        if (it is SpaceShip) {
            entityTypeList.add(it)
            entityTypeList.addAll(it.playersOnBord)
        } else {
            entityTypeList.add(it)
        }
    }
    return entityTypeList
}


fun MutableList<EntityType>.getPlayerByName(name: String): Player? {
    this.forEach { entityType ->
        return if (entityType is Player && entityType.name == name) {
            entityType
        } else if (entityType is SpaceShip) {
            entityType.playersOnBord.find { player ->
                player.name === name
            }
        } else {
            null
        }
    }
    return null
}

fun MutableList<EntityType>.getAllPlayersFromCell(): List<Player> {
    val data = mutableListOf<Player>()
    if (this.isNotEmpty()) {
        for (entityType in this) {
            if (entityType is SpaceShip) {
                entityType.playersOnBord.forEach { data.add(it) }
            } else if (entityType is Player) {
                data.add(entityType)
            }
        }
    }
    return data
}

fun MutableList<EntityType>.getDeathPlayersFromCell(): List<Player> {
    val data = mutableListOf<Player>()
    if (this.isNotEmpty()) {
        for (entityType in this) {
            if (entityType is SpaceShip) {
                entityType.playersOnBord.forEach {
                    if (it.isDead) {
                        data.add(it)
                    }
                }
            } else if (entityType is Player && entityType.isDead) {
                data.add(entityType)
            }
        }
    }
    return data
}

fun MutableList<EntityType>.isSpaceShip(): Boolean = this.getSpaceShip() != null

fun MutableList<EntityType>.getSpaceShip(): SpaceShip? =
        this.find { it is SpaceShip }?.let { it as SpaceShip }

fun MutableList<EntityType>.isSpaceShipBelongFraction(entityType: EntityType): Boolean =
        entityType.fraction.fractionsType == this.getSpaceShip()?.fraction?.fractionsType

fun MutableList<EntityType>.removeSpaceShip() {
    getSpaceShip()?.let { this.remove(it) }
}

inline fun <reified T : SpaceShip> Array<Array<Cell>>.findSpaceShip(): T {
    this.forEachIndexed { x, arrayOfCells ->
        arrayOfCells.forEachIndexed { y, cell ->
            if (x == 0 || x == this.size - 1 || y == 0 || y == arrayOfCells.size - 1) {
                val ship = cell.entityType.getSpaceShip()
                if (ship != null && ship is T)
                    return ship
            }
        }
    }
    throw ShipNotFoundException()
}