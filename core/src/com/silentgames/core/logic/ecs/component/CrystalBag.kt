package com.silentgames.core.logic.ecs.component

private const val MAX_CRYSTALS_INFINITY = -1

class CrystalBag(
    currentCrystals: Int = 0,
    private val maxCrystals: Int = MAX_CRYSTALS_INFINITY
) : ComponentEquals() {

    var amount: Int = currentCrystals
        private set

    val isFull: Boolean get() = this.amount == this.maxCrystals

    /**
     * @param count amount of added crystals
     * @return Int amount of added crystals.
     * example: count = 3, maxCrystals = 1, return 1
     * example: count = 1, maxCrystals = 1, return 1
     * @see maxCrystals maximum number of crystals that can be added
     */
    private fun addCrystals(count: Int): Int {
        val balance = this.amount
        val sum = balance + count
        return when {
            maxCrystals == MAX_CRYSTALS_INFINITY -> {
                this.amount = sum
                count
            }
            sum >= maxCrystals -> {
                this.amount = maxCrystals
                maxCrystals - balance
            }
            else -> {
                this.amount = sum
                count
            }
        }
    }

    fun addCrystals(crystal: Crystal, count: Int): Boolean =
        if (crystal.count >= count) {
            crystal.getCount(addCrystals(count)) > 0
        } else {
            false
        }

    fun addCrystals(crystal: CrystalBag, count: Int): Boolean =
        if (crystal.amount >= count) {
            crystal.getCount(addCrystals(count)) > 0
        } else {
            false
        }

    fun addAllCrystals(crystal: CrystalBag): Boolean =
        crystal.getCount(addCrystals(crystal.amount)) > 0

    fun getAll(): Int {
        val getCrystals = amount
        amount = 0
        return getCrystals
    }

    /**
     * @param count amount of get crystals
     * @return Int amount of taken crystals.
     * example: count = 3, this.count = 1, return 1 and this.count = 0
     * example: count = 3, this.count  = 4, return 3 and this.count = 1
     */
    private fun getCount(count: Int): Int {
        val currentBalance = this.amount
        val balance = currentBalance - count
        return if (balance >= 0) {
            this.amount = balance
            count
        } else {
            this.amount = 0
            currentBalance
        }
    }

    fun canGetCrystal() = if (maxCrystals == MAX_CRYSTALS_INFINITY) true else maxCrystals > amount
}
