package com.silentgames.core.logic.ecs.component

import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class Death(var unit: UnitEcs? = null) : ComponentEquals()
