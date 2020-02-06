package com.silentgames.core.logic.ecs.component

private const val MAX_CRYSTAL_INFINITY = -1

class Crystal(count: Int = 0, private val maxCrystals: Int = MAX_CRYSTAL_INFINITY) : ComponentEquals() {

    var count: Int = count
        private set

    /**
     * @param count amount of added crystals
     * @return Int amount of added crystals.
     * example: count = 3, maxCrystals = 1, return 1
     * example: count = 1, maxCrystals = 1, return 1
     * @see maxCrystals maximum number of crystals that can be added
     */
    fun addCrystals(count: Int): Int {
        val balance = this.count
        val sum = balance + count
        return when {
            maxCrystals == MAX_CRYSTAL_INFINITY -> {
                this.count = sum
                count
            }
            sum >= maxCrystals -> {
                this.count = maxCrystals
                maxCrystals - balance
            }
            else -> {
                this.count = sum
                count
            }
        }
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

    fun canGetCrystal() = if (maxCrystals == MAX_CRYSTAL_INFINITY) true else maxCrystals > count

}