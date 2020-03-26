package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.component.FractionsType

class Turn(
        private var _currentTurnFraction: FractionsType,
        private var _turnCount: Int = 0
) {

    val turnCount: Int get() = _turnCount

    val currentTurnFraction: FractionsType get() = _currentTurnFraction

    fun nextTurn() {
        _turnCount++
        nextPlayer()
    }

    private fun nextPlayer() {
        _currentTurnFraction = currentTurnFraction.next()
    }

}