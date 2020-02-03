package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Description
import com.silentgames.core.logic.ecs.component.Transport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class TransportSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        if (unit.hasComponent<Transport>()) {
            unit.removeUnitsFromTransport()
            unit.moveAllUnitsOnTransport(gameState)
        }
    }

    private fun UnitEcs.removeUnitsFromTransport() {
        val transport = getComponent<Transport>()
        transport?.unitsOnBoard?.toMutableList()?.forEach { unit ->
            if (unit.getCurrentPosition() != this.getCurrentPosition()) {
                CoreLogger.logDebug(
                        "TransportSystem",
                        "move from transport ${this.getComponent<Description>()?.name} ${unit.getCurrentPosition()} ${unit.getComponent<Description>()?.name}"
                )
                transport.removeFromBoard(unit)
            }
        }
    }

    private fun UnitEcs.moveAllUnitsOnTransport(gameState: GameState) {
        val transport = getComponent<Transport>()
        val position = getCurrentPosition()
        if (position != null && transport != null) {
            gameState.getUnits(position).forEach { unit ->
                if (unit.getCurrentPosition() == position
                        && !unit.hasComponent<Transport>()
                        && !transport.unitsOnBoard.contains(unit)
                ) {
                    CoreLogger.logDebug(
                            "TransportSystem",
                            "move to transport ${this.getComponent<Description>()?.name} ${unit.getCurrentPosition()} ${unit.getComponent<Description>()?.name}"
                    )
                    transport.addOnBoard(unit)
                }
            }
        }
    }

}