package com.silentgames.core.logic.ecs.entity.cell

import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.*

class TornadoCell(
        position: Axis,
        imageName: String = "tornado_cell.png"
) : GroundCell(
        Position(position),
        Hide(
                imageName,
                Description(Strings.tornado_cell_name.getString(), Strings.tornado_cell_description.getString())
        )
) {
    init {
        addComponent(Tornado())
        addComponent(LossCrystalComponent())
    }
}