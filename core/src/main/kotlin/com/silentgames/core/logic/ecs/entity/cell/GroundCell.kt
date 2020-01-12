package com.silentgames.core.logic.ecs.entity.cell


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.component.*

abstract class GroundCell(
        position: Position,
        hide: Hide
) : CellEcs(
        position,
        Description(Strings.unknown_cell_name.getString(), Strings.unknown_cell_description.getString()),
        Texture("planet_background")
) {

    init {
        addComponent(MovingMode.WALK)
        addComponent(hide)
    }

}