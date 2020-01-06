package com.silentgames.silent_planet.logic.ecs

import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.entity.Entity
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis

class GameState(
        val cellMap: MutableList<Cell>,
        val unitMap: MutableList<Unit>,
        var moveSuccess: Boolean = false
) {

    fun getUnit(id: Long) = unitMap.find { it.id == id }

    fun getCell(id: Long) = unitMap.find { it.id == id }

    fun getCell(axis: Axis) = cellMap.getByPosition(axis)

    fun getUnit(axis: Axis) = unitMap.getByPosition(axis)

    fun getUnits(axis: Axis) =
            unitMap.filter { it.getComponent<Position>()?.currentPosition == axis }

    fun moveUnit(fromPosition: Axis, toPosition: Axis) {
        val unit = getUnit(fromPosition)
        if (unit != null) {
            unit.getComponent<Position>()?.currentPosition = toPosition
        }
    }

    private fun <T : Entity> List<T>.getByPosition(axis: Axis) =
            find { it.getComponent<Position>()?.currentPosition == axis }

}