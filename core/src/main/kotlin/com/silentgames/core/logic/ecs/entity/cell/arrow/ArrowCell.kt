package com.silentgames.core.logic.ecs.entity.cell.arrow


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.cell.GroundCell

/**
 * Created by gidroshvandel on 09.12.16.
 */
abstract class ArrowCell(
        position: Axis,
        rotateAngle: RotateAngle,
        arrowMode: ArrowMode,
        distance: Int,
        imageName: String,
        description: String = ""
) : GroundCell(
        Position(position),
        Hide(
                imageName,
                Description(Strings.arrow_cell_name.getString(), description)
        )
) {

    init {
        addComponent(Arrow(distance, arrowMode, rotateAngle))
    }

}
