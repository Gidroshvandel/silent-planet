package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TeleportSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.unitMap.forEach {
            val teleport = it.getComponent<Teleport>()
            val targetPosition = it.getComponent<TargetPosition>()
            if (teleport != null && targetPosition != null) {
                CoreLogger.logDebug(
                        this::class.simpleName ?: "",
                        "${it.getComponent<Description>()?.name} target ${targetPosition.axis}"
                )
                it.removeComponent(teleport)
                it.removeComponent(targetPosition)
                gameState.moveUnit(it, targetPosition.axis)
            }
        }
    }

}