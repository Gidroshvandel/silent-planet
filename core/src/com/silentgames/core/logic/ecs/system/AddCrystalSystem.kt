package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.CrystalBag
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
        val unitCrystalBag = unit.getComponent<CrystalBag>()
        if (position != null && unitCrystalBag != null) {
            val crystal = getCell(position)?.getComponent<Crystal>()
            if (crystal != null) {
                unitCrystalBag.addCrystals(crystal, addCrystalEvent.crystals)
                unit.removeComponent(addCrystalEvent)
                CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} added addCrystalEvent")
            }
        }
    }

}