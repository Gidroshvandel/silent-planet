package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.event.SkipTurnEventComponent
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.system.unit.finishTurn

class SkipTurnSystem : EventSystem() {

    override fun execute(gameState: GameState, eventEcs: EventEcs): Boolean {
        eventEcs.getComponent<SkipTurnEventComponent>()?.let {
            if (it.unitEcs == null) {
                gameState.finishTurn()
            } else {
                gameState.finishTurn(it.unitEcs)
            }
            return true
        }
        return false
    }

}