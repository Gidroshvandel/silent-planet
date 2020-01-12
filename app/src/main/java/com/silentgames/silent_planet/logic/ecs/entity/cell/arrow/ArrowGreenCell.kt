package com.silentgames.silent_planet.logic.ecs.entity.cell.arrow

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.ArrowMode
import com.silentgames.silent_planet.logic.ecs.component.RotateAngle

/**
 * Created by gidroshvandel on 09.12.16.
 */
class ArrowGreenCell(
        context: Context,
        position: Axis,
        rotateAngle: RotateAngle
) : ArrowCell(
        context,
        position,
        rotateAngle,
        ArrowMode.SLANTING,
        1,
        R.drawable.arrow_green_cell,
        context.getString(R.string.arrow_green_cell_description)
)
