package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.CapitalShip
import com.silentgames.silent_planet.logic.ecs.component.Crystal
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class CrystalSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        unit.getComponent<AddCrystalEvent>()?.let {
            gameState.processedCrystalAddEvent(it, unit)
        }
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
                }
            }
        }
    }

    private fun GameState.processedCrystalAddEvent(addCrystalEvent: AddCrystalEvent, unit: Unit) {
            val position = unit.getComponent<Position>()?.currentPosition
            if (position != null) {
                val crystal = getCell(position)?.getComponent<Crystal>()
                if (crystal != null && crystal.count > 0) {
                    unit.addCrystal(addCrystalEvent)
                    crystal.decrement()
                }
            }
    }

    private fun Unit.addCrystal(addCrystalEvent: AddCrystalEvent) {
        val unitCrystal = getComponent<Crystal>()
        if (unitCrystal == null) {
            addComponent(Crystal(1))
        } else {
            unitCrystal.increment()
        }
        removeComponent(addCrystalEvent)
    }

}