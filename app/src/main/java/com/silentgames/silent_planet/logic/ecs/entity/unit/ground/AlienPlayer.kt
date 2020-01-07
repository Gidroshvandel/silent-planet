package com.silentgames.silent_planet.logic.ecs.entity.unit.ground

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class AlienPlayer(
        context: Context,
        name: String,
        position: Axis
) : Player(
        context,
        position,
        Texture(BitmapEditor.getEntityBitmap(context, R.drawable.alien)),
        FractionsType.ALIEN,
        Description(
                name,
                context.getString(R.string.alien_player_description)
        )
)
