package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Transport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TransportSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.removeUnitsFromTransport()
        unit.getComponent<Position>()?.let {
            gameState.moveAllUnitsOnTransport(it)
        }
    }

    private fun GameState.removeUnitsFromTransport() {
        unitMap.forEach {
            val transport = it.getComponent<Transport>()
            transport?.unitsOnBoard?.toMutableList()?.forEach { unit ->
                if (unitMap.contains(unit)) {
                    transport.removeFromBoard(unit)
                }
            }
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

    private fun GameState.moveAllUnitsOnTransport(unit: UnitEcs, targetTransport: Transport) {
        this.removeUnit(unit)
        targetTransport.addOnBoard(unit)
    }

}