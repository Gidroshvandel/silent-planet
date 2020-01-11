package com.silentgames.silent_planet.logic.ecs.entity.cell

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Death
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Hide
import com.silentgames.silent_planet.logic.ecs.component.Position

class DeathCell(
        context: Context,
        position: Axis
) : GroundCell(
        context,
        Position(position),
        Hide(
                R.drawable.dead_cell,
                Description(context.getString(R.string.death_cell_name), context.getString(R.string.death_cell_description))
        )
) {
    init {
        addComponent(Death())
    }

}
