package com.silentgames.silent_planet.logic.ecs.entity.unit

import android.content.Context
import com.silentgames.silent_planet.logic.ecs.component.Description
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Texture
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.model.fractions.FractionsType

class Unit(
        val context: Context,
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
        addComponent(MovingMode.FLY)
        addComponent(FractionsType.ALIEN)
        addComponent(Description("Первопроходец", "Что-то тут есть"))
    }

}