package com.silentgames.core.logic.path

import com.silentgames.core.logic.ecs.Axis

object PathFinderCore {

    fun findPath(
            position: Axis,
            chooseCheckedNode: (reachable: List<Node>) -> Node?,
            condition: (checkedNode: Node) -> Boolean,
            getAdjacentNodes: (checkedNode: Node) -> List<Node>
    ): List<Axis> {
//    println("START(${unit.getComponent<Description>()?.name})-----------------------------")
        val startPosition = Node(position, cost = 0)
        val reachable = mutableListOf(startPosition)
        val explored = mutableListOf<Node>()
        while (reachable.isNotEmpty()) {
//        println("reachable-------------------------------------------")
//            reachable.forEach {
            //            println(it.toString())
//            }
//        println("node-------------------------------------------")
            val node = chooseCheckedNode(reachable) ?: return listOf()
//        println(node.toString())
            if (condition(node)) {
                //            println("SUCCESS-------------------------------------------")
                val finalPath = buildPath(node).toMutableList().apply {
                    remove(startPosition.position)
                }
                //            println("PATH-------------------------------------------")
//                finalPath.forEach {
                //                println(it.toString())
//                }
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
//    println("FAILURE-------------------------------------------")
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

}