package com.silentgames.silent_planet.logic.ecs.entity.unit

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.Entity

abstract class Unit(
        val context: Context,
        position: Position,
        texture: Texture,
        movingMode: MovingMode,
        fractionsType: FractionsType,
        description: Description
) : Entity() {

    init {
        addComponent(Active())
        addComponent(position)
        addComponent(texture)
        addComponent(movingMode)
        addComponent(fractionsType)
        addComponent(description)
    }

}