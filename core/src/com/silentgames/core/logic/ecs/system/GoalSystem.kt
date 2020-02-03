package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Goal
import com.silentgames.core.logic.ecs.component.Position
import com.silentgames.core.logic.ecs.component.TargetPosition
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.findPath

class GoalSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        val goal = unit.getComponent<Goal>()
        val nextAxisToGoal = goal?.axis?.let { gameState.getNextAxisToGoal(unit, goal.axis) }
        if (nextAxisToGoal != null) {
            CoreLogger.logDebug(
                    this::class.simpleName ?: "",
                    "target $nextAxisToGoal"
            )
            unit.addComponent(TargetPosition(nextAxisToGoal))
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