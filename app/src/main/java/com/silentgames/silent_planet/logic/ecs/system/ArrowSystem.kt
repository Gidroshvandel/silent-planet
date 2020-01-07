package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.*
import com.silentgames.silent_planet.logic.ecs.component.ArrowMode.DIRECT
import com.silentgames.silent_planet.logic.ecs.component.ArrowMode.SLANTING
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.utils.notNull

class ArrowSystem : System {

    override fun execute(gameState: GameState, unit: Unit) {
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
            unit: Unit,
            gameState: GameState
    ) {
        val target = arrow.calculateTargetPosition(unitPosition.currentPosition)
        if (gameState.isDestinationCorrect(target, unitFractionsType)
                && !gameState.isCyclicMove(target, unitPosition.currentPosition, unitPosition.oldPosition)) {
            unit.addComponent(Teleport(target))
            unit.removeComponent(TargetPosition::class.java)
        } else {
            //todo move to ship
        }
    }

    private fun GameState.isCyclicMove(destination: Axis, unitPosition: Axis, previousPosition: Axis): Boolean {
//        val previousCell = this.getCell(previousPosition)?.getComponent<Arrow>()
        val destinationCell = this.getCell(destination)?.getComponent<Arrow>()
        val target = destinationCell?.calculateTargetPosition(destination)
//        val previous = previousCell?.calculateTargetPosition(previousPosition)
        return target == unitPosition && destination == previousPosition
    }

    private fun GameState.isDestinationCorrect(destination: Axis, fractionsType: FractionsType): Boolean =
            (destination.inGameBorders() && destination.inGroundBorders()
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

    private fun BitmapEditor.RotateAngle.calculateSlantingTargetPosition(position: Axis, distance: Int): Axis =
            when (this) {
                BitmapEditor.RotateAngle.DEGREES0 -> {
                    Axis(
                            position.x + distance,
                            position.y - distance
                    )
                }
                BitmapEditor.RotateAngle.DEGREES90 -> {
                    Axis(
                            position.x + distance,
                            position.y + distance
                    )
                }
                BitmapEditor.RotateAngle.DEGREES180 -> {
                    Axis(
                            position.x - distance,
                            position.y + distance
                    )
                }
                BitmapEditor.RotateAngle.DEGREES270 -> {
                    Axis(
                            position.x - distance,
                            position.y - distance
                    )
                }
            }

    private fun BitmapEditor.RotateAngle.calculateDirectTargetPosition(position: Axis, distance: Int): Axis =
            when (this) {
                BitmapEditor.RotateAngle.DEGREES0 -> {
                    Axis(
                            position.x,
                            position.y - distance
                    )
                }
                BitmapEditor.RotateAngle.DEGREES90 -> {
                    Axis(
                            position.x + distance,
                            position.y
                    )
                }
                BitmapEditor.RotateAngle.DEGREES180 -> {
                    Axis(
                            position.x,
                            position.y + distance
                    )
                }
                BitmapEditor.RotateAngle.DEGREES270 -> {
                    Axis(
                            position.x - distance,
                            position.y
                    )
                }
            }

}