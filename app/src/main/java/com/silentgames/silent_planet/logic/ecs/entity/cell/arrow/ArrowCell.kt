package com.silentgames.silent_planet.logic.ecs.entity.cell.arrow

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.cell.GroundCell
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
abstract class ArrowCell(
        context: Context,
        position: Axis,
        rotateAngle: BitmapEditor.RotateAngle,
        arrowMode: ArrowMode,
        distance: Int,
        texture: Bitmap,
        description: String = ""
) : GroundCell(
        context,
        Position(position),
        Hide(
                texture,
                Description(context.getString(R.string.arrow_cell_name), description)
        )
) {

    init {
        addComponent(Arrow(distance, arrowMode, rotateAngle))
    }

}
