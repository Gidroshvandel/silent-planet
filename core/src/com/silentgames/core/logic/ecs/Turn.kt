package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.component.FractionsType

class Turn(firstTurnFraction: FractionsType) {

    private var turnCount: Int = 0

    var currentTurnFraction: FractionsType = firstTurnFraction
        private set

    fun nextTurn() {
        turnCount++
        nextPlayer()
    }

    private fun nextPlayer() {
        currentTurnFraction = currentTurnFraction.next()
    }

}