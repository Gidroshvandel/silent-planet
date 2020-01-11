package com.silentgames.silent_planet.logic.ecs.entity.unit

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.EntityEcs

abstract class UnitEcs(
        val context: Context,
        position: Position,
        texture: Texture,
        movingMode: MovingMode,
        fractionsType: FractionsType,
        description: Description
) : EntityEcs() {

    init {
        addComponent(Active())
        addComponent(position)
        addComponent(texture)
        addComponent(movingMode)
        addComponent(fractionsType)
        addComponent(description)
    }

}