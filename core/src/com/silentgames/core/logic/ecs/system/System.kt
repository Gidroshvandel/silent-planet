package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState

interface System {

    fun execute(gameState: GameState) {}

    fun onEngineAttach(engine: EngineEcs) {}
}
