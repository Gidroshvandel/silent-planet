package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.fractions.Fractions
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.model.fractions.FractionsType.*
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.asFlow

/**
 * Created by gidroshvandel on 26.09.16.
 */
object TurnHandler {

    @ExperimentalCoroutinesApi
    private val channel = BroadcastChannel<Fractions>(1)

    private var turnCount: Int = 0
    lateinit var fractionType: FractionsType

    @ExperimentalCoroutinesApi
    fun turnCount() {
        turnCount++
        nextPlayer()
        channel.sendBlocking(getCurrentFraction())
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getFlow() = channel.asFlow()

    fun start(fraction: Fractions) {
        this.fractionType = fraction.fractionsType
    }

    fun getCurrentFraction(): Fractions =
            when (fractionType) {
                ALIEN -> Aliens
                HUMAN -> Humans
                PIRATE -> Pirates
                ROBOT -> Robots
            }

    private fun nextPlayer() {
        fractionType = fractionType.next()
    }
}
