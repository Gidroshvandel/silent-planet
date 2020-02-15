package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Moving
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class MovingSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.removeComponent(Moving::class.java)
    }

    override fun onEngineAttach(engine: EngineEcs) {
        super.onEngineAttach(engine)
        engine.gameState.unitMap.forEach { unit ->
            unit.getComponent<Position>()?.addPositionChangedListener {
                unit.addComponent(Moving())
            }
        }
    }
}