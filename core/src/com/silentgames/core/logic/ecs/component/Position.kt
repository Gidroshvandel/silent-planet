package com.silentgames.core.logic.ecs.component

import com.silentgames.core.logic.ecs.Axis

class Position(axis: Axis) : ComponentEquals() {

    var onPositionChanged: ((Axis) -> Unit)? = null

    var needMovingAnimation = true

    var currentPosition: Axis = axis
        set(value) {
            oldPosition = field
            needMovingAnimation = true
            field = value
            onPositionChanged?.invoke(value)
        }

    var oldPosition: Axis = axis
        private set

}