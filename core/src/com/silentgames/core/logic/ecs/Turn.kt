package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.component.FractionsType

class Turn(
    private var currentTurnFraction: FractionsType,
    private var turnCount: Int = 0
) {

    val count: Int get() = turnCount

    val currentFraction: FractionsType get() = currentTurnFraction

    fun nextTurn() {
        turnCount++
        nextPlayer()
    }

    private fun nextPlayer() {
        currentTurnFraction = currentFraction.next()
    }
}
