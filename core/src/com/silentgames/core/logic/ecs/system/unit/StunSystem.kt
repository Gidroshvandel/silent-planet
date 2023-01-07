package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CanMove
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.event.SkipTurnEventComponent
import com.silentgames.core.logic.ecs.component.stun.CanStunnedBy
import com.silentgames.core.logic.ecs.component.stun.StunComponent
import com.silentgames.core.logic.ecs.component.stun.StunEffect
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentUnitCell

class StunSystem : UnitSystem() {

    override fun onEngineAttach(engine: EngineEcs) {
        super.onEngineAttach(engine)
        engine.gameState.unitMap.forEach { unit ->
            unit.addComponentChangedListener<Position> {
                if (unit.getComponent<StunEffect>()?.stunTurnsLeft == 0) {
                    unit.removeComponent(StunEffect::class.java)
                }
            }
        }
        engine.gameState.addNewEventListener {
            val unit = it.getComponent<SkipTurnEventComponent>()?.unitEcs
            val stun = unit?.getComponent<StunEffect>()
            stun?.skipStunTurn()
            if (stun?.canMove() == true) {
                unit.addComponent(CanMove())
            }
        }
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (!unit.hasComponent<StunEffect>()) {
            gameState.getCurrentUnitCell(unit) { cell, position ->
                val stun = cell.getComponent<StunComponent>()
                val canStunnedBy = unit.getComponent<CanStunnedBy>()
                if (stun != null &&
                    canStunnedBy != null &&
                    canStunnedBy.stunTypeGroup == stun.stunTypeGroup
                ) {
                    unit.addComponent(StunEffect(stun))
                    unit.removeComponent(CanMove::class.java)
                }
            }
        }
    }
}
