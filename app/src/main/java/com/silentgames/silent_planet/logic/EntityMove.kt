package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.ShipNotFoundException
import com.silentgames.silent_planet.utils.getSpaceShip
import com.silentgames.silent_planet.utils.isSpaceShipBelongFraction

/**
 * Created by gidroshvandel on 23.09.16.
 */
class EntityMove(private var gameMatrixHelper: GameMatrixHelper) {

    private val entityTypeXY: MutableList<EntityType>
        get() = gameMatrixHelper.gameMatrixCellByXY.entityType

    private val entityTypeOldXY: MutableList<EntityType>
        get() = gameMatrixHelper.gameMatrixCellByOldXY?.entityType ?: mutableListOf()

    fun canMove(entityType: EntityType): GameMatrixHelper? {
        return moveCheck(entityType)
    }

    fun doEvent(): GameMatrixHelper {
        return gameMatrixHelper.gameMatrixCellByXY.cellType.doEvent(gameMatrixHelper)
    }

    private fun moveCheck(entityType: EntityType): GameMatrixHelper? {
        val targetAxis = gameMatrixHelper.currentXY
        val currentAxis = gameMatrixHelper.oldXY ?: Axis(-1, -1)

        val targetCellType = gameMatrixHelper.gameMatrixCellByXY.cellType
        val targetEntityType = gameMatrixHelper.gameMatrixCellByXY.entityType

        if (isMoveAtDistance(targetAxis, currentAxis)
                && isPlayable(currentAxis)
                && isNowPlaying(currentAxis)) {
            if (entityType is SpaceShip
                    && entityType.isCanFly
                    && targetCellType.isCanFly
                    && !isSpaceShip(targetAxis)) {
                moveShip(entityType)
                return gameMatrixHelper
            } else if (entityType is Player) {
                if (entityType.isCanMove) {
                    if (targetCellType.isCanMove) {
                        val enemy = gameMatrixHelper.getEnemy(targetAxis)
                        if (enemy != null) {
                            gameMatrixHelper.captureEnemyUnit(Entity(entityType, currentAxis), enemy)
                        } else {
                            movePlayer(entityType)
                        }
                        return gameMatrixHelper
                    } else if (isSpaceShip(targetAxis)
                            && targetEntityType.isSpaceShipBelongFraction(entityType)) {
                        moveOnBoard(entityType)
                        return gameMatrixHelper
                    }
                }
            }
        }
        return null
    }

    private fun GameMatrixHelper.captureEnemyUnit(entity: Entity<Player>, enemyEntity: Entity<Player>) {
        moveOnBoardAllyShip(entity)
        enemyEntity.entity.isCanMove = false
        moveOnBoardAllyShip(enemyEntity)
    }

    private fun GameMatrixHelper.getEnemy(axis: Axis): Entity<Player>? {
        val enemy = getCell(axis).entityType.getEnemy(currentTurnFraction)
        return if (enemy != null) Entity(enemy, axis) else null
    }

    private fun MutableList<EntityType>.getEnemy(currentFraction: FractionsType) =
            find { !it.isDead && it.fraction.fractionsType != currentFraction && it is Player } as? Player

    private fun isNowPlaying(axis: Axis): Boolean {
        return getEntityType(axis).firstOrNull {
            it.fraction.fractionsType == gameMatrixHelper.currentTurnFraction
        } != null
    }

    private fun isPlayable(axis: Axis): Boolean {
        return getEntityType(axis).firstOrNull { it.fraction.isPlayable } != null
    }

    @Deprecated("")
    private fun moveOnBoard(player: Player) {
        val entityType = entityTypeXY.getSpaceShip()
        if (entityType != null) {
            entityType.crystals = entityType.crystals + player.crystals
            entityType.playersOnBord.add(player.apply { crystals = 0 })
        }
        deletePlayer(player)
    }

    private fun GameMatrixHelper.moveOnBoard(current: Entity<Player>, spaceShip: SpaceShip) {
        spaceShip.crystals = spaceShip.crystals + current.entity.crystals
        spaceShip.playersOnBord.add(current.entity.apply { crystals = 0 })
        this.deletePlayer(current)
    }

    fun GameMatrixHelper.moveOnBoardAllyShip(current: Entity<Player>) {
        moveOnBoard(current, findAllyShip().entity)
    }

    @Deprecated("")
    fun moveOnBoardAllyShip(player: Player) {
        gameMatrixHelper.currentXY = findAllyShip()
        moveOnBoard(player)
    }

    private fun GameMatrixHelper.findAllyShip(): Entity<SpaceShip> {
        var spaceShip: SpaceShip? = null
        gameMatrix.forEachIndexed { x, arrayOfCells ->
            val y = arrayOfCells.indexOfFirst {
                spaceShip = it.entityType.getSpaceShip()
                if (spaceShip?.fraction?.fractionsType == currentTurnFraction) {
                    true
                } else {
                    spaceShip = null
                    false
                }
            }
            spaceShip?.let {
                if (y != -1) {
                    return Entity(it, Axis(x, y))
                }
            }
        }
        throw ShipNotFoundException()
    }

    @Deprecated("")
    private fun findAllyShip(): Axis {
        gameMatrixHelper.gameMatrix.forEachIndexed { x, arrayOfCells ->
            val y = arrayOfCells.indexOfFirst {
                it.entityType.getSpaceShip()?.fraction?.fractionsType == gameMatrixHelper.currentTurnFraction
            }
            if (y != -1) {
                return Axis(x, y)
            }
        }
        throw ShipNotFoundException()
    }

    @Deprecated("")
    private fun deletePlayer(player: Player) {
        if (!entityTypeOldXY.remove(player)) {
            entityTypeOldXY.getSpaceShip()?.playersOnBord?.remove(player)
        }
    }

    private fun GameMatrixHelper.deletePlayer(current: Entity<Player>) {
        this.getCell(current.axis).entityType.deletePlayer(current.entity)
    }

    private fun MutableList<EntityType>.deletePlayer(player: Player) {
        if (!remove(player)) {
            getSpaceShip()?.playersOnBord?.remove(player)
        }
    }

    fun movePlayer(player: Player) {
        gameMatrixHelper.gameMatrixCellByXY.entityType.add(player)
        gameMatrixHelper.gameMatrixCellByXY.cellType.isVisible = true
        deletePlayer(player)
    }

    private fun moveShip(spaceShip: SpaceShip) {
        gameMatrixHelper.gameMatrixCellByXY.entityType.add(spaceShip)
        gameMatrixHelper.gameMatrixCellByXY.cellType.isVisible = true
        gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.remove(spaceShip)
    }

    private fun isSpaceShip(axis: Axis): Boolean {
        return getEntityType(axis).getSpaceShip() != null
    }

    private fun getEntityType(axis: Axis): MutableList<EntityType> {
        return gameMatrixHelper.gameMatrix[axis.x][axis.y].entityType
    }

    //Проверки перемещения юнитов
    private fun isMoveAtDistance(target: Axis, current: Axis): Boolean {
        val targetX = target.x
        val targetY = target.y
        val currentX = current.x
        val currentY = current.y
        if (!(currentX == targetX && currentY == targetY)) {
            for (i in 0..2) {
                for (j in 0..2) {
                    if (currentX + j - 1 == targetX && currentY + i - 1 == targetY) {
                        return true
                    }
                }
            }
        }
        return false
    }
}

data class Entity<out A : EntityType>(
        val entity: A,
        val axis: Axis
) {
    override fun toString(): String = "($entity, $axis)"
}