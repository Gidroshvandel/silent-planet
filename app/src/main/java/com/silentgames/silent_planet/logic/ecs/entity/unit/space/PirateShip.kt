package com.silentgames.silent_planet.logic.ecs.entity.unit.space

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.utils.BitmapEditor

class PirateShip(
        context: Context,
        position: Axis
) : SpaceShip(
        context,
        position,
        Texture(BitmapEditor.getEntityBitmap(context, R.drawable.pirate_space_ship)),
        FractionsType.PIRATE,
        Description(
                context.getString(R.string.pirate_ship_name),
                context.getString(R.string.pirate_ship_description)
        )
)