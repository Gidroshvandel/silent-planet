package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.Motion
import com.silentgames.core.logic.ecs.MotionType
import com.silentgames.core.logic.ecs.MotionType.MOVEMENT
import com.silentgames.core.logic.ecs.MotionType.TELEPORT
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.Route
import com.silentgames.core.logic.ecs.component.event.TargetPosition
import com.silentgames.core.logic.ecs.component.event.Teleport
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class SavePathSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "SavePathSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        notNull(
                gameState,
                unit,
                unit.getComponent<Position>()?.currentPosition,
                ::savePath
        )
    }

    private fun savePath(gameState: GameState, unit: UnitEcs, axis: Axis) {
        val route = unit.getComponent<Route>()
        if (route != null) {
            if (route.paths.isNotEmpty() && route.paths.last().axis != axis) {
                route.paths.add(Motion(axis, motionType(gameState, unit)))
            }
        } else {
            unit.addComponent(Route(mutableListOf(Motion(axis, motionType(gameState, unit)))))
        }
    }

    private fun motionType(gameState: GameState, unit: UnitEcs): MotionType {
        if (gameState.eventList.find {
                    it.hasComponent<Teleport>()
                            && it.getComponent<TargetPosition>()?.unit == unit
                } != null
        ) {
            return TELEPORT
        } else {
            return MOVEMENT
        }
    }
}