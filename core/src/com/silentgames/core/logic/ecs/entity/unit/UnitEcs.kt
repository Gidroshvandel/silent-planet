package com.silentgames.core.logic.ecs.entity.unit

import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.EntityEcs

abstract class UnitEcs(
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
        addComponent(CanMove())
    }
}
