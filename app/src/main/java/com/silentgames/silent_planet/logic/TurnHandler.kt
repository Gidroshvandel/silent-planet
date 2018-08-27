package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsEnum

/**
 * Created by gidroshvandel on 26.09.16.
 */
class TurnHandler {
    companion object {

        private var turnCount: Int = 0
        var fraction: FractionsEnum? = null

        fun turnCount() {
            var turnCount = turnCount
            turnCount = turnCount++
            nextPlayer()
        }

        fun start(fraction: Fractions) {

            this.fraction = fraction.fractionsEnum
        }

        fun setPlayable(fraction: Fractions) {
            fraction.isPlayable = true
        }

        private fun nextPlayer() {
            fraction = fraction!!.next()
        }
    }
}
