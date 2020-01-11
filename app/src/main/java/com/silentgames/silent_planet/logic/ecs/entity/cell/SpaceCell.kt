package com.silentgames.silent_planet.logic.ecs.entity.cell

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture

/**
 * Created by gidroshvandel on 07.07.16.
 */
class SpaceCell(
        context: Context,
        position: Axis
) : CellEcs(
        Position(position),
        Description(context.getString(R.string.space_cell_name), context.getString(R.string.space_cell_description)),
        Texture(R.drawable.space_texture)
) {
    init {
        addComponent(MovingMode.FLY)
    }
}