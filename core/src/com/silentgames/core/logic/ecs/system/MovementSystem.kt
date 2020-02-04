package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class MovementSystem : UnitSystem() {

    companion object {
        private const val SYSTEM_TAG = "MovementSystem"
    }

    override fun execute(gameState: GameState, unit: UnitEcs) {
        val moveSuccess = if (unit.hasComponent<Teleport>()
                || unit.getComponent<FractionsType>() != gameState.turn.currentTurnFraction
                || !unit.hasComponent<TurnToMove>()
                || !unit.hasComponent<Active>()
        ) {
            false
        } else {
            notNull(
                    unit.getComponent<TargetPosition>()?.axis,
                    unit.getComponent<Position>()?.currentPosition,
                    unit,
                    gameState,
                    ::tryToMove
            ) ?: false
        }
        if (moveSuccess) {
            unit.addComponent(MovedSuccess())
            notNull(
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::move
            )
        } else {
            unit.removeComponent(MovedSuccess::class.java)
        }
    }

    private fun move(
            targetPosition: TargetPosition,
            unit: UnitEcs,
            gameState: GameState
    ) {
        gameState.moveUnit(unit, targetPosition.axis)
        unit.removeComponent(targetPosition)
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