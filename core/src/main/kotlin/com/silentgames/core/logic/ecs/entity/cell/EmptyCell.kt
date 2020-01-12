package com.silentgames.core.logic.ecs.entity.cell


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position

/**
 * Created by gidroshvandel on 13.07.16.
 */
class EmptyCell(

        position: Axis
) : GroundCell(

        Position(position),
        Hide(
                "empty_cell",
                Description(Strings.empty_cell_name.getString(), Strings.empty_cell_description.getString())
        )
)
