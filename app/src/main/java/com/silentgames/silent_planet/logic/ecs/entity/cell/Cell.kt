package com.silentgames.silent_planet.logic.ecs.entity.cell

import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.Entity

abstract class Cell(
        val position: Position,
        val description: Description,
        val defaultTexture: Texture,
        val exploredTexture: Texture
) : Entity() {

    init {
        components.add(position)
        components.add(description)
        components.add(defaultTexture)
        components.add(exploredTexture)
    }

}