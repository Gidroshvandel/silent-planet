package com.silentgames.core.logic.ecs.component.event

import com.silentgames.core.logic.ecs.entity.EntityEcs

class AddCrystalEventComponent(val crystals: Int = 1, val entityEcs: EntityEcs) : EventComponent()
