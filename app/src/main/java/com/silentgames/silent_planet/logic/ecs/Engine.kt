package com.silentgames.silent_planet.logic.ecs

import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.ecs.system.System

class Engine(val gameState: GameState) {

    private val systems = mutableListOf<System>()

    var onProcessingChanged: ((Boolean) -> kotlin.Unit)? = null

    var processing = false
        set(value) {
            field = value
            onProcessingChanged?.invoke(value)
        }

    fun addSystem(system: System) {
        system.onEngineAttach(this)
        systems.add(system)
    }

    fun processSystems() {
        if (!processing) {
            systems.forEach {
                it.execute(gameState)
            }
        }
    }

    fun processSystems(entity: Unit) {
        if (!processing) {
            forceProcessSystem(entity)
        }
    }

    fun forceProcessSystem(entity: Unit) {
        systems.forEach {
            it.execute(gameState, entity)
        }
    }

}