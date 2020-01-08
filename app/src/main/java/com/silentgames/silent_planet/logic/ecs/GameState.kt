package com.silentgames.silent_planet.logic.ecs

import com.silentgames.silent_planet.logic.ecs.component.CapitalShip
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.fractions.FractionsType

class GameState(
        cellList: List<Cell>,
        unitList: List<Unit>,
        firstTurnFraction: FractionsType,
        var moveSuccess: Boolean = false
) {

    val turn: Turn = Turn(firstTurnFraction)

    val cellMap: List<Cell> get() = mutableCellList.toList()
    val unitMap: List<Unit> get() = mutableUnitList.toList()

    private val mutableCellList: MutableList<Cell> = cellList.toMutableList()
    private val mutableUnitList: MutableList<Unit> = unitList.toMutableList()

    fun getUnit(id: Long) = unitMap.find { it.id == id }

    fun getCell(id: Long) = unitMap.find { it.id == id }

    fun getCell(axis: Axis) = cellMap.getByPosition(axis)

    fun getUnit(axis: Axis) = unitMap.getByPosition(axis)

    fun getUnits(axis: Axis) =
            unitMap.filter { it.getComponent<Position>()?.currentPosition == axis }

    fun getCapitalShip(unitFractionsType: FractionsType) =
            unitMap.findCapitalShip(unitFractionsType)

    fun getCapitalShipPosition(unitFractionsType: FractionsType) =
            getCapitalShip(unitFractionsType)?.getComponent<Position>()

    fun moveUnit(fromPosition: Axis, toPosition: Axis) {
        val unit = getUnit(fromPosition)
        if (unit != null) {
            moveUnit(unit, toPosition)
        }
    }

    fun moveUnit(unit: Unit, toPosition: Axis) {
        if (!unitMap.contains(unit)) {
            mutableUnitList.add(unit)
        }
        unit.getComponent<Position>()?.currentPosition = toPosition
    }

    fun removeUnit(unit: Unit) {
        mutableUnitList.remove(unit)
    }

    private fun <T : Entity> List<T>.getByPosition(axis: Axis) =
            find { it.getComponent<Position>()?.currentPosition == axis }

    private fun List<Unit>.findCapitalShip(unitFractionsType: FractionsType) =
            find { it.hasComponent<CapitalShip>() && it.getComponent<FractionsType>() == unitFractionsType }

}

fun List<Unit>.extractTransports(): List<Unit> {
    val onBoardEntities = mutableListOf<Unit>()
    forEach {
        val transport = it.getComponent<Transport>()
        if (transport != null) {
            onBoardEntities.addAll(transport.unitsOnBoard)
        }
    }
    return this.toMutableList().apply {
        addAll(onBoardEntities)
    }
}