package com.silentgames.silent_planet.model.entities.space

import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.Fractions

/**
 * Created by gidroshvandel on 07.07.16.
 */
abstract class SpaceShip(
        fraction: Fractions,
        var playersOnBord: MutableList<Player> = mutableListOf(),
        override var isCanFly: Boolean = true,
        override var isCanMove: Boolean = false
) : EntityType(fraction)
