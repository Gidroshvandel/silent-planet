package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsType

/**
 * Created by gidroshvandel on 26.09.16.
 */
class TurnHandler {
    companion object {

        private var turnCount: Int = 0
        lateinit var fractionType: FractionsType

        fun turnCount() {
            var turnCount = turnCount
            turnCount = turnCount++
            nextPlayer()
        }

        fun start(fraction: Fractions) {
            this.fractionType = fraction.fractionsType
        }

        fun setPlayable(fraction: Fractions) {
            fraction.isPlayable = true
        }

        private fun nextPlayer() {
            fractionType = fractionType.next()
        }
    }
}
