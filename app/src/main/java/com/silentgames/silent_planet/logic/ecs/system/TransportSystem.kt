package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class TransportSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        unit.getComponent<Position>()?.let {
            gameState.moveAllUnitsOnTransport(it)
        }
    }

    private fun GameState.moveAllUnitsOnTransport(unitPosition: Position) {
        val allCellUnits = getUnits(unitPosition.currentPosition)
        val transport = allCellUnits.firstOrNull { it.hasComponent<Transport>() }?.getComponent<Transport>()
        if (transport != null) {
            val units = allCellUnits.filterNot { it.hasComponent<Transport>() }
            units.forEach {
                this.moveAllUnitsOnTransport(it, transport)
            }
        }
    }

    private fun GameState.moveAllUnitsOnTransport(unit: Unit, targetTransport: Transport) {
        this.unitMap.remove(unit)
        targetTransport.addOnBoard(unit)
    }

}