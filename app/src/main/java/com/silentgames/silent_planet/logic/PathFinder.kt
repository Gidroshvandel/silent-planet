package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.cells.Arrow.Arrow
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.getCell
import kotlin.math.pow
import kotlin.math.sqrt

fun GameMatrix.findPath(position: Axis, goal: Axis, entity: EntityType): List<Axis> {
    println("START(${entity.name})-----------------------------")
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

            val newReachable = this.getAdjacentNodes(node, entity) - explored
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
private fun GameMatrix.getAdjacentNodes(node: Node, entity: EntityType): List<Node> {
    val cell = this.getCell(node.position)
    return if (cell.cellType.isVisible && cell.cellType is Arrow && entity is Player) {
        val destination = cell.cellType.getDestination(this, entity.fraction.fractionsType)
        if (destination != null) listOf(Node(destination, cost = Int.MAX_VALUE)) else listOf()
    } else {
        this.getAvailableMoveDistancePositionList(node.position, entity).map { Node(it, cost = Int.MAX_VALUE) }
    }
}

fun GameMatrix.getAvailableMoveDistancePositionList(position: Axis, entity: EntityType) =
        getAvailableMoveDistancePositionList(position).filter { canMoveEntity(it, entity) }

private class Node(val position: Axis, var previous: Node? = null, var cost: Int = 1) {

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