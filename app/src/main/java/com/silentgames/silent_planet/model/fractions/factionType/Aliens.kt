package com.silentgames.silent_planet.model.fractions.factionType

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsType

/**
 * Created by gidroshvandel on 27.09.16.
 */
class Aliens private constructor() : Fractions(FractionsType.ALIEN) {

    companion object {

        private var instance: Aliens? = null

        @Synchronized
        fun getInstance(): Aliens {
            if (instance == null) {
                instance = Aliens()
            }
            return instance!!
        }
    }
}
