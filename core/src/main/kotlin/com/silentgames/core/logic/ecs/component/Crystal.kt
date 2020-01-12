package com.silentgames.core.logic.ecs.component

class Crystal(count: Int = 0) : ComponentEquals() {

    var count: Int = count
        private set

    fun addCrystals(count: Int) {
        this.count += count
    }

    fun increment() {
        count++
    }

    fun decrement() {
        count--
        if (count < 0) {
            count = 0
        }
    }

    fun getAll(): Int {
        val getCrystals = count
        count = 0
        return getCrystals
    }

    fun getCount(count: Int): Int {
        this.count = this.count - count
        return count
    }

}