package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs

class Death(var unit: UnitEcs? = null) : ComponentEquals()