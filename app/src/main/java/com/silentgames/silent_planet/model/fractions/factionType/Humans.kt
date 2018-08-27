package com.silentgames.silent_planet.model.fractions.factionType

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsEnum

/**
 * Created by gidroshvandel on 27.09.16.
 */
class Humans private constructor() : Fractions() {

    init {
        this.fractionsEnum = FractionsEnum.Humans
    }

    companion object {

        private var instance: Humans? = null

        @Synchronized
        fun getInstance(): Humans {
            if (instance == null) {
                instance = Humans()
            }
            return instance!!
        }
    }
}
