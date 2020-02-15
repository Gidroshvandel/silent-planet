package com.silentgames.core.logic.ecs.component.event

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TargetPosition(val axis: Axis, val unit: UnitEcs) : EventComponent()