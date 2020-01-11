package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Goal
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.TargetPosition
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.findPath
import com.silentgames.silent_planet.model.Axis

class GoalSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        val goal = unit.getComponent<Goal>()
        val nextAxisToGoal = goal?.axis?.let { gameState.getNextAxisToGoal(unit, goal.axis) }
        if (nextAxisToGoal != null) {
            unit.addComponent(TargetPosition(nextAxisToGoal))
        }
    }

    private fun GameState.getNextAxisToGoal(unit: Unit, goalTarget: Axis): Axis? {
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