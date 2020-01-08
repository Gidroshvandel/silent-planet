package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.logic.ecs.Engine
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.fractions.FractionsType

class TurnSystem(firstTurnFractionsType: FractionsType) : System {

    init {
        TurnHandler.start(firstTurnFractionsType)
    }

    private var currentFractionsType: FractionsType? = null

    override fun onEngineAttach(engine: Engine) {
        engine.onProcessingChanged = { processing ->
            if (!processing && currentFractionsType == TurnHandler.fractionType) {
                TurnHandler.turnCount()
            }
        }
    }

    override fun execute(gameState: GameState, unit: Unit) {
        unit.getComponent<FractionsType>()?.let { nextTurn(it) }
    }

    private fun nextTurn(fractionsType: FractionsType) {
        currentFractionsType = fractionsType
    }

}