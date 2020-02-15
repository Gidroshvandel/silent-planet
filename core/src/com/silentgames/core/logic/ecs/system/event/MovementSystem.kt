package com.silentgames.core.logic.ecs.system.event

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.event.TargetPosition
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getAvailableMoveDistancePositionList
import com.silentgames.core.logic.ecs.system.getName
import com.silentgames.core.utils.notNull

class MovementSystem : EventSystem() {

    companion object {
        private const val SYSTEM_TAG = "MovementSystem"
    }

    override fun execute(gameState: GameState, eventEcs: EventEcs): Boolean {
        eventEcs.getComponent<TargetPosition>()?.let {
            if (process(gameState, it.unit, it.axis)) {
                gameState.moveUnit(it.unit, it.axis)
            } else {
                it.unit.removeComponent(MovedSuccess::class.java)
            }
            return true
        }

        return false
    }

    private fun process(gameState: GameState, unit: UnitEcs, target: Axis): Boolean {
        return if (unit.getComponent<FractionsType>() != gameState.turn.currentTurnFraction
                || !unit.hasComponent<CanTurn>()
                || !unit.hasComponent<Active>()
        ) {
            false
        } else {
            notNull(
                    target,
                    unit.getComponent<Position>()?.currentPosition,
                    unit,
                    gameState,
                    ::tryToMove
            ) ?: false
        }
    }

    private fun tryToMove(
            targetPosition: Axis,
            currentPosition: Axis,
            unit: UnitEcs,
            gameState: GameState
    ): Boolean {
        val isCanMove = isCanMove(targetPosition, currentPosition, unit, gameState)
        if (isCanMove) {
            CoreLogger.logDebug(SYSTEM_TAG, "moved success " + unit.getName())
        } else {
            CoreLogger.logDebug(SYSTEM_TAG, "moved failed " + unit.getName())
        }
        return isCanMove
    }

    fun isCanMove(
            targetPosition: Axis,
            currentPosition: Axis,
            unit: UnitEcs,
            gameState: GameState
    ): Boolean = isMoveAtDistance(targetPosition, currentPosition)
            && (isCanMoveToAllyTransport(unit, gameState.getUnit(targetPosition))
            || isCanMoveToCell(gameState.getCell(targetPosition), unit))

    private fun isCanMoveToCell(targetCell: CellEcs?, unit: UnitEcs) = targetCell != null && canMove(unit.getComponent(), targetCell.getComponent())

    private fun isCanMoveToAllyTransport(unit: UnitEcs, targetUnit: UnitEcs?): Boolean {
        val transport = targetUnit?.getComponent<Transport>()
        return (transport != null
                && unit.getComponent<Transport>() == null
                && isTargetUnitFromAllyFraction(targetUnit.getComponent(), unit.getComponent()))
    }

    private fun isTargetUnitFromAllyFraction(
            targetUnitMovingMode: FractionsType?,
            unitMovingMode: FractionsType?
    ): Boolean = targetUnitMovingMode == unitMovingMode

    private fun canMove(
            unitMovingMode: MovingMode?,
            cellMovingMode: MovingMode?
    ): Boolean = unitMovingMode == cellMovingMode

    private fun isMoveAtDistance(target: Axis, current: Axis): Boolean {
        if (!(current.x == target.x && current.y == target.y)) {
            return getAvailableMoveDistancePositionList(current).contains(target)
        }
        return false
    }

}