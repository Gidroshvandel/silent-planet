package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.entity.cell.CellEcs

abstract class CellSystem : System {

    abstract fun execute(gameState: GameState, unit: CellEcs)

    override fun execute(gameState: GameState) {
        gameState.cellMap.forEach {
            execute(gameState, it)
        }
    }

    override fun onEngineAttach(engine: EngineEcs) {
        super.onEngineAttach(engine)
    }

}