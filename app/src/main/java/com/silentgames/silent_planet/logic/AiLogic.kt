package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Active
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.fractions.FractionsType

fun GameState.choosePlayerToMove(fractionsType: FractionsType): Unit? {
    val capitalShip = this.getCapitalShip(fractionsType)
    val list = this.unitMap.filter {
        it.getComponent<FractionsType>() == fractionsType
                && !it.hasComponent<Transport>()
                && it.hasComponent<Active>()
    }
    val playerOnGround = list.firstOrNull()
    if (playerOnGround != null) {
        return playerOnGround
    } else {
        capitalShip?.getComponent<Transport>()?.getFirstPlayerFromTransport(fractionsType)?.let {
            return it
        }
    }
    return null
}

private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): Unit? =
        this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

