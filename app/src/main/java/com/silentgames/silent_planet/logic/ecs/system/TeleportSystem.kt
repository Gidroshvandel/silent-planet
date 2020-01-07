package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Teleport
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class TeleportSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        gameState.unitMap.forEach {
            val teleport = it.getComponent<Teleport>()
            if (teleport != null) {
                it.removeComponent(teleport)
                gameState.moveUnit(it, teleport.target)
            }
        }
    }

}