package com.silentgames.core.logic.ecs.entity.cell.stun.swell

import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.stun.StunTypeGroup
import com.silentgames.core.logic.ecs.entity.cell.stun.StunCell

class SwellCell(
    position: Axis,
    stunTurnsCount: Int,
    imageName: String
) : StunCell(
    Position(position),
    stunTurnsCount,
    Hide(
        imageName,
        Description(
            Strings.empty_cell_name.getString(),
            Strings.empty_cell_description.getString()
        )
    ),
    StunTypeGroup.SWELL
) {

    constructor(
        position: Axis,
        swellsEnum: SwellsEnum
    ) : this(position, swellsEnum.turnCount, swellsEnum.getImageName())
}
