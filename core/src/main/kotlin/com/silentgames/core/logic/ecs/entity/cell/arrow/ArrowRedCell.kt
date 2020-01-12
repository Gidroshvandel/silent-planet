package com.silentgames.core.logic.ecs.entity.cell.arrow


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.ArrowMode
import com.silentgames.core.logic.ecs.component.RotateAngle

/**
 * Created by Lantiets on 28.08.2017.
 */

class ArrowRedCell(
        position: Axis,
        rotateAngle: RotateAngle
) : ArrowCell(
        position,
        rotateAngle,
        ArrowMode.DIRECT,
        3,
        "arow_red_cell",
        Strings.arrow_red_cell_description.getString()
)
