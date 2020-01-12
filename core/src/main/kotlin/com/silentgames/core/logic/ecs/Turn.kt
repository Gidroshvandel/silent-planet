package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.ecs.component.FractionsType

class Turn(firstTurnFraction: FractionsType) {

//    @ExperimentalCoroutinesApi
//    private val channel = BroadcastChannel<FractionsType>(1)

    private var turnCount: Int = 0
    var currentTurnFraction: FractionsType = firstTurnFraction

    //    @ExperimentalCoroutinesApi
    fun turnCount() {
        turnCount++
        nextPlayer()
//        channel.sendBlocking(currentTurnFraction)
    }

//    @ExperimentalCoroutinesApi
//    @FlowPreview
//    fun getFlow() = channel.asFlow()

    private fun nextPlayer() {
        currentTurnFraction = currentTurnFraction.next()
    }

}