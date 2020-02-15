package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Goal
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.entity.event.MovementEvent
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.findPath

class GoalSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "GoalSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        val goal = unit.getComponent<Goal>()
        val nextAxisToGoal = goal?.axis?.let { gameState.getNextAxisToGoal(unit, goal.axis) }
        if (nextAxisToGoal != null) {
            CoreLogger.logDebug(SYSTEM_TAG, "unit ${unit.getName()} added goal $nextAxisToGoal")
            gameState.addEvent(MovementEvent(nextAxisToGoal, unit))
        }
    }

    private fun GameState.getNextAxisToGoal(unit: UnitEcs, goalTarget: Axis): Axis? {
        val position = unit.getComponent<Position>()?.currentPosition ?: return null
        val path = this.findPath(position, goalTarget, unit)
        if (path.isNotEmpty()) {
            if (path.last() == goalTarget) {
                unit.removeComponent(Goal::class.java)
            }
            return path.last()
        }
        return null
    }

}