package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.CrystalBag
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.event.AddCrystalEventComponent
import com.silentgames.core.logic.ecs.entity.EntityEcs
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.system.getName

class AddCrystalSystem : EventSystem() {

    companion object {
        private const val SYSTEM_TAG = "AddCrystalSystem"
    }

    override fun execute(gameState: GameState, eventEcs: EventEcs): Boolean {
        eventEcs.getComponent<AddCrystalEventComponent>()?.let {
            return gameState.processedCrystalAddEvent(it.crystals, it.entityEcs)
        }
        return false
    }

    private fun GameState.processedCrystalAddEvent(crystalCount: Int, entity: EntityEcs): Boolean {
        val position = entity.getComponent<Position>()?.currentPosition
        val unitCrystalBag = entity.getComponent<CrystalBag>()
        if (position != null && unitCrystalBag != null) {
            val crystal = getCell(position)?.getComponent<Crystal>()
            if (crystal != null) {
                unitCrystalBag.addCrystals(crystal, crystalCount)
                CoreLogger.logDebug(SYSTEM_TAG, "unit ${entity.getName()} added addCrystalEvent")
                return true
            }
        }
        return false
    }

}