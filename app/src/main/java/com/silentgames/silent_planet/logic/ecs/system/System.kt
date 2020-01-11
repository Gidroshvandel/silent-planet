package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.EngineEcs
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs

interface System {

    fun execute(gameState: GameState, unit: UnitEcs)

    fun execute(gameState: GameState) {}

    fun onEngineAttach(engine: EngineEcs) {}

}