package com.silentgames.core.logic.ecs.entity.event

import com.silentgames.core.logic.ecs.component.event.AddCrystalEventComponent
import com.silentgames.core.logic.ecs.entity.EntityEcs

class AddCrystalEvent(crystals: Int = 1, entityEcs: EntityEcs) : EventEcs() {

    init {
        addComponent(AddCrystalEventComponent(crystals, entityEcs))
    }
}
