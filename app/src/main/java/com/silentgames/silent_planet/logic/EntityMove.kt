package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.utils.*

/**
 * Created by gidroshvandel on 23.09.16.
 */
class EntityMove(private var gameMatrixHelper: GameMatrixHelper) {

    private val isEntityBelongFraction: Boolean
        get() = gameMatrixHelper.gameMatrixCellByXY.entityType.first().fraction === gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.first()?.fraction

    private val isCanMovePlayer: Boolean
        get() = gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.getPlayerByName(gameMatrixHelper.playerName!!)?.isCanMove
                ?: false

    private val isCanFlyToCell: Boolean
        get() = gameMatrixHelper.gameMatrixCellByXY.cellType.isCanFly == gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.getSpaceShip()?.isCanFly

    private val entityTypeXY: MutableList<EntityType>
        get() = gameMatrixHelper.gameMatrixCellByXY.entityType

    private val entityTypeOldXY: MutableList<EntityType>
        get() = gameMatrixHelper.gameMatrixCellByOldXY?.entityType ?: mutableListOf()

    fun canMove(): GameMatrixHelper? {
        return moveCheck()
    }

    fun doEvent(): GameMatrixHelper {
        return gameMatrixHelper.gameMatrixCellByXY.cellType.doEvent(gameMatrixHelper)
    }

    private fun moveCheck(): GameMatrixHelper? {
        val x = gameMatrixHelper.currentXY.x
        val y = gameMatrixHelper.currentXY.y
        val oldX = gameMatrixHelper.oldXY?.x ?: -1
        val oldY = gameMatrixHelper.oldXY?.y ?: -1
        if (isMoveAtDistance(x, y, oldX, oldY) && isPlayable(oldX, oldY) && isCurrentPlayer(oldX, oldY)) {
            if (isSpaceShip(oldX, oldY) && gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.getSpaceShip()?.isCanFly == true && !isSpaceShip(x, y)) {
                if (isCanFlyToCell) {
                    moveShip()
                    TurnHandler.turnCount()
                } else if (gameMatrixHelper.playerName != null) {
                    moveFromBoard()
                    gameMatrixHelper = gameMatrixHelper.gameMatrixCellByXY.cellType.doEvent(gameMatrixHelper)
                    TurnHandler.turnCount()
                }
            } else if (isPlayer(oldX, oldY)) {
                if (isCanMovePlayer) {
                    if (isCanMovePlayer == gameMatrixHelper.gameMatrixCellByXY.cellType.isCanMove) {
                        movePlayer()
                        gameMatrixHelper = gameMatrixHelper.gameMatrixCellByXY.cellType.doEvent(gameMatrixHelper)
                        TurnHandler.turnCount()
                    } else if (isSpaceShip(x, y) && isEntityBelongFraction) {
                        moveOnBoard()
                        TurnHandler.turnCount()
                    }
                }
            }
            return gameMatrixHelper
        } else {
            return null
        }
    }

    private fun isCurrentPlayer(x: Int, y: Int): Boolean {
        return getEntityType(x, y).firstOrNull { it.fraction.fractionsType == TurnHandler.fractionType } != null
    }

    private fun isPlayable(x: Int, y: Int): Boolean {
        return getEntityType(x, y).firstOrNull { it.fraction.isPlayable } != null
    }

//    private fun event(x: Int, y: Int) {
//        if (gameMatrixHelper.gameMatrixCellByXY.cellType.isDead && !getEntityType(x, y)!!.isDead) {
//            val playerList = PlayersOnCell()
//            playerList.add(DeadPlayer(getEntityType(x, y)!!.playersOnCell.playerList!![0]))
//            gameMatrixHelper.gameMatrixCellByXY.entityType = EntityType(playerList)
//        }
//    }

    private fun moveOnBoard() {
        val player = gameMatrixHelper.playerName?.let {
            entityTypeOldXY.getPlayerByName(it)
        }
        entityTypeXY.getSpaceShip()?.crystals = player!!.crystals
        entityTypeXY.getSpaceShip()?.playersOnBord?.add(player.apply { crystals = 0 })
        deletePlayer()
    }

    fun moveOnBoardAllyShip() {
        //Если не найден корабль то что-то не так(должен быть всегда)
        gameMatrixHelper.currentXY = findAllyShip()!!
        moveOnBoard()
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

    private fun moveFromBoard() {
        val selectPlayer = gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.getPlayerByName(gameMatrixHelper.playerName!!)

        gameMatrixHelper.gameMatrixCellByXY.entityType.add(selectPlayer!!)
        gameMatrixHelper.gameMatrixCellByXY.cellType.isVisible = true
        deletePlayerOnBoard()
    }

//    private fun deleteEntity(x: Int, y: Int) {
//        gameMatrixHelper.gameMatrix[x][y].entityType = null
//    }

    //todo объединить
    private fun deletePlayer() {
        entityTypeOldXY.removePlayerByName(gameMatrixHelper.playerName!!)
    }

    //todo объединить
    private fun deletePlayerOnBoard() {
        entityTypeOldXY.removePlayerByName(gameMatrixHelper.playerName!!)
    }

    fun movePlayer() {

        val selectPlayer = entityTypeOldXY.getPlayerByName(gameMatrixHelper.playerName!!)

        gameMatrixHelper.gameMatrixCellByXY.entityType.add(selectPlayer!!)
        gameMatrixHelper.gameMatrixCellByXY.cellType.isVisible = true
        deletePlayer()
    }

    private fun moveShip() {
        gameMatrixHelper.gameMatrixCellByXY.entityType = gameMatrixHelper.gameMatrixCellByOldXY?.entityType!!
        gameMatrixHelper.gameMatrixCellByXY.cellType.isVisible = true
        gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.removeSpaceShip()
    }

    private fun isSpaceShip(x: Int, y: Int): Boolean {
        return getEntityType(x, y).getSpaceShip() != null
    }

    private fun isPlayer(x: Int, y: Int): Boolean {
        return getEntityType(x, y).getAllPlayersFromCell().isNotEmpty()
    }

    private fun getEntityType(x: Int, y: Int): MutableList<EntityType> {
        return gameMatrixHelper.gameMatrix[x][y].entityType
    }

    //Проверки перемещения юнитов
    private fun isMoveAtDistance(x: Int, y: Int, oldX: Int, oldY: Int): Boolean {
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
