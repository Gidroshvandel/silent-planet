package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.EngineEcs
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Moving
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Transport
import com.silentgames.core.logic.ecs.component.setCurrentPosition
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentPosition
import com.silentgames.core.logic.ecs.system.getName

class TransportSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "TransportSystem"
    }

    override fun onEngineAttach(engine: EngineEcs) {
        engine.gameState.unitMap.forEach { unit ->
            val transport = unit.getComponent<Transport>()
            if (transport != null) {
                unit.addComponentChangedListener<Position> { axis ->
                    transport.unitsOnBoard.forEach {
                        it.setCurrentPosition(axis)
                        it.removeComponent(Moving::class.java)
                    }
                }
            }
        }
        super.onEngineAttach(engine)
    }

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
                        SYSTEM_TAG,
                        "move from transport ${this.getName()} ${unit.getCurrentPosition()} ${unit.getName()}"
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
                            SYSTEM_TAG,
                            "move to transport ${this.getName()} ${unit.getCurrentPosition()} ${unit.getName()}"
                    )
                    transport.addOnBoard(unit)
                }
            }
        }
    }

}