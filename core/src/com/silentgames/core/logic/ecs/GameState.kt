package com.silentgames.core.logic.ecs

import com.silentgames.core.logic.CoreLogger
import com.silentgames.core.logic.ecs.component.*
import com.silentgames.core.logic.ecs.entity.EntityEcs
import com.silentgames.core.logic.ecs.entity.cell.CellEcs
import com.silentgames.core.logic.ecs.entity.event.EventEcs
import com.silentgames.core.logic.ecs.entity.unit.UnitEcs
import com.silentgames.core.logic.ecs.system.getName

class GameState(
    private val mutableEventList: MutableList<EventEcs>,
    private val mutableCellList: MutableList<CellEcs>,
    private val mutableUnitList: MutableList<UnitEcs>,
    val aiFractionList: List<FractionsType> = listOf(),
    val turn: Turn
) {

    constructor(
        cellList: List<CellEcs>,
        unitList: List<UnitEcs>,
        firstTurnFraction: FractionsType,
        aiFractionList: List<FractionsType> = listOf()
    ) : this(
        mutableListOf(),
        cellList.toMutableList(),
        unitList.toMutableList(),
        aiFractionList,
        Turn(firstTurnFraction)
    )

    @Transient
    private val onEventAddList = mutableListOf<((EventEcs) -> Unit)>()

    val eventList: List<EventEcs> get() = mutableEventList.toList()
    val cellMap: List<CellEcs> get() = mutableCellList.toList()
    val unitMap: List<UnitEcs> get() = mutableUnitList.toList()

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

    fun getAllFractionUnits(fractionsType: FractionsType): List<UnitEcs> =
        unitMap.filter { it.getComponent<FractionsType>() == fractionsType }

    fun moveUnit(unit: UnitEcs, toPosition: Axis) {
        if (!unitMap.contains(unit)) {
            mutableUnitList.add(unit)
        }
        CoreLogger.logDebug("Moving", "unit ${unit.getName()} start Moving")
        unit.setCurrentPosition(toPosition)
    }

    fun addEvent(event: EventEcs) {
        onEventAddList.forEach {
            it.invoke(event)
        }
        mutableEventList.add(event)
    }

    fun addNewEventListener(listener: (EventEcs) -> Unit) {
        onEventAddList.add(listener)
    }

    fun removeEvent(event: EventEcs) {
        mutableEventList.remove(event)
    }

    fun removeUnit(unit: UnitEcs) {
        mutableUnitList.remove(unit)
    }

    fun isTurnEnd() = getAllFractionUnits(turn.currentFraction).find {
        it.hasComponent<CanTurn>()
    } == null

    fun isMovingFinish() = unitMap.find { it.hasComponent<Moving>() } == null

    private fun <T : EntityEcs> List<T>.getByPosition(axis: Axis) =
        find { it.getComponent<Position>()?.currentPosition == axis }

    private fun List<UnitEcs>.findCapitalShip(unitFractionsType: FractionsType) =
        find { it.hasComponent<CapitalShip>() && it.getComponent<FractionsType>() == unitFractionsType }
}
