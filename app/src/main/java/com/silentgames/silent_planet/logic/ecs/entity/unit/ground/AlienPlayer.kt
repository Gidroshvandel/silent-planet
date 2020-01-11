package com.silentgames.silent_planet.logic.ecs.entity.unit.ground

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.Texture

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
        Texture(R.drawable.alien),
        FractionsType.ALIEN,
        Description(
                name,
                context.getString(R.string.alien_player_description)
        )
)
