package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.system.RenderSystem
import com.silentgames.core.logic.ecs.system.System

class EngineEcs(val gameState: GameState) {

    private val systems = mutableListOf<System>()

    var processing = false

    fun addSystem(vararg system: System) {
        processing = true
        system.forEach {
            addSystem(it)
        }
        processing = false
        processSystems()
    }

    fun addEvent(event: EventEcs) {
        gameState.addEvent(event)
        processSystems()
    }

    fun processing() {
        if (processing) {
            systems.find { it is RenderSystem }?.execute(gameState)
        } else {
            processSystems()
        }
    }

    fun stop() {
        processing = true
        systems.clear()
        processing = false
    }

    private fun addSystem(system: System) {
        system.onEngineAttach(this)
        systems.add(system)
    }

    private fun processSystems() {
        if (!processing) {
            systems.forEach {
                it.execute(gameState)
            }
        }
    }

}