package com.silentgames.core.logic.ecs.entity.cell

import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Abyss
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position

class AbyssCell(position: Axis, imageName: String = "abyss_cell") : GroundCell(
        Position(position),
        Hide(
                imageName,
                Description(Strings.abyss_cell_name.getString(), Strings.abyss_cell_description.getString())
        )
) {
    init {
        addComponent(Abyss())
    }
}