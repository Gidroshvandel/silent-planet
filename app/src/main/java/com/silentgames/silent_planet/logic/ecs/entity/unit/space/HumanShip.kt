package com.silentgames.silent_planet.logic.ecs.entity.unit.space

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.BitmapEditor

class HumanShip(
        context: Context,
        position: Axis
) : SpaceShip(
        context,
        position,
        Texture(BitmapEditor.getEntityBitmap(context, R.drawable.human_space_ship)),
        FractionsType.HUMAN,
        Description(
                context.getString(R.string.human_ship_name),
                context.getString(R.string.human_ship_description)
        )
)