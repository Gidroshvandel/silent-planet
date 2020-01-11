package com.silentgames.silent_planet.logic.ecs.entity.cell.arrow

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.ArrowMode
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 28.08.2017.
 */

class ArrowRedCell(
        context: Context,
        position: Axis,
        rotateAngle: BitmapEditor.RotateAngle
) : ArrowCell(
        context,
        position,
        rotateAngle,
        ArrowMode.DIRECT,
        3,
        R.drawable.arow_red_cell,
        context.getString(R.string.arrow_red_cell_description)
)
