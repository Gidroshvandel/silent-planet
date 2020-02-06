package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Crystal
import com.silentgames.core.logic.ecs.component.LossCrystalComponent
import com.silentgames.core.logic.ecs.component.LossCrystalImmunityComponent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs

class LossCrystalSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            if (!unit.hasComponent<LossCrystalImmunityComponent>() && cell.hasComponent<LossCrystalComponent>()) {
                unit.getComponent<Crystal>()?.getAll()
            }
        }
    }

}