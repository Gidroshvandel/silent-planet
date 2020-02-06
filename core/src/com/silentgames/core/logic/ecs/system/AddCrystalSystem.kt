package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class AddCrystalSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "AddCrystalSystem"
    }

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
                crystal.getCount(unit.addCrystal(addCrystalEvent))
                CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} added addCrystalEvent")
            }
        }
    }

    private fun UnitEcs.addCrystal(addCrystalEvent: AddCrystalEvent): Int {
        val unitCrystal = getComponent<Crystal>()
        removeComponent(addCrystalEvent)
        return if (unitCrystal == null) {
            val crystal = Crystal()
            addComponent(crystal)
            crystal.addCrystals(addCrystalEvent.crystals)
        } else {
            unitCrystal.addCrystals(addCrystalEvent.crystals)
        }
    }

}