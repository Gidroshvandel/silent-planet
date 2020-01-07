package com.silentgames.silent_planet.logic.ecs.entity.cell

import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.Entity

abstract class Cell(
        position: Position,
        description: Description,
        texture: Texture
) : Entity() {

    init {
        addComponent(position)
        addComponent(description)
        addComponent(texture)
    }

}