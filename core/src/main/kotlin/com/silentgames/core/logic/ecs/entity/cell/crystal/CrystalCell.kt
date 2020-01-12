package com.silentgames.core.logic.ecs.entity.cell.crystal


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.entity.cell.GroundCell

/**
 * Created by Lantiets on 29.08.2017.
 */

class CrystalCell(

        position: Axis,
        crystalType: CrystalsEnum
) : GroundCell(
        Position(position),
        Hide(
                crystalType.getImageId(),
                Description(
                        Strings.crystal_cell_name.getString(),
                        Strings.crystal_cell_description.getString(crystalType.crystalsCount)
                )
        )
) {
    init {
        addComponent(Crystal(crystalType.crystalsCount))
    }
}
