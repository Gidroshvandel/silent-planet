package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Crystal
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class CrystalSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        val addCrystalEvent = unit.getComponent<AddCrystalEvent>()
        if (addCrystalEvent != null) {
            val position = unit.getComponent<Position>()?.currentPosition
            if (position != null) {
                val crystal = gameState.getCell(position)?.getComponent<Crystal>()
                if (crystal != null && crystal.count > 0) {
                    unit.addCrystal(addCrystalEvent)
                    crystal.decrement()
                }
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