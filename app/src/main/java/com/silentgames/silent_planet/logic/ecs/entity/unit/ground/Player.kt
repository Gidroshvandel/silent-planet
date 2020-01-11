package com.silentgames.silent_planet.logic.ecs.entity.unit.ground

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

/**
 * Created by gidroshvandel on 10.07.16.
 */
abstract class Player(
        context: Context,
        position: Axis,
        texture: Texture,
        fractionsType: FractionsType,
        description: Description
) : Unit(
        context,
        Position(position),
        texture,
        MovingMode.WALK,
        fractionsType,
        description
)
