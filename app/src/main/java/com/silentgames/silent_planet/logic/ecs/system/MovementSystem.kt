package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.TargetPositionComponent
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.model.GameMatrix

class MovementSystem : System {

    override fun processEntity(entity: Entity, gameMatrix: GameMatrix) {

    }

    private fun move(
            position: Position,
            targetPositionComponent: TargetPositionComponent
    ) {

    }

}