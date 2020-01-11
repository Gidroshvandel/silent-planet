package com.silentgames.silent_planet.logic.ecs.entity.cell

import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.EntityEcs

abstract class CellEcs(
        position: Position,
        description: Description,
        texture: Texture
) : EntityEcs() {

    init {
        addComponent(position)
        addComponent(description)
        addComponent(texture)
    }

}