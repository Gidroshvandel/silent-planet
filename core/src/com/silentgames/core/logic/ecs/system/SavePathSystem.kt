package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Route
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class SavePathSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        notNull(unit,
                unit.getComponent<Position>()?.currentPosition,
                ::savePath)
    }

    private fun savePath(unit: UnitEcs, position: Axis) {
        val route = unit.getComponent<Route>()
        if (route != null) {
            if (route.path.isNotEmpty() && route.path.lastOrNull() != position) {
                route.path.add(position)
            }
        } else {
            unit.addComponent(Route(mutableListOf(position)))
        }
    }
}