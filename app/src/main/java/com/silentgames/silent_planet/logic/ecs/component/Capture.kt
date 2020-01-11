package com.silentgames.silent_planet.logic.ecs.component

class Capture(
        val invaderFaction: FractionsType,
        val buybackPrice: Int = 1
) : ComponentEquals()