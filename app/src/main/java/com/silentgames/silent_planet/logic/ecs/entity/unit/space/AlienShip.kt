package com.silentgames.silent_planet.logic.ecs.entity.unit.space

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.Texture

class AlienShip(
        context: Context,
        position: Axis
) : SpaceShip(
        context,
        position,
        Texture(R.drawable.aliens_space_ship),
        FractionsType.ALIEN,
        Description(
                context.getString(R.string.alien_ship_name),
                context.getString(R.string.alien_ship_description)
        )
)