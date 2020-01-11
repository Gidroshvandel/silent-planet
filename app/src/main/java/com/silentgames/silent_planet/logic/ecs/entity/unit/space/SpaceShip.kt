package com.silentgames.silent_planet.logic.ecs.entity.unit.space

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

/**
 * Created by gidroshvandel on 07.07.16.
 */
abstract class SpaceShip(
        context: Context,
        position: Axis,
        texture: Texture,
        fractionsType: FractionsType,
        description: Description
) : Unit(
        context,
        Position(position),
        texture,
        MovingMode.FLY,
        fractionsType,
        description
) {

    init {
        addComponent(CapitalShip())
    }

}
