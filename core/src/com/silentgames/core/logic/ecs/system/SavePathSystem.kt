package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.MotionType
import com.silentgames.core.logic.ecs.MotionType.MOVEMENT
import com.silentgames.core.logic.ecs.MotionType.TELEPORT
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Route
import com.silentgames.core.logic.ecs.component.Teleport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class SavePathSystem : UnitSystem() {
    override fun execute(gameState: GameState, unit: UnitEcs) {
        notNull(unit,
                unit.getComponent<Position>()?.currentPosition,
                ::savePath)
    }

    private fun savePath(unit: UnitEcs, axis: Axis) {
        val route = unit.getComponent<Route>()
        if (route != null) {
            if (route.paths.isNotEmpty() && route.paths.last().axis != axis) {
                route.paths.add(Motion(axis, motionType(unit)))
            }
        } else {
            unit.addComponent(Route(mutableListOf(Motion(axis, motionType(unit)))))
        }
    }

    private fun motionType(unit: UnitEcs): MotionType {
        return if (unit.getComponent<Teleport>() != null) TELEPORT else MOVEMENT
    }
}