package com.silentgames.silent_planet.model.fractions.factionType

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsEnum

/**
 * Created by gidroshvandel on 27.09.16.
 */
class Aliens private constructor() : Fractions() {

    init {
        this.fractionsEnum = FractionsEnum.Aliens
    }

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
