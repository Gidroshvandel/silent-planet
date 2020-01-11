package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.TargetPosition
import com.silentgames.silent_planet.logic.ecs.component.Teleport
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs
import com.silentgames.silent_planet.logic.ecs.extractTransports

class TeleportSystem : System {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.unitMap.extractTransports().forEach {
            val teleport = it.getComponent<Teleport>()
            val targetPosition = it.getComponent<TargetPosition>()
            if (teleport != null && targetPosition != null) {
                it.removeComponent(teleport)
                it.removeComponent(targetPosition)
                gameState.moveUnit(it, targetPosition.axis)
            }
        }
    }

}