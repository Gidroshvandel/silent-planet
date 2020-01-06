package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

interface System {

    fun execute(gameState: GameState, unit: Unit)

}