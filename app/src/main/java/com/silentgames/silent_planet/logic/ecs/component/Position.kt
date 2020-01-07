package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.model.Axis

class Position(axis: Axis) : ComponentEquals() {

    var moved = false

    var currentPosition: Axis = axis
        set(value) {
            oldPosition = field
            moved = false
            field = value
        }

    var oldPosition: Axis = axis
        private set

}