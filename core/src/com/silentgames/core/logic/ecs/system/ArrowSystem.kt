package com.silentgames.core.logic.ecs.system

import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.GameState
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.component.ArrowMode.DIRECT
import com.silentgames.core.logic.ecs.component.ArrowMode.SLANTING
import com.silentgames.core.logic.ecs.component.RotateAngle
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.utils.notNull

class ArrowSystem : UnitSystem() {

    override fun execute(gameState: GameState, unit: UnitEcs) {
        gameState.getCurrentUnitCell(unit) { cell ->
            notNull(
                    cell.getComponent(),
                    unit.getComponent(),
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::move
            )
        }
    }

    private fun move(
            arrow: Arrow,
            unitPosition: Position,
            unitFractionsType: FractionsType,
            unit: UnitEcs,
            gameState: GameState
    ) {
        val correctTarget = getCorrectTarget(gameState, arrow, unitPosition, unitFractionsType)
        if (correctTarget != null) {
            unit.addComponent(Teleport())
            unit.addComponent(TargetPosition(correctTarget))
        } else {
            val capitalShipPosition = gameState.getCapitalShipPosition(unitFractionsType)?.currentPosition
            capitalShipPosition?.let {
                unit.addComponent(Teleport())
                unit.addComponent(TargetPosition(it))
            }
        }
    }

    fun getCorrectTarget(gameState: GameState, arrow: Arrow, arrowCellPosition: Position, unitFractionsType: FractionsType): Axis? {
        val target = arrow.calculateTargetPosition(arrowCellPosition.currentPosition)
        return if (gameState.isDestinationCorrect(target, unitFractionsType)) target else null
    }

    private fun GameState.isDestinationCorrect(destination: Axis, fractionsType: FractionsType): Boolean =
            (destination.inGroundBorders()
                    || destination.inGameBorders() && isTransportBelongFraction(destination, fractionsType))

    private fun GameState.isTransportBelongFraction(target: Axis, fractionsType: FractionsType): Boolean {
        val unit = this.getUnit(target)
        return (unit != null && unit.hasComponent<Transport>() && unit.getComponent<FractionsType>() == fractionsType)
    }

    private fun Axis.inGroundBorders(): Boolean {
        return x <= Constants.verticalCountOfGroundCells &&
                x >= 1 &&
                y <= Constants.horizontalCountOfGroundCells &&
                y >= 1
    }

    private fun Axis.inGameBorders(): Boolean {
        return x < Constants.verticalCountOfCells &&
                x >= 0 &&
                y < Constants.horizontalCountOfCells &&
                y >= 0
    }

    private fun Arrow.calculateTargetPosition(position: Axis) =
            when (arrowMode) {
                DIRECT -> rotateAngle.calculateDirectTargetPosition(position, distance)
                SLANTING -> rotateAngle.calculateSlantingTargetPosition(position, distance)
            }

    private fun RotateAngle.calculateSlantingTargetPosition(position: Axis, distance: Int): Axis =
            when (this) {
                RotateAngle.DEGREES0 -> {
                    Axis(
                            position.x + distance,
                            position.y - distance
                    )
                }
                RotateAngle.DEGREES90 -> {
                    Axis(
                            position.x + distance,
                            position.y + distance
                    )
                }
                RotateAngle.DEGREES180 -> {
                    Axis(
                            position.x - distance,
                            position.y + distance
                    )
                }
                RotateAngle.DEGREES270 -> {
                    Axis(
                            position.x - distance,
                            position.y - distance
                    )
                }
            }

    private fun RotateAngle.calculateDirectTargetPosition(position: Axis, distance: Int): Axis =
            when (this) {
                RotateAngle.DEGREES0 -> {
                    Axis(
                            position.x,
                            position.y - distance
                    )
                }
                RotateAngle.DEGREES90 -> {
                    Axis(
                            position.x + distance,
                            position.y
                    )
                }
                RotateAngle.DEGREES180 -> {
                    Axis(
                            position.x,
                            position.y + distance
                    )
                }
                RotateAngle.DEGREES270 -> {
                    Axis(
                            position.x - distance,
                            position.y
                    )
                }
            }

}