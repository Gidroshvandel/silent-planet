package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.fractions.FractionsType
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
    private val channel = BroadcastChannel<FractionsType>(1)

    private var turnCount: Int = 0
    lateinit var fractionType: FractionsType

    @ExperimentalCoroutinesApi
    fun turnCount() {
        turnCount++
        nextPlayer()
        channel.sendBlocking(fractionType)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getFlow() = channel.asFlow()

    fun start(fraction: FractionsType) {
        this.fractionType = fraction
    }

    private fun nextPlayer() {
        fractionType = fractionType.next()
    }
}
