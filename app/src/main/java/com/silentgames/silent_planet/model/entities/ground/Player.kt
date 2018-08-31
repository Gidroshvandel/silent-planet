package com.silentgames.silent_planet.model.entities.ground

import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.fractions.Fractions

/**
 * Created by gidroshvandel on 10.07.16.
 */
abstract class Player(
        var playerName: String,
        fraction: Fractions,
        override var isCanMove: Boolean = true,
        override var isCanFly: Boolean = false
) : EntityType(fraction)
