package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.utils.notNull

class MovementSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        gameState.moveSuccess = if (unit.hasComponent<Teleport>()
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
                    ::isCanMove
            ) ?: false
        }
        if (gameState.moveSuccess) {
            notNull(
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::move
            )
        }
    }

    private fun move(
            targetPosition: TargetPosition,
            unit: Unit,
            gameState: GameState
    ) {
        gameState.moveUnit(unit, targetPosition.axis)
        unit.removeComponent(targetPosition)
    }

    fun isCanMove(
            targetPosition: Axis,
            currentPosition: Axis,
            unit: Unit,
            gameState: GameState
    ): Boolean = isMoveAtDistance(targetPosition, currentPosition)
            && (isCanMoveToAllyTransport(unit, gameState.getUnit(targetPosition))
            || isCanMoveToCell(gameState.getCell(targetPosition), unit))

    private fun isCanMoveToCell(targetCell: Cell?, unit: Unit) = targetCell != null && canMove(unit.getComponent(), targetCell.getComponent())

    private fun isCanMoveToAllyTransport(unit: Unit, targetUnit: Unit?): Boolean {
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