package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.event.TargetPosition
import com.silentgames.core.logic.ecs.component.event.Teleport
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.system.getName

class TeleportSystem : EventSystem() {

    companion object {
        private const val SYSTEM_TAG = "TeleportSystem"
    }

    override fun execute(gameState: GameState, eventEcs: EventEcs): Boolean {
        if (eventEcs.hasComponent<Teleport>()) {
            eventEcs.getComponent<TargetPosition>()?.let {
                CoreLogger.logDebug(
                    SYSTEM_TAG,
                    "${it.unit.getName()} target ${it.axis}"
                )
                gameState.moveUnit(it.unit, it.axis)
                return true
            }
        }
        return false
    }
}
