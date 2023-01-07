package com.silentgames.core.logic.ecs.entity.cell

import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Death
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position

class DeathCell(
    position: Axis,
    imageName: String = "dead_cell.png"
) : GroundCell(
    Position(position),
    Hide(
        imageName,
        Description(Strings.death_cell_name.getString(), Strings.death_cell_description.getString())
    )
) {
    init {
        addComponent(Death())
    }
}
