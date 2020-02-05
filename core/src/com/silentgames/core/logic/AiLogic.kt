package com.silentgames.core.logic

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Active
import com.silentgames.core.logic.ecs.component.CanTurn
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.component.Transport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

fun GameState.choosePlayerToMove(fractionsType: FractionsType): UnitEcs? {
    val capitalShip = this.getCapitalShip(fractionsType)
    val list = this.unitMap.filter {
        it.getComponent<FractionsType>() == fractionsType
                && !it.hasComponent<Transport>()
                && it.hasComponent<Active>()
                && it.hasComponent<CanTurn>()
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

