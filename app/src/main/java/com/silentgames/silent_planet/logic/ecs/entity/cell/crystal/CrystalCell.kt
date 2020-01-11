package com.silentgames.silent_planet.logic.ecs.entity.cell.crystal

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Crystal
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Hide
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.entity.cell.GroundCell

/**
 * Created by Lantiets on 29.08.2017.
 */

class CrystalCell(
        context: Context,
        position: Axis,
        crystalType: CrystalsEnum
) : GroundCell(
        context,
        Position(position),
        Hide(
                crystalType.getImage(context),
                Description(
                        context.getString(R.string.crystal_cell_name),
                        context.getString(R.string.crystal_cell_description, crystalType.crystalsCount)
                )
        )
) {
    init {
        addComponent(Crystal(crystalType.crystalsCount))
    }
}
