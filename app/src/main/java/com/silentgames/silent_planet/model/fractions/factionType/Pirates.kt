package com.silentgames.silent_planet.model.fractions.factionType

import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate
import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsEnum

/**
 * Created by gidroshvandel on 27.09.16.
 */
class Pirates private constructor() : Fractions() {

    init {
        this.fractionsEnum = FractionsEnum.Pirates
    }

    companion object {

        private var instance: Pirates? = null

        @Synchronized
        fun getInstance(): Pirates {
            if (instance == null) {
                instance = Pirates()
            }
            return instance!!
        }
    }
}
