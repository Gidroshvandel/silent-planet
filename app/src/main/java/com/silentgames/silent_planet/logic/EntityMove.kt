package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.space.SpaceShip
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
        val currentXY = gameMatrixHelper.currentXY
        val oldXY = gameMatrixHelper.oldXY ?: Axis(-1, -1)

        val currentCellType = gameMatrixHelper.gameMatrixCellByXY.cellType
        val currentEntityType = gameMatrixHelper.gameMatrixCellByXY.entityType

        if (isMoveAtDistance(currentXY, oldXY)
                && isPlayable(oldXY)
                && isCurrentPlayer(oldXY)) {
            if (entityType is SpaceShip
                    && entityType.isCanFly
                    && currentCellType.isCanFly
                    && !isSpaceShip(currentXY)) {
                moveShip(entityType)
                TurnHandler.turnCount()
                return gameMatrixHelper
            } else if (entityType is Player) {
                if (entityType.isCanMove) {
                    if (currentCellType.isCanMove) {
                        movePlayer(entityType)
                        gameMatrixHelper = currentCellType.doEvent(gameMatrixHelper)
                        TurnHandler.turnCount()
                        return gameMatrixHelper
                    } else if (isSpaceShip(currentXY)
                            && currentEntityType.isSpaceShipBelongFraction(entityType)) {
                        moveOnBoard(entityType)
                        TurnHandler.turnCount()
                        return gameMatrixHelper
                    }
                }
            }
        }
        return null
    }

    private fun isCurrentPlayer(axis: Axis): Boolean {
        return getEntityType(axis).firstOrNull {
            it.fraction.fractionsType == TurnHandler.fractionType
        } != null
    }

    private fun isPlayable(axis: Axis): Boolean {
        return getEntityType(axis).firstOrNull { it.fraction.isPlayable } != null
    }

    private fun moveOnBoard(player: Player) {
        entityTypeXY.getSpaceShip()?.crystals = player.crystals
        entityTypeXY.getSpaceShip()?.playersOnBord?.add(player.apply { crystals = 0 })
        deletePlayer(player)
    }

    fun moveOnBoardAllyShip(player: Player) {
        //Если не найден корабль то что-то не так(должен быть всегда)
        gameMatrixHelper.currentXY = findAllyShip()!!
        moveOnBoard(player)
    }

    private fun findAllyShip(): Axis? {
        gameMatrixHelper.gameMatrix.forEachIndexed { x, arrayOfCells ->
            val y = arrayOfCells.indexOfFirst {
                it.entityType.getSpaceShip()?.fraction?.fractionsType == TurnHandler.fractionType
            }
            if (y != -1) {
                return Axis(x, y)
            }
        }
        return null
    }

    private fun deletePlayer(player: Player) {
        if (!entityTypeOldXY.remove(player)) {
            entityTypeOldXY.getSpaceShip()?.playersOnBord?.remove(player)
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
    private fun isMoveAtDistance(currentXY: Axis, oldXY: Axis): Boolean {
        val x = currentXY.x
        val y = currentXY.y
        val oldX = oldXY.x
        val oldY = oldXY.y
        if (!(oldX == x && oldY == y)) {
            for (i in 0..2) {
                for (j in 0..2) {
                    if (oldX + j - 1 == x && oldY + i - 1 == y) {
                        return true
                    }
                }
            }
        }
        return false
    }
}
