package com.silentgames.silent_planet.logic.ecs

import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.logic.ecs.system.System

class Engine(val gameState: GameState) {

    private val systems = mutableListOf<System>()

    fun addSystem(system: System) {
        systems.add(system)
    }

    fun processSystems(entity: Entity) {
        systems.forEach {
            it.execute(gameState, entity)
        }
    }

}