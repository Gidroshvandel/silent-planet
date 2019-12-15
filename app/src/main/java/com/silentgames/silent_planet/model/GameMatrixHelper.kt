package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.model.fractions.FractionsType.*
import com.silentgames.silent_planet.utils.findSpaceShip

/**
 * Created by gidroshvandel on 28.06.17.
 */
typealias GameMatrix = Array<Array<Cell>>

class GameMatrixHelper(
        gameMatrix: Array<Array<Cell>>,
        var oldGameMatrix: Array<Array<Cell>>? = null,
        var currentXY: Axis = Axis(0, 0),
        var oldXY: Axis? = null,
        var selectedEntity: EntityType? = null,
        var isEventMove: Boolean = false) {

    val humanShip: HumanShip get() = gameMatrix.findSpaceShip()
    val robotShip: RobotShip get() = gameMatrix.findSpaceShip()
    val pirateShip: PirateShip get() = gameMatrix.findSpaceShip()
    val alienShip: AlienShip get() = gameMatrix.findSpaceShip()

    val currentTurnFraction get() = TurnHandler.fractionType

    var gameMatrix: Array<Array<Cell>> = gameMatrix
        set(value) {
            oldGameMatrix = field
            field = value
        }

    val gameMatrixCellByOldXY: Cell? get() = oldXY?.let { gameMatrix[it.x][it.y] }

    var gameMatrixCellByXY: Cell
        get() = gameMatrix[currentXY.x][currentXY.y]
        set(gameMatrixCell) {
            this.gameMatrix[currentXY.x][currentXY.y] = gameMatrixCell
        }

}

fun GameMatrix.getCell(axis: Axis) = this[axis.x][axis.y]

fun GameMatrix.getShip(fractionsType: FractionsType) =
        when (fractionsType) {
            ALIEN -> this.findSpaceShip<AlienShip>()
            HUMAN -> this.findSpaceShip<HumanShip>()
            PIRATE -> this.findSpaceShip<PirateShip>()
            ROBOT -> this.findSpaceShip<RobotShip>()
        }
