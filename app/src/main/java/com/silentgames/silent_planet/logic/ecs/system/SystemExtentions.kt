package com.silentgames.silent_planet.logic.ecs.system

import com.silentgames.silent_planet.logic.ecs.GameState
import com.silentgames.silent_planet.logic.ecs.component.Position
import com.silentgames.silent_planet.logic.ecs.entity.cell.Cell
import com.silentgames.silent_planet.logic.ecs.entity.unit.Unit
import com.silentgames.silent_planet.model.Axis

fun GameState.getCurrentUnitCell(unit: Unit, cellExist: (cell: Cell, position: Axis) -> kotlin.Unit) {
    val position = unit.getComponent<Position>()?.currentPosition
    if (position != null) {
        getCell(position)?.let { cellExist.invoke(it, position) }
    }
}

fun GameState.getCurrentUnitCell(unit: Unit, cellExist: (cell: Cell) -> kotlin.Unit) {
    val position = unit.getComponent<Position>()?.currentPosition
    if (position != null) {
        getCell(position)?.let { cellExist.invoke(it) }
    }
}