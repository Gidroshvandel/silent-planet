package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class AddCrystalSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        unit.getComponent<AddCrystalEvent>()?.let {
            gameState.processedCrystalAddEvent(it, unit)
        }
    }

    private fun GameState.processedCrystalAddEvent(addCrystalEvent: AddCrystalEvent, unit: UnitEcs) {
        val position = unit.getComponent<Position>()?.currentPosition
        if (position != null) {
            val crystal = getCell(position)?.getComponent<Crystal>()
            if (crystal != null && crystal.count > 0) {
                unit.addCrystal(addCrystalEvent)
                crystal.getCount(addCrystalEvent.crystals)
            }
        }
    }

    private fun UnitEcs.addCrystal(addCrystalEvent: AddCrystalEvent) {
        val unitCrystal = getComponent<Crystal>()
        if (unitCrystal == null) {
            addComponent(Crystal(addCrystalEvent.crystals))
        } else {
            unitCrystal.addCrystals(addCrystalEvent.crystals)
        }
        removeComponent(addCrystalEvent)
    }

}