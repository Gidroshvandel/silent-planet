package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.event.BuyBackEventComponent
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.entity.event.TeleportEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.utils.notNull

class BuyBackSystem(
    private val onSuccess: (name: String) -> Unit,
    private val onFailure: (missingAmount: Int) -> Unit
) : EventSystem() {

    companion object {
        private const val SYSTEM_TAG = "BuyBackSystem"
    }

    override fun execute(gameState: GameState, eventEcs: EventEcs): Boolean {
        eventEcs.getComponent<BuyBackEventComponent>()?.let {
            notNull(
                it.unitEcs.getComponent(),
                it.unitEcs.getComponent(),
                it.unitEcs,
                gameState,
                ::buyBack
            )
            return true
        }
        return false
    }

    private fun buyBack(
        capture: Capture,
        unitFractionsType: FractionsType,
        unit: UnitEcs,
        gameState: GameState
    ) {
        val unitCapitalShip = gameState.getCapitalShip(unitFractionsType)
        val unitFractionCrystals = unitCapitalShip?.getComponent<CrystalBag>()
        val invadersCapitalShip = gameState.getCapitalShip(capture.invaderFaction)
        val invadersFractionCrystals = invadersCapitalShip?.getComponent<CrystalBag>()
        if (invadersFractionCrystals != null &&
            unitFractionCrystals != null &&
            invadersFractionCrystals.addCrystals(unitFractionCrystals, capture.buybackPrice)
        ) {
            unit.removeComponent(capture)
            unit.addComponent(Active())
            unitCapitalShip.getComponent<Position>()?.currentPosition?.let {
                gameState.addEvent(TeleportEvent(it, unit))
            }
            onSuccess.invoke(unit.getComponent<Description>()?.name ?: "")
            CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} buyBack success")
        } else {
            onFailure.invoke(capture.buybackPrice.minus(unitFractionCrystals?.amount ?: 0))
            CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} buyBack failure")
        }
    }
}
