package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

abstract class UnitSystem : System {

    abstract fun execute(gameState: GameState, unit: UnitEcs)

    override fun execute(gameState: GameState) {
        gameState.unitMap.forEach {
            execute(gameState, it)
        }
    }

    override fun onEngineAttach(engine: EngineEcs) {
        super.onEngineAttach(engine)
    }
}