package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CanTurn
import com.silentgames.core.logic.ecs.component.event.SkipTurnEventComponent
import com.silentgames.core.logic.ecs.entity.event.EventEcs

class SkipTurnSystem : EventSystem() {

    override fun execute(gameState: GameState, eventEcs: EventEcs): Boolean {
        eventEcs.getComponent<SkipTurnEventComponent>()?.let {
            if (it.unitEcs == null) {
                gameState.endTurn()
            } else {
                it.unitEcs.removeComponent(CanTurn::class.java)
            }
            return true
        }
        return false
    }

}