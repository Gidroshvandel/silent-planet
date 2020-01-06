package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.MovingMode
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.TargetPosition
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.logic.getAvailableMoveDistancePositionList
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.notNull

class MovementSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
        notNull(
                unit.getComponent(),
                unit.getComponent(),
                unit,
                gameState,
                ::move
        )
    }

    private fun move(
            targetPosition: TargetPosition,
            position: Position,
            unit: Unit,
            gameState: GameState
    ) {
        val targetCell = gameState.getCell(targetPosition.axis)
        val targetUnit = gameState.getUnit(targetPosition.axis)
        if (isMoveAtDistance(targetPosition.axis, position.currentPosition)) {
            val transport = targetUnit?.getComponent<Transport>()
            if (transport != null
                    && unit.getComponent<Transport>() == null
                    && isTargetUnitFromAllyFraction(targetUnit.getComponent(), unit.getComponent())
            ) {
                gameState.moveOnBoard(unit, transport)
                gameState.moveSuccess = true
                return
            } else if (targetCell != null && canMove(unit.getComponent(), targetCell.getComponent())) {
                gameState.moveUnit(position.currentPosition, targetPosition.axis)
                gameState.moveSuccess = true
                return
            }
        }
        gameState.moveSuccess = false
    }

    private fun GameState.moveOnBoard(entity: Entity, targetTransport: Transport) {
        this.unitMap.remove(entity)
        targetTransport.addOnBoard(entity)
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