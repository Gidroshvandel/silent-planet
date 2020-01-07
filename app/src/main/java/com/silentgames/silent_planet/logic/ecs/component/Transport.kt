package com.silentgames.silent_planet.logic.ecs.component

import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit

class Transport(private val mutableUnitsOnBoard: MutableList<Unit>) : ComponentEquals() {

    val unitsOnBoard: List<Unit> get() = mutableUnitsOnBoard

    fun addOnBoard(unit: Unit) {
        mutableUnitsOnBoard.add(unit)
    }

    fun removeFromBoard(unit: Unit) {
        mutableUnitsOnBoard.remove(unit)
    }

}