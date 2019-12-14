package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsType

/**
 * Created by gidroshvandel on 26.09.16.
 */
object TurnHandler {
    private var turnCount: Int = 0
    lateinit var fractionType: FractionsType

    fun turnCount() {
        turnCount++
        nextPlayer()
    }

    fun start(fraction: Fractions) {
        this.fractionType = fraction.fractionsType
    }

    private fun nextPlayer() {
        fractionType = fractionType.next()
    }
}
