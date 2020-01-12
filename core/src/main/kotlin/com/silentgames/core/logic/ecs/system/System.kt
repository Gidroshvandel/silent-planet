package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

interface System {

    fun execute(gameState: GameState, unit: UnitEcs)

    fun execute(gameState: GameState) {}

    fun onEngineAttach(engine: EngineEcs) {}

}