package com.silentgames.core.logic.ecs.entity.cell.stun

import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.stun.StunComponent
import com.silentgames.core.logic.ecs.component.stun.StunTypeGroup
import com.silentgames.core.logic.ecs.entity.cell.GroundCell

abstract class StunCell(
    position: Position,
    stunTurnsCount: Int,
    hide: Hide,
    stunTypeGroup: StunTypeGroup
) : GroundCell(position, hide) {

    init {
        addComponent(StunComponent(stunTurnsCount, stunTypeGroup))
    }
}
