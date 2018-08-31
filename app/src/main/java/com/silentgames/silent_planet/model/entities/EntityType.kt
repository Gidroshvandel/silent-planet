package com.silentgames.silent_planet.model.entities

import com.silentgames.silent_planet.model.fractions.Fractions

/**
 * Created by gidroshvandel on 09.07.16.
 */

abstract class EntityType(
        override var fraction: Fractions,
        override var crystals: Int = 0,
        override var isDead: Boolean = false
) : EntityTypeProperties
