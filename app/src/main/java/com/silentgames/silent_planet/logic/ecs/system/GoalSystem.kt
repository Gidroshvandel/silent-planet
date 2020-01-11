package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Crystal
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

    private fun GameState.isGoalActual(goalTarget: Axis): Boolean {
        val cell = getCell(goalTarget)
        return (cell != null && cell.isVisible() && cell.getComponent<Crystal>()?.count ?: 0 > 0
                || cell != null && cell.isHide())
    }

    private fun GameState.getNextAxisToGoal(unit: Unit, goalTarget: Axis): Axis? {
        if (isGoalActual(goalTarget)) {
            val position = unit.getComponent<Position>()?.currentPosition ?: return null
            val path = this.findPath(position, goalTarget, unit)
            if (path.isNotEmpty()) {
                if (path.last() == goalTarget) {
                    unit.removeComponent(Goal::class.java)
                }
                return path.last()
            }
        } else {
            unit.removeComponent(Goal::class.java)
        }
        return null
    }

}