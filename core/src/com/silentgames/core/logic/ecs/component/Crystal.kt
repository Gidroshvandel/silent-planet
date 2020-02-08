package com.silentgames.core.logic.ecs.component

class Crystal(count: Int = 0) : ComponentEquals() {

    var count: Int = count
        private set

    fun addCrystals(count: Int) {
        this.count += count
    }

    fun getAll(): Int {
        val getCrystals = count
        count = 0
        return getCrystals
    }

    /**
     * @param count amount of get crystals
     * @return Int amount of taken crystals.
     * example: count = 3, this.count = 1, return 1 and this.count = 0
     * example: count = 3, this.count  = 4, return 3 and this.count = 1
     */
    fun getCount(count: Int): Int {
        val currentBalance = this.count
        val balance = currentBalance - count
        return if (balance >= 0) {
            this.count = balance
            count
        } else {
            this.count = 0
            currentBalance
        }
    }

}