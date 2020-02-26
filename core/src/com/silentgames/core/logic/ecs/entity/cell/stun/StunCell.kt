package com.silentgames.core.logic.ecs.entity.cell.stun

import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.StunComponent
import com.silentgames.core.logic.ecs.entity.cell.GroundCell

abstract class StunCell(
        position: Position,
        stunTurnsCount: Int,
        hide: Hide
) : GroundCell(position, hide) {

    init {
        addComponent(StunComponent(stunTurnsCount))
    }

}