package com.silentgames.silent_planet.model.fractions.factionType

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsType

/**
 * Created by gidroshvandel on 27.09.16.
 */
class Robots private constructor() : Fractions(FractionsType.ROBOT) {

    companion object {

        private var instance: Robots? = null

        @Synchronized
        fun getInstance(): Robots {
            if (instance == null) {
                instance = Robots()
            }
            return instance!!
        }
    }
}
