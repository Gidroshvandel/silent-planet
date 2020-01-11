package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Crystal
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class AddCrystalSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        unit.getComponent<AddCrystalEvent>()?.let {
            gameState.processedCrystalAddEvent(it, unit)
        }
    }

    private fun GameState.processedCrystalAddEvent(addCrystalEvent: AddCrystalEvent, unit: Unit) {
        val position = unit.getComponent<Position>()?.currentPosition
        if (position != null) {
            val crystal = getCell(position)?.getComponent<Crystal>()
            if (crystal != null && crystal.count > 0) {
                unit.addCrystal(addCrystalEvent)
                crystal.getCount(addCrystalEvent.crystals)
            }
        }
    }

    private fun Unit.addCrystal(addCrystalEvent: AddCrystalEvent) {
        val unitCrystal = getComponent<Crystal>()
        if (unitCrystal == null) {
            addComponent(Crystal(addCrystalEvent.crystals))
        } else {
            unitCrystal.addCrystals(addCrystalEvent.crystals)
        }
        removeComponent(addCrystalEvent)
    }

}