package com.silentgames.core.logic.ecs.entity.event

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.event.TargetPosition
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class MovementEvent(axis: Axis, unitEcs: UnitEcs) : EventEcs() {

    init {
        addComponent(TargetPosition(axis, unitEcs))
    }
}
