package com.silentgames.silent_planet.logic.ecs.entity.unit

import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.Entity

class Unit(
        position: Position,
//        targetPosition: TargetPosition,
//        description: Description,
        texture: Texture
) : Entity() {

    init {
        addComponent(position)
//        addComponent(targetPosition)
//        addComponent(description)
        addComponent(texture)
        addComponent(MovingMode.WALK)
    }

}