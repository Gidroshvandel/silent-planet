package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Active
import com.silentgames.silent_planet.logic.ecs.component.FractionsType
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.UnitEcs

fun GameState.choosePlayerToMove(fractionsType: FractionsType): UnitEcs? {
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

private fun Transport.getFirstPlayerFromTransport(fractionsType: FractionsType): UnitEcs? =
        this.unitsOnBoard.firstOrNull { it.getComponent<FractionsType>() == fractionsType }

