package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.cells.Arrow.Arrow
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.getCell

fun GameMatrix.findPath(position: Axis, goal: Axis, entity: EntityType): List<Axis> {
    println("START(${entity.name})-----------------------------")
    val startPosition = Node(position)
    val goalNode = Node(goal)
    val reachable = mutableListOf(startPosition)
    val explored = mutableListOf<Node>()
    while (reachable.isNotEmpty()) {
        val node = reachable.random()
        if (node != goalNode) {
            println(node.toString())
            reachable.remove(node)
            explored.add(node)

            val newReachable = this.getAdjacentNodes(node, entity) - explored
            newReachable.forEach { adjacent ->
                if (!reachable.contains(adjacent)) {
                    adjacent.previous = node
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
private fun List<Node>.chooseAxis(): Node {
    return Node(getAvailableMoveDistancePositionList(this.random().position).random())
}

// Where can we get from here?
private fun GameMatrix.getAdjacentNodes(node: Node, entity: EntityType): List<Node> {
    val cell = this.getCell(node.position)
    return if (cell.cellType.isVisible && cell.cellType is Arrow && entity is Player) {
        val destination = cell.cellType.getDestination(this, entity.fraction.fractionsType)
        if (destination != null) listOf(Node(destination)) else listOf()
    } else {
        this.getAvailableMoveDistancePositionList(node.position, entity).map { Node(it) }
    }
}

fun GameMatrix.getAvailableMoveDistancePositionList(position: Axis, entity: EntityType) =
        getAvailableMoveDistancePositionList(position).filter { canMoveEntity(it, entity) }

private class Node(val position: Axis, var previous: Node? = null) {

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