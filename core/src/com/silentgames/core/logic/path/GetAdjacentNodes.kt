package com.silentgames.core.logic.path

import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.Abyss
import com.silentgames.core.logic.ecs.component.Hide
import com.silentgames.core.logic.ecs.component.MovementCoordinatesComponent
import com.silentgames.core.logic.ecs.component.stun.StunComponent
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.event.MovementSystem
import com.silentgames.core.logic.ecs.system.getCurrentPosition

/**
 * Check where can we get from this node
 */
fun GameState.getAdjacentNodes(node: Node, unit: UnitEcs): List<Node> =
    this.getCell(node.position)?.getVisibleDestinationNodes()
        ?: getAvailableNodePositionList(node, unit)

private fun GameState.getAvailableNodePositionList(node: Node, unit: UnitEcs) =
    this.getAvailableMoveDistancePositionList(node.position, unit)
        .map { Node(it, cost = Int.MAX_VALUE) }

private fun CellEcs.getVisibleDestinationNodes(): List<Node>? =
    if (!this.hasComponent<Hide>()) {
        this.getDestinationNodes()
    } else {
        null
    }

private fun GameState.getAvailableMoveDistancePositionList(position: Axis, unit: UnitEcs) =
    com.silentgames.core.logic.ecs.system.getAvailableMoveDistancePositionList(position).filter {
        MovementSystem().isCanMove(it, position, unit, this)
    }

/**
 * Cells moving logic
 */
private fun CellEcs.getDestinationNodes(): List<Node>? {
    val abyss = this.getComponent<Abyss>()
    val movementCoordinates = this.getComponent<MovementCoordinatesComponent>()
    val stun = this.getComponent<StunComponent>()
    return when {
        movementCoordinates != null -> listOf(Node(movementCoordinates.axis))
        abyss != null -> listOf()
        stun != null -> listOf(Node(this.getCurrentPosition(), costPerMoving = stun.stunTurns))
        else -> null
    }
}
