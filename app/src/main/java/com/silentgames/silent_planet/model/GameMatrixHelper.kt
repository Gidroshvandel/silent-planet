package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.model.fractions.FractionsType.*
import com.silentgames.silent_planet.utils.ShipNotFoundException
import com.silentgames.silent_planet.utils.findSpaceShip
import com.silentgames.silent_planet.utils.getSpaceShip

/**
 * Created by gidroshvandel on 28.06.17.
 */
typealias GameMatrix = Array<Array<Cell>>

class GameMatrixHelper(
        gameMatrix: Array<Array<Cell>>,
        var oldGameMatrix: Array<Array<Cell>>? = null,
        var currentXY: Axis = Axis(0, 0),
        var oldXY: Axis? = null,
        var selectedEntity: EntityType? = null
) {

    val humanShip: HumanShip get() = gameMatrix.findSpaceShip()
    val robotShip: RobotShip get() = gameMatrix.findSpaceShip()
    val pirateShip: PirateShip get() = gameMatrix.findSpaceShip()
    val alienShip: AlienShip get() = gameMatrix.findSpaceShip()

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

fun GameMatrix.copy(): GameMatrix {
    val horizontalCountOfCells = Constants.horizontalCountOfCells
    val verticalCountOfCells = Constants.verticalCountOfCells
    return Array(horizontalCountOfCells) { x ->
        Array(verticalCountOfCells) { y ->
            Cell(this[x][y].cellType, this[x][y].entityType.copy())
        }
    }
}

fun List<EntityType>.copy() = Array(this.size) {
    this[it].copy()
}.toMutableList()

fun GameMatrix.getCell(axis: Axis) = this[axis.x][axis.y]

fun GameMatrix.getShip(fractionsType: FractionsType) = this.getShipPosition(fractionsType).entity

fun GameMatrix.getShipPosition(fractionsType: FractionsType) =
        when (fractionsType) {
            ALIEN -> this.findPositionSpaceShip<AlienShip>()
            HUMAN -> this.findPositionSpaceShip<HumanShip>()
            PIRATE -> this.findPositionSpaceShip<PirateShip>()
            ROBOT -> this.findPositionSpaceShip<RobotShip>()
        }

fun GameMatrix.doEvent(entityType: EntityType) =
        this.getEntityCell(entityType)?.cellType?.doEvent(Event(this, entityType)) ?: false

fun GameMatrix.getEntityCell(entityType: EntityType) =
        flatten().find {
            it.entityType.contains(entityType)
                    || it.entityType.getSpaceShip()?.playersOnBord?.contains(entityType) == true
        }

inline fun <reified T : SpaceShip> GameMatrix.findPositionSpaceShip(): EntityPosition<T> {
    this.forEachIndexed { x, arrayOfCells ->
        arrayOfCells.forEachIndexed { y, cell ->
            if (x == 0 || x == this.size - 1 || y == 0 || y == arrayOfCells.size - 1) {
                val ship = cell.entityType.getSpaceShip()
                if (ship != null && ship is T)
                    return EntityPosition(ship, Axis(x, y))
            }
        }
    }
    throw ShipNotFoundException()
}

data class EntityPosition<out A : EntityType>(
        val entity: A,
        val position: Axis
) {
    override fun toString(): String = "($entity, $position)"
}
