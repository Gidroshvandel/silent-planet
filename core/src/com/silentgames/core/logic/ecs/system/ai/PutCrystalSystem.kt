package com.silentgames.core.logic.ecs.system.ai

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.ArtificialIntelligence
import com.silentgames.core.logic.ecs.entity.event.AddCrystalEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCrystalsCount
import com.silentgames.core.logic.ecs.system.getCurrentUnitCell
import com.silentgames.core.logic.ecs.system.isCrystalBagFull
import com.silentgames.core.logic.ecs.system.unit.UnitSystem

class PutCrystalSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            if (unit.hasComponent<ArtificialIntelligence>() &&
                !unit.isCrystalBagFull() &&
                cell.getCrystalsCount() > 0
            ) {
                gameState.addEvent(AddCrystalEvent(cell.getCrystalsCount(), unit))
            }
        }
    }
}
