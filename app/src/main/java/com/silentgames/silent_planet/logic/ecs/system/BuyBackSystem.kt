package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.component.event.BuyBackEvent
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.notNull

class BuyBackSystem(
        private val onSuccess: (name: String) -> kotlin.Unit,
        private val onFailure: (missingAmount: Int) -> kotlin.Unit
) : System {

    override fun execute(gameState: GameState, unit: Unit) {
        unit.getComponent<BuyBackEvent>()?.let {
            notNull(
                    unit.getComponent(),
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::buyBack
            )
            unit.removeComponent(it)
        }
    }

    private fun buyBack(
            capture: Capture,
            unitFractionsType: FractionsType,
            unit: Unit,
            gameState: GameState
    ) {
        val unitCapitalShip = gameState.getCapitalShip(unitFractionsType)
        val unitFractionCrystals = unitCapitalShip?.getComponent<Crystal>()
        val invadersCapitalShip = gameState.getCapitalShip(capture.invaderFaction)
        if (invadersCapitalShip != null
                && unitFractionCrystals != null
                && unitFractionCrystals.count >= capture.buybackPrice) {

            val invadersFractionCrystals = invadersCapitalShip.getComponent<Crystal>()

            val buyBackCount = unitFractionCrystals.getCount(capture.buybackPrice)
            if (invadersFractionCrystals == null) {
                invadersCapitalShip.addComponent(Crystal(buyBackCount))
            } else {
                invadersFractionCrystals.addCrystals(buyBackCount)
            }
            unit.removeComponent(capture)
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(unitCapitalShip.getComponent<Position>()!!.currentPosition))
            onSuccess.invoke(unit.getComponent<Description>()?.name ?: "")
        } else {
            onFailure.invoke(capture.buybackPrice.minus(unitFractionCrystals?.count ?: 0))
        }
    }

}