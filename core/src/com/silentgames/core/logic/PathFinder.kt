package com.silentgames.core.logic

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.AbyssSystem
import com.silentgames.core.logic.ecs.system.MovementSystem
import com.silentgames.core.logic.ecs.system.getAvailableMoveDistancePositionList
import kotlin.math.pow
import kotlin.math.sqrt

fun GameState.findPath(position: Axis, goal: Axis, unit: UnitEcs): List<Axis> {
    println("START(${unit.getComponent<Description>()?.name})-----------------------------")
    val startPosition = Node(position, cost = 0)
    val goalNode = Node(goal)
    val reachable = mutableListOf(startPosition)
    val explored = mutableListOf<Node>()
    while (reachable.isNotEmpty()) {
        println("reachable-------------------------------------------")
        reachable.forEach {
            println(it.toString())
        }
        println("node-------------------------------------------")
        val node = reachable.chooseNode(goalNode)
        println(node.toString())
        if (node != goalNode) {
            reachable.remove(node)
            explored.add(node)

            val newReachable = this.getAdjacentNodes(node, unit) - explored
            newReachable.forEach { adjacent ->
                if (node.cost + 1 < adjacent.cost) {
                    adjacent.previous = node
                    adjacent.cost = node.cost + 1
                }
                if (!reachable.contains(adjacent)) {
                    reachable.add(adjacent)
                }
            }
        } else {
            println("SUCCESS-------------------------------------------")
            val finalPath = buildPath(node).toMutableList().apply {
                remove(startPosition.position)
            }
            println("PATH-------------------------------------------")
            finalPath.forEach {
                println(it.toString())
            }
            return finalPath
        }
    }
    println("FAILURE-------------------------------------------")
    return listOf()
}

private fun buildPath(node: Node?): List<Axis> {
    var goal: Node? = node
    val path = mutableListOf<Axis>()
    while (goal != null) {
        path.add(goal.position)
        goal = goal.previous
    }
    return path
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

// Where can we get from here?
private fun GameState.getAdjacentNodes(node: Node, unit: UnitEcs): List<Node> {
    val cell = this.getCell(node.position)
    val abyss = cell?.getComponent<Abyss>()
    return if (cell != null && !cell.hasComponent<Hide>() && cell.hasComponent<MovementCoordinatesComponent>()) {
        val destination = getDestination(cell)
        if (destination != null) listOf(Node(destination, cost = Int.MAX_VALUE)) else listOf()
    } else if (cell != null && !cell.hasComponent<Hide>() && abyss != null) {
        val destination = getDestination(unit)
        if (destination != null) listOf(Node(destination, cost = Int.MAX_VALUE)) else listOf()
    } else {
        this.getAvailableMoveDistancePositionList(node.position, unit).map { Node(it, cost = Int.MAX_VALUE) }
    }
}

fun getDestination(cell: CellEcs): Axis? {
    return cell.getComponent<MovementCoordinatesComponent>()?.axis ?: return null
}

fun getDestination(unit: UnitEcs): Axis? {
    val paths = unit.getComponent<Route>()?.paths ?: return null
    return AbyssSystem().getTarget(paths)
}

fun GameState.getAvailableMoveDistancePositionList(position: Axis, unit: UnitEcs) =
        getAvailableMoveDistancePositionList(position).filter {
            MovementSystem().isCanMove(it, position, unit, this)
        }

fun GameState.findPathToCell(unit: UnitEcs, event: (CellEcs) -> Boolean): List<Axis> {
    val position = unit.getComponent<Position>()?.currentPosition ?: return emptyList()
    val startPosition = Node(position, cost = 0)
    val reachable = mutableListOf(startPosition)
    val explored = mutableListOf<Node>()
    while (reachable.isNotEmpty()) {
        val node = reachable.minBy { it.cost } ?: return listOf()
        val cell = this.getCell(node.position)
        if (cell != null && event(cell)) {
            val result = buildPath(node).toMutableList()
            result.remove(startPosition.position)
            return result
        } else {
            reachable.remove(node)
            explored.add(node)

            val newReachable = this.getAdjacentNodes(node, unit) - explored
            newReachable.forEach { adjacent ->
                if (node.cost + 1 < adjacent.cost) {
                    adjacent.previous = node
                    adjacent.cost = node.cost + 1
                }
                if (!reachable.contains(adjacent)) {
                    reachable.add(adjacent)
                }
            }
        }
    }
    return listOf()
}

private class Node(val position: Axis = Axis(0, 0), var previous: Node? = null, var cost: Int = 1) {

    override fun equals(other: Any?): Boolean {
        return if (other is Node) {
            other.position == position
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return position.hashCode()
    }

    override fun toString(): String {
        return "Path($position, $previous)"
    }

}