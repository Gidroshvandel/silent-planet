package com.silentgames.core.logic.ecs.entity.cell.arrow


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.ArrowMode
import com.silentgames.core.logic.ecs.component.RotateAngle

/**
 * Created by gidroshvandel on 09.12.16.
 */
class ArrowGreenCell(
        position: Axis,
        rotateAngle: RotateAngle
) : ArrowCell(
        position,
        rotateAngle,
        ArrowMode.SLANTING,
        1,
        "arrow_green_cell",
        Strings.arrow_green_cell_description.getString()
)
