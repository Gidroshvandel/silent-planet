package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.model.fractions.FractionsType

class Capture(
        val invaderFaction: FractionsType,
        val buybackPrice: Int = 1
) : ComponentEquals()