package com.silentgames.silent_planet.utils

import com.silentgames.silent_planet.engine.Background
import com.silentgames.silent_planet.engine.EngineAxis
import com.silentgames.silent_planet.engine.Entity
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.space.SpaceShip

class GameMatrixMover(oldGameMatrix: GameMatrix?, newGameMatrix: GameMatrix) {

    private val oldCells: List<Cell>? = oldGameMatrix?.toCellList()

    private val newCells: List<Cell> = newGameMatrix.toCellList()

    fun convertToSceneLayers(): SceneLayers {
        val backgroundLayer = Layer()
        val entityLayer = Layer()

        val changedList = oldCells?.let { newCells.getChangedEntityList(oldCells).toMutableList() }
                ?: mutableListOf()
        changedList.ifShipMovingRemoveAnotherEntity()

        this.newCells.forEach { newCell ->
            backgroundLayer.add(Background(
                    newCell.position.toEngineAxis(),
                    newCell.cellType.getCurrentBitmap()
            ))
            if (newCell.entityType.isNotEmpty()) {
                newCell.entityType.extractPlayersFromShips().forEachIndexed { index, entityType ->
                    val changedEntity = changedList.find { it.entityType == entityType }
                    if (changedEntity != null || index == 0) {
                        val entity = Entity(
                                entityType.id,
                                newCell.position.toEngineAxis(),
                                entityType.bitmap
                        )

                        if (changedEntity != null) {
                            entity.move(changedEntity.oldPosition.toEngineAxis(), newCell.position.toEngineAxis())
                        }
                        entityLayer.add(entity)
                    }
                }
            }
        }
        return SceneLayers(backgroundLayer, entityLayer)
    }

    private fun MutableList<EntityTypePosition>.ifShipMovingRemoveAnotherEntity() {
        if (this.any { it.entityType is SpaceShip }) {
            this.removeAll { it.entityType !is SpaceShip }
        }
    }

    private fun List<Cell>.getChangedEntityList(oldGameMatrix: List<Cell>): List<EntityTypePosition> {
        val changed = mutableListOf<EntityTypePosition>()

        val oldEntities = oldGameMatrix.toEntityPositionList().fromShipsToList()
        val newEntities = this.toEntityPositionList().fromShipsToList()

        oldEntities.forEach { oldEntity ->
            newEntities.forEach { newEntity ->
                if (oldEntity.entityType == newEntity.entityType && oldEntity.oldPosition != newEntity.oldPosition) {
                    changed.add(oldEntity.copy(newPosition = newEntity.oldPosition))
                }
            }
        }
        return changed
    }

    private fun GameMatrix.toCellList() = this.flatten()

    private fun Axis.toEngineAxis(): EngineAxis = EngineAxis(x.toFloat(), y.toFloat())

    private fun List<Cell>.toEntityPositionList() =
            map { cell -> cell.entityType.map { EntityTypePosition(it, cell.position) } }.flatten()

    private fun List<EntityTypePosition>.fromShipsToList(): List<EntityTypePosition> =
            this.toMutableList().apply {
                addAll(
                        filter { it.entityType is SpaceShip }.map { entityTypePosition ->
                            (entityTypePosition.entityType as SpaceShip).playersOnBord.map {
                                EntityTypePosition(it, entityTypePosition.oldPosition)
                            }
                        }.flatten()
                )
            }

    private fun List<EntityType>.extractPlayersFromShips(): List<EntityType> =
            this.toMutableList().apply {
                addAll(filterIsInstance<SpaceShip>().map { it.playersOnBord }.flatten())
            }

    private data class EntityTypePosition(
            val entityType: EntityType,
            val oldPosition: Axis,
            val newPosition: Axis? = null
    )

}

class SceneLayers(val background: Layer, val entity: Layer)

