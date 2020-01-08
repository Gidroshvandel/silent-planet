package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Crystal
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.fractions.FractionsType

class WinSystem(
        private val crystalsToWin: Int,
        private val crystals: (FractionsType, crystals: Int) -> kotlin.Unit,
        private val onWin: (FractionsType) -> kotlin.Unit
) : System {

    override fun execute(gameState: GameState, unit: Unit) {
        checkWin(gameState)
    }

    override fun execute(gameState: GameState) {
        checkWin(gameState)
    }

    private fun checkWin(gameState: GameState) {
        FractionsType.values().toList().forEach {
            val crystalCount = gameState.getCapitalShip(it)?.getComponent<Crystal>()?.count ?: 0

            crystals.invoke(it, crystalCount)

            if (crystalCount >= crystalsToWin) {
                onWin.invoke(it)
            }
        }
    }

}