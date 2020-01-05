package com.silentgames.silent_planet.logic.ecs.entity.cell

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 13.07.16.
 */
class EmptyCell(
        context: Context,
        position: Axis
) : Cell(
        Position(position),
        Description(context.getString(R.string.empty_cell_name), context.getString(R.string.empty_cell_description)),
        Texture(BitmapEditor.getCellBitmap(context, R.drawable.empty_cell)),
        Texture(BitmapEditor.getCellBitmap(context, R.drawable.empty_cell))
)
