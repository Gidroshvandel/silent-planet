package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.system.System

abstract class EventSystem : System {

    override fun execute(gameState: GameState) {
        gameState.eventList.forEach {
            if (execute(gameState, it)) {
                gameState.removeEvent(it)
            }
        }
    }

    abstract fun execute(gameState: GameState, eventEcs: EventEcs): Boolean
}
