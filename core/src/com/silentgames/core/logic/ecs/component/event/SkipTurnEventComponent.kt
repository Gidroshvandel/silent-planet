package com.silentgames.core.logic.ecs.component.event

import com.silentgames.core.logic.ecs.component.ComponentEquals
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

/**
 * if it has UnitEcs skip its turn; if null skip the turn of the fraction.
 */
class SkipTurnEventComponent(val unitEcs: UnitEcs? = null) : ComponentEquals()