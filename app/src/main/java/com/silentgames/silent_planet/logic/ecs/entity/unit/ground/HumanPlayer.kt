package com.silentgames.silent_planet.logic.ecs.entity.unit.ground

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 24.09.16.
 */
class HumanPlayer(
        context: Context,
        name: String,
        position: Axis
) : Player(
        context,
        position,
        Texture(BitmapEditor.getEntityBitmap(context, R.drawable.human_spaceman)),
        FractionsType.HUMAN,
        Description(
                name,
                context.getString(R.string.human_player_description)
        )
)
