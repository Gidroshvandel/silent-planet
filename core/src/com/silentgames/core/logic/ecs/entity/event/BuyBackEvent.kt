package com.silentgames.core.logic.ecs.entity.event

import com.silentgames.core.logic.ecs.component.event.BuyBackEventComponent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class BuyBackEvent(unitEcs: UnitEcs) : EventEcs() {

    init {
        addComponent(BuyBackEventComponent(unitEcs))
    }
}
