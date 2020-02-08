package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CapitalShip
import com.silentgames.core.logic.ecs.component.CrystalBag
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
        if (capitalShip != null && capitalShip.getCurrentPosition() == unit.getCurrentPosition()) {
            val crystalBag = unit.getComponent<CrystalBag>()
            if (!unit.hasComponent<CapitalShip>()
                    && crystalBag != null
                    && capitalShip.getComponent<CrystalBag>()?.addAllCrystals(crystalBag) == true) {
                CoreLogger.logDebug(
                        SYSTEM_TAG,
                        "unit ${unit.getName()} put crystals to ${capitalShip.getName()} ${capitalShip.getCurrentPosition().toString()}"
                )
            }
        }
    }

}