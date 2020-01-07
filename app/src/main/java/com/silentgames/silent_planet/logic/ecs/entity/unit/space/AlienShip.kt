package com.silentgames.silent_planet.logic.ecs.entity.unit.space

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.BitmapEditor

class AlienShip(
        context: Context,
        position: Axis
) : SpaceShip(
        context,
        position,
        Texture(BitmapEditor.getEntityBitmap(context, R.drawable.aliens_space_ship)),
        FractionsType.ALIEN,
        Description(
                context.getString(R.string.alien_ship_name),
                context.getString(R.string.alien_ship_description)
        )
)