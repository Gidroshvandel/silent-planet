package com.silentgames.core.logic.ecs.component

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.entity.EntityEcs

class Position(val currentPosition: Axis, val oldPosition: Axis = currentPosition) :
    ComponentEquals()

fun EntityEcs.setCurrentPosition(axis: Axis) {
    val currentPosition = getComponent<Position>()
    if (currentPosition != null) {
        this.addComponent(Position(axis, currentPosition.currentPosition))
    } else {
        this.addComponent(Position(axis, axis))
    }
}

fun EntityEcs.setCurrentPosition(position: Position) {
    this.setCurrentPosition(position.currentPosition)
}
