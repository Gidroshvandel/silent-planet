package com.silentgames.core.logic.path

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getCurrentPosition
import kotlin.math.pow
import kotlin.math.sqrt

fun GameState.findPathToCell(unit: UnitEcs, event: (CellEcs) -> Boolean): List<Axis> {
    val position = unit.getCurrentPosition() ?: return emptyList()
    return PathFinderCore.findPath(
            position,
            { reachable ->
                reachable.minBy { it.cost }
            },
            {
                val cell = this.getCell(it.position)
                cell != null && event(cell)
            },
            {
                this.getAdjacentNodes(it, unit)
            }
    )
}

fun GameState.findPathToGoal(position: Axis, goal: Axis, unit: UnitEcs): List<Axis> {
    return PathFinderCore.findPath(
            position,
            {
                it.chooseNode(Node(goal))
            },
            {
                it.position == goal
            },
            {
                this.getAdjacentNodes(it, unit)
            }
    )
}


// Choose some node we know how to reach.
private fun List<Node>.chooseNode(goalNode: Node): Node {
    var minCost = Int.MAX_VALUE
    var bestNode: Node? = null
    forEach { node ->
        val startCost = node.cost
        val costNodeToGoal = estimateDistance(node, goalNode)
        val totalCoast = startCost + costNodeToGoal
        if (minCost > totalCoast) {
            minCost = totalCoast
            bestNode = node
        }
    }
    return bestNode!!
}

private fun estimateDistance(node: Node, goalNode: Node): Int =
        sqrt((node.position.x - goalNode.position.x).toFloat().pow(2)
                + (node.position.y - goalNode.position.y).toFloat().pow(2)).toInt()