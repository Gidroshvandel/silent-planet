package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.logic.ecs.entity.Entity

class Transport(private val unitsOnBoard: MutableList<Entity>) : ComponentEquals() {

    fun addOnBoard(entity: Entity) {
        unitsOnBoard.add(entity)
    }

    fun removeFromBoard(entity: Entity) {
        unitsOnBoard.remove(entity)
    }

}