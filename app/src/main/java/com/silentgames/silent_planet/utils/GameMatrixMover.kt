package com.silentgames.silent_planet.utils

import com.silentgames.silent_planet.engine.Background
import com.silentgames.silent_planet.engine.EngineAxis
import com.silentgames.silent_planet.engine.Entity
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.entities.EntityType

class GameMatrixMover(oldGameMatrix: GameMatrix?, newGameMatrix: GameMatrix) {

    private val oldCells: List<Cell>? = oldGameMatrix?.toCellList()

    private val newCells: List<Cell> = newGameMatrix.toCellList()

    fun convertToSceneLayers(): SceneLayers {
        val backgroundLayer = Layer()
        val entityLayer = Layer()

        val changedList = oldCells?.let { newCells.getChangedEntityList(oldCells) } ?: listOf()

        this.newCells.forEach { newCell ->
            backgroundLayer.add(Background(
                    newCell.position.toEngineAxis(),
                    newCell.cellType.getCurrentBitmap()
            ))
            if (newCell.entityType.isNotEmpty()) {
                val entityType = newCell.entityType.first()
                val entity = Entity(
                        entityType.id,
                        newCell.position.toEngineAxis(),
                        entityType.bitmap
                )
                val changedEntity = changedList.find { it.entityType == entityType }
                if (changedEntity != null) {
                    entity.move(changedEntity.position.toEngineAxis(), newCell.position.toEngineAxis())
                }
                entityLayer.add(entity)
            }
        }
        return SceneLayers(backgroundLayer, entityLayer)
    }

    private fun List<Cell>.getChangedEntityList(oldGameMatrix: List<Cell>): List<EntityTypePosition> {
        val changed = mutableListOf<EntityTypePosition>()

        val oldEntities = oldGameMatrix.toEntityPositionList()
        val newEntities = this.toEntityPositionList()

        oldEntities.forEach { oldEntity ->
            newEntities.forEach { newEntity ->
                if (oldEntity.entityType == newEntity.entityType && oldEntity.position != newEntity.position) {
                    changed.add(oldEntity)
                }
            }
        }
        return changed
    }

    private fun GameMatrix.toCellList() = this.flatten()

    private fun Axis.toEngineAxis(): EngineAxis = EngineAxis(x.toFloat(), y.toFloat())

    private fun List<Cell>.toEntityPositionList() =
            map { cell -> cell.entityType.map { EntityTypePosition(it, cell.position) } }.flatten()

    private class EntityTypePosition(val entityType: EntityType, val position: Axis)

}

class SceneLayers(val background: Layer, val entity: Layer)

