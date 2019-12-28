package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType

/**
 * Created by gidroshvandel on 09.07.16.
 */
class Cell(val cellType: CellType, val entityType: MutableList<EntityType> = mutableListOf()) {

    val position get() = cellType.position

}
