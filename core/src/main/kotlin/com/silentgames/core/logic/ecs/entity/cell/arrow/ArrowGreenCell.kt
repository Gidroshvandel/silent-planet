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
        rotateAngle: RotateAngle,
        imageName: String = "arrow_green_cell.png"
) : ArrowCell(
        position,
        rotateAngle,
        ArrowMode.SLANTING,
        1,
        imageName,
        Strings.arrow_green_cell_description.getString()
)
