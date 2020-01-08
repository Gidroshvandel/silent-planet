package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.fractions.FractionsType

class TurnSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        unit.getComponent<FractionsType>()?.let { nextTurn(it) }
    }

    private fun nextTurn(fractionsType: FractionsType) {
        if (fractionsType == TurnHandler.fractionType) {
            TurnHandler.turnCount()
        }
    }

}