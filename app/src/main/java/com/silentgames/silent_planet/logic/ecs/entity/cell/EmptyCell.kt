package com.silentgames.silent_planet.logic.ecs.entity.cell

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Hide
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 13.07.16.
 */
class EmptyCell(
        context: Context,
        position: Axis
) : GroundCell(
        context,
        Position(position),
        Hide(
                BitmapEditor.getCellBitmap(context, R.drawable.empty_cell),
                Description(context.getString(R.string.empty_cell_name), context.getString(R.string.empty_cell_description))
        )
)
