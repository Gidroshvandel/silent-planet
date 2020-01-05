package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.model.GameMatrix

interface System {
    fun processEntity(entity: Entity, gameMatrix: GameMatrix)
}