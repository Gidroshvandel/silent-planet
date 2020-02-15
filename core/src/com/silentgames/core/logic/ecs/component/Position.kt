package com.silentgames.core.logic.ecs.component

import com.silentgames.core.logic.ecs.Axis

class Position(axis: Axis) : ComponentEquals() {

    private val onPositionChangedList = mutableListOf<((Axis) -> Unit)>()

    fun addPositionChangedListener(onChanged: (Axis) -> Unit) {
        onPositionChangedList.add(onChanged)
    }

    var currentPosition: Axis = axis
        set(value) {
            oldPosition = field
            field = value
            onPositionChangedList.forEach {
                it.invoke(value)
            }
        }

    var oldPosition: Axis = axis
        private set

}