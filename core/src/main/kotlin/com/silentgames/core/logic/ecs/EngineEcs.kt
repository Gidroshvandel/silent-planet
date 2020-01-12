package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.System

class EngineEcs(val gameState: GameState) {

    private val systems = mutableListOf<System>()

    var onProcessingChanged: ((Boolean) -> Unit)? = null

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

    fun processSystems(entity: UnitEcs) {
        if (!processing) {
            forceProcessSystem(entity)
        }
    }

    fun forceProcessSystem(entity: UnitEcs) {
        systems.forEach {
            it.execute(gameState, entity)
        }
    }

    fun stop() {
        processing = true
        systems.clear()
        processing = false
    }

}