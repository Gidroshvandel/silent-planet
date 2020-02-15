package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.system.System

class EngineEcs(val gameState: GameState) {

    val systems = mutableListOf<System>()

    var onProcessingChanged: ((Boolean) -> Unit)? = null

    var processing = false
        set(value) {
            field = value
            onProcessingChanged?.invoke(value)
        }

    fun addSystem(vararg system: System) {
        systems.forEach {
            addSystem(it)
        }
    }

    fun addSystem(system: System) {
        system.onEngineAttach(this)
        systems.add(system)
    }

    fun addEvent(event: EventEcs) {
        gameState.addEvent(event)
        processSystems()
    }

    fun processSystems() {
        if (!processing) {
            systems.forEach {
                it.execute(gameState)
            }
        }
    }

    fun stop() {
        processing = true
        systems.clear()
        processing = false
    }

}