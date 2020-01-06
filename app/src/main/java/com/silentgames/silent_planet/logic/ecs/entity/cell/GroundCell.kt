package com.silentgames.silent_planet.logic.ecs.entity.cell

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.utils.BitmapEditor

abstract class GroundCell(
        context: Context,
        position: Position,
        description: Description,
        hide: Hide
) : Cell(position, description, Texture(BitmapEditor.getCellBitmap(context, R.drawable.planet_background))) {

    init {
        addComponent(MovingMode.WALK)
        addComponent(hide)
    }

}