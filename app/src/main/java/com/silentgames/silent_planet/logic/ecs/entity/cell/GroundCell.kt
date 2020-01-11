package com.silentgames.silent_planet.logic.ecs.entity.cell

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.*

abstract class GroundCell(
        context: Context,
        position: Position,
        hide: Hide
) : CellEcs(
        position,
        Description(context.getString(R.string.unknown_cell_name), context.getString(R.string.unknown_cell_description)),
        Texture(R.drawable.planet_background)
) {

    init {
        addComponent(MovingMode.WALK)
        addComponent(hide)
    }

}