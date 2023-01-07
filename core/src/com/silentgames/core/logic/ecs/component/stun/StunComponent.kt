package com.silentgames.core.logic.ecs.component.stun

import com.silentgames.core.logic.ecs.component.ComponentEquals

class StunComponent(val stunTurns: Int, val stunTypeGroup: StunTypeGroup) : ComponentEquals()
