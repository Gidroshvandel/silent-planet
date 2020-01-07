package com.silentgames.silent_planet.logic.ecs.component

class Crystal(count: Int = 0) : ComponentEquals() {

    var count: Int = count
        private set

    fun increment() {
        count++
    }

    fun decrement() {
        count--
        if (count < 0) {
            count = 0
        }
    }

}