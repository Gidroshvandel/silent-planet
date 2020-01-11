package com.silentgames.silent_planet.logic.ecs

import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.asFlow

class Turn(firstTurnFraction: FractionsType) {

    @ExperimentalCoroutinesApi
    private val channel = BroadcastChannel<FractionsType>(1)

    private var turnCount: Int = 0
    var currentTurnFraction: FractionsType = firstTurnFraction

    @ExperimentalCoroutinesApi
    fun turnCount() {
        turnCount++
        nextPlayer()
        channel.sendBlocking(currentTurnFraction)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getFlow() = channel.asFlow()

    private fun nextPlayer() {
        currentTurnFraction = currentTurnFraction.next()
    }

}