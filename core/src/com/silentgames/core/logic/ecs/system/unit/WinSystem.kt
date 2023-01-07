package com.silentgames.core.logic.ecs.system.unit

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.CrystalBag
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class WinSystem(
    private val crystalsToWin: Int,
    private val crystals: (FractionsType, crystals: Int) -> Unit,
    private val onWin: (FractionsType) -> Unit
) : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "WinSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        checkWin(gameState)
    }

    override fun execute(gameState: GameState) {
        checkWin(gameState)
    }

    private fun checkWin(gameState: GameState) {
        FractionsType.values().toList().forEach {
            val crystalCount = gameState.getCapitalShip(it)?.getComponent<CrystalBag>()?.amount ?: 0

            crystals.invoke(it, crystalCount)

            if (crystalCount >= crystalsToWin) {
                onWin.invoke(it)
            }
        }
    }
}
