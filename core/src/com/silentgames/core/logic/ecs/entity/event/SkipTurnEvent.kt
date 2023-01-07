package com.silentgames.core.logic.ecs.entity.event

import com.silentgames.core.logic.ecs.component.event.SkipTurnEventComponent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class SkipTurnEvent(unitEcs: UnitEcs? = null) : EventEcs() {
    init {
        addComponent(SkipTurnEventComponent(unitEcs))
    }
}
