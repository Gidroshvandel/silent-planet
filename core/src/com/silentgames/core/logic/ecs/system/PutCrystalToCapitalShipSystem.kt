package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CapitalShip
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class PutCrystalToCapitalShipSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "PutCrystalToCapitalShipSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        notNull(
                gameState,
                unit,
                unit.getComponent(),
                ::putCrystalsToCapitalShip
        )
    }

    private fun putCrystalsToCapitalShip(
            gameState: GameState,
            unit: UnitEcs,
            fractionsType: FractionsType
    ) {
        val capitalShip = gameState.getCapitalShip(fractionsType)
        if (capitalShip != null) {
            val crystals = unit.getComponent<Crystal>()
            if (!unit.hasComponent<CapitalShip>() && crystals != null) {
                val capitalShipCrystals = capitalShip.getComponent<Crystal>()
                if (capitalShipCrystals != null) {
                    capitalShipCrystals.addCrystals(crystals.getAll())
                } else {
                    capitalShip.addComponent(crystals)
                    unit.removeComponent(crystals)
                }
                CoreLogger.logDebug(
                        SYSTEM_TAG,
                        "unit ${unit.getName()} put crystals to ${capitalShip.getName()} ${capitalShip.getCurrentPosition().toString()}"
                )
            }
        }
    }

}