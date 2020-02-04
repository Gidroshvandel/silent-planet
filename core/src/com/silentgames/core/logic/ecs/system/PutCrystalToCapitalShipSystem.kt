package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CapitalShip
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class PutCrystalToCapitalShipSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "PutCrystalToCapitalShipSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<Position>()?.let {
            gameState.putCrystalsToCapitalShip(it)
        }
    }

    private fun GameState.putCrystalsToCapitalShip(unitPosition: Position) {
        val units = getUnits(unitPosition.currentPosition)
        val capitalShip = units.find { it.hasComponent<CapitalShip>() }
        if (capitalShip != null) {
            units.forEach {
                val crystals = it.getComponent<Crystal>()
                if (!it.hasComponent<CapitalShip>() && crystals != null) {
                    val capitalShipCrystals = capitalShip.getComponent<Crystal>()
                    if (capitalShipCrystals != null) {
                        capitalShipCrystals.addCrystals(crystals.getAll())
                    } else {
                        capitalShip.addComponent(crystals)
                        it.removeComponent(crystals)
                    }
                    CoreLogger.logDebug(
                            SYSTEM_TAG, "${capitalShip.getName()} ${capitalShip.getCurrentPosition().toString()}"
                    )
                }
            }
        }
    }

}