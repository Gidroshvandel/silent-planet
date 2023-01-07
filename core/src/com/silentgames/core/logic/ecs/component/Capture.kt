package com.silentgames.core.logic.ecs.component

class Capture(
    val invaderFaction: FractionsType,
    val buybackPrice: Int = 1
) : ComponentEquals()
