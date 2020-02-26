package com.silentgames.core.logic.path

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis

object PathFinderCore {

    private const val SYSTEM_TAG = "PathFinderCore"

    private var enableLogs = false

    fun enableLogs() {
        enableLogs = true
    }

    fun findPath(
            position: Axis,
            chooseCheckedNode: (reachable: List<Node>) -> Node?,
            condition: (checkedNode: Node) -> Boolean,
            getAdjacentNodes: (checkedNode: Node) -> List<Node>
    ): List<Axis> {
        log("START($position)-----------------------------")
        val startPosition = Node(position, cost = 0)
        val reachable = mutableListOf(startPosition)
        val explored = mutableListOf<Node>()
        while (reachable.isNotEmpty()) {
            log("reachable-------------------------------------------")
            reachable.forEach {
                log(it.toString())
            }
            log("node-------------------------------------------")
            val node = chooseCheckedNode(reachable) ?: return listOf()
            log(node.toString())
            if (condition(node)) {
                log("SUCCESS-------------------------------------------")
                val finalPath = buildPath(node).toMutableList().apply {
                    remove(startPosition.position)
                }
                log("PATH-------------------------------------------")
                finalPath.forEach {
                    log(it.toString())
                }
                return finalPath
            } else {
                reachable.remove(node)
                explored.add(node)

                val newReachable = getAdjacentNodes(node) - explored
                newReachable.forEach { adjacent ->
                    if (adjacent.cost > node.cost + node.costPerMoving) {
                        adjacent.previous = node
                        adjacent.cost = node.cost + node.costPerMoving
                    }
                    if (!reachable.contains(adjacent)) {
                        reachable.add(adjacent)
                    }
                }
            }
        }
        log("FAILURE-------------------------------------------")
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

    private fun log(string: String) {
        if (enableLogs) {
            CoreLogger.logDebug(SYSTEM_TAG, string)
        }
    }

}