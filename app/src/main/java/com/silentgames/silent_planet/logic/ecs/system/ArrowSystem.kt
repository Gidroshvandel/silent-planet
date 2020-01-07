package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Arrow
import com.silentgames.silent_planet.logic.ecs.component.ArrowMode.DIRECT
import com.silentgames.silent_planet.logic.ecs.component.ArrowMode.SLANTING
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Teleport
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.utils.notNull

class ArrowSystem : System {

    private var flag = true

    override fun execute(gameState: GameState, unit: Unit) {
        gameState.getCurrentUnitCell(unit) { cell ->
            flag = false
            gameState.moveAgain = notNull(
                    cell.getComponent(),
                    unit.getComponent(),
                    unit.getComponent(),
                    unit,
                    gameState,
                    ::move
            ) ?: false
        }
        if (flag) {
            gameState.moveAgain = false
        }
    }

    private fun move(
            arrow: Arrow,
            unitPosition: Position,
            unitFractionsType: FractionsType,
            unit: Unit,
            gameState: GameState
    ): Boolean {
        val target = arrow.calculateTargetPosition(unitPosition.currentPosition)
        return if (gameState.isDestinationCorrect(target, unitFractionsType)
                && !gameState.isCyclicMove(target, unitPosition.currentPosition)) {
            unit.addComponent(Teleport(target))
            true
        } else {
            //todo move to ship
            false
        }
    }

    private fun GameState.isCyclicMove(destination: Axis, unitPosition: Axis): Boolean {
        val destinationCell = this.getCell(destination)?.getComponent<Arrow>()
        val target = destinationCell?.calculateTargetPosition(destination)
        return target == unitPosition
    }

    private fun GameState.isDestinationCorrect(destination: Axis, fractionsType: FractionsType): Boolean =
            (destination.inGameBorders() && destination.inGroundBorders()
                    || destination.inGameBorders() && isTransportBelongFraction(destination, fractionsType))

    private fun GameState.isTransportBelongFraction(target: Axis, fractionsType: FractionsType): Boolean {
        val unit = this.getUnit(target)
        return (unit != null && unit.hasComponent<Transport>() && unit.getComponent<FractionsType>() == fractionsType)
    }

    fun Axis.inGroundBorders(): Boolean {
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