package com.silentgames.silent_planet.model.effects

import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.FractionsType

class CaptureEffect(
        val player: Player,
        val invaderFaction: FractionsType,
        val buybackPrice: Int = 1
) : Effect {

    init {
        player.isCanMove = false
        player.addEffect(this)
    }

    override fun remove() {
        player.isCanMove = true
        player.removeEffect(this)
    }
}