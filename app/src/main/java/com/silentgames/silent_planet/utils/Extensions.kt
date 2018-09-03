package com.silentgames.silent_planet.utils

import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip

fun MutableList<EntityType>.removePlayerByName(name: String) {
    getPlayerByName(name)?.let { this.remove(it) }
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

fun MutableList<EntityType>.getSpaceShip(): SpaceShip? =
        this.firstOrNull { it is SpaceShip }?.let { it as SpaceShip }

fun MutableList<EntityType>.removeSpaceShip() {
    getSpaceShip()?.let { this.remove(it) }
}