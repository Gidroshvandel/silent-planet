package com.silentgames.silent_planet.logic.ecs.entity.unit.ground

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType

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
