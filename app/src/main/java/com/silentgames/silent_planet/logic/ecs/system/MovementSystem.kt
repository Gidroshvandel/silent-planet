package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.getAvailableMoveDistancePositionList
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.notNull

class MovementSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        gameState.moveSuccess = if (unit.hasComponent<Teleport>()) {
            false
        } else {
            notNull(
                    unit.getComponent(),
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::move
            ) ?: false
        }
    }

    private fun move(
            targetPosition: TargetPosition,
            position: Position,
            unit: Unit,
            gameState: GameState
    ): Boolean {
        val targetCell = gameState.getCell(targetPosition.axis)
        val targetUnit = gameState.getUnit(targetPosition.axis)
        if (isMoveAtDistance(targetPosition.axis, position.currentPosition)) {
            val transport = targetUnit?.getComponent<Transport>()
            if (transport != null
                    && unit.getComponent<Transport>() == null
                    && isTargetUnitFromAllyFraction(targetUnit.getComponent(), unit.getComponent())
            ) {
                gameState.moveUnit(unit, targetPosition.axis)
                unit.removeComponent(targetPosition)
                return true
            } else if (targetCell != null && canMove(unit.getComponent(), targetCell.getComponent())) {
                gameState.moveUnit(unit, targetPosition.axis)
                unit.removeComponent(targetPosition)
                return true
            }
        }
        return false
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