package com.silentgames.silent_planet.logic.ecs.entity.cell

import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture

abstract class GroundCell(
        position: Position,
        description: Description,
        texture: Texture
) : Cell(position, description, texture) {

    init {
        addComponent(MovingMode.WALK)
    }

}