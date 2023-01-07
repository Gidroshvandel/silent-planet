package com.silentgames.core.logic.path

import com.silentgames.core.logic.ecs.Axis

class Node(
    val position: Axis = Axis(0, 0),
    var previous: Node? = null,
    val costPerMoving: Int = 1,
    var cost: Int = Int.MAX_VALUE
) {

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
