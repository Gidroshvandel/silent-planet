package com.silentgames.silent_planet.logic

import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer
import java.util.*

/**
 * Created by gidroshvandel on 23.09.16.
 */
class EntityMove(private var gameMatrixHelper: GameMatrixHelper) {

    private val isEntityBelongFraction: Boolean
        get() = gameMatrixHelper.gameMatrixCellByXY.entityType?.fraction === gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.fraction

    private val isCanMovePlayer: Boolean
        get() = gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.playersOnCell?.getPlayerByName(gameMatrixHelper.playerName!!)!!.isCanMove

    private val isCanFlyToCell: Boolean
        get() = gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.isCanFly == gameMatrixHelper.gameMatrixCellByXY.cellType.isCanFly

    private val entityTypeXY: EntityType
        get() = gameMatrixHelper.gameMatrixCellByXY.entityType!!

    private val entityTypeOldXY: EntityType
        get() = gameMatrixHelper.gameMatrixCellByOldXY?.entityType!!

    fun canMove(): GameMatrixHelper? {
        return moveCheck()
    }

    fun doEvent(): GameMatrixHelper {
        return gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible!!.doEvent(gameMatrixHelper)
    }

    private fun moveCheck(): GameMatrixHelper? {
        val x = gameMatrixHelper.x
        val y = gameMatrixHelper.y
        val oldX = gameMatrixHelper.oldXY?.x ?: -1
        val oldY = gameMatrixHelper.oldXY?.y ?: -1
        if (isMoveAtDistance(x, y, oldX, oldY) && isPlayable(oldX, oldY) && isCurrentPlayer(oldX, oldY)) {
            if (isSpaceShip(oldX, oldY) && gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.spaceShip!!.isCanFly && !isSpaceShip(x, y)) {
                if (isCanFlyToCell) {
                    moveShip()
                    TurnHandler.turnCount()
                } else if (gameMatrixHelper.playerName != null) {
                    moveFromBoard()
                    gameMatrixHelper = gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible?.doEvent(gameMatrixHelper)!!
                    TurnHandler.turnCount()
                }
            } else if (isPlayer(oldX, oldY)) {
                if (isCanMovePlayer) {
                    if (isCanMovePlayer == gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible!!.isCanMove) {
                        movePlayer()
                        gameMatrixHelper = gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible?.doEvent(gameMatrixHelper)!!
                        TurnHandler.turnCount()
                    } else if (isSpaceShip(x, y) && isEntityBelongFraction) {
                        moveOnBoard()
                    }
                }
            }
            return gameMatrixHelper
        } else {
            return null
        }
    }

    private fun isCurrentPlayer(x: Int, y: Int): Boolean {
        return getEntityType(x, y)!!.fraction?.fractionsEnum == TurnHandler.fraction
    }

    private fun isPlayable(x: Int, y: Int): Boolean {
        return getEntityType(x, y)!!.fraction?.isPlayable!!
    }

    private fun event(x: Int, y: Int) {
        if (gameMatrixHelper.gameMatrixCellByXY.cellType.isDead && !getEntityType(x, y)!!.isDead) {
            val playerList = PlayersOnCell()
            playerList.add(DeadPlayer(getEntityType(x, y)!!.playersOnCell?.playerList!![0]))
            gameMatrixHelper.gameMatrixCellByXY.entityType = EntityType(playerList)
        }
    }

    private fun moveOnBoard() {
        entityTypeXY.playersOnCell?.add(gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.playersOnCell?.getPlayerByName(gameMatrixHelper.playerName!!)!!)
        deletePlayer()
    }

    fun moveOnBoardAllyShip() {
        val XY = findAllyShip()
        gameMatrixHelper.x = XY!!["X"]!!
        gameMatrixHelper.y = XY["Y"]!!
        moveOnBoard()
    }

    private fun findAllyShip(): Map<String, Int>? {
        for (i in 0 until Constants.getHorizontalCountOfCells()) {
            for (j in 0 until Constants.getVerticalCountOfCells()) {
                if (gameMatrixHelper.gameMatrix[i][j].cellType.onVisible != null && gameMatrixHelper.gameMatrix[i][j].cellType.onVisible == SpaceCell::class.java || gameMatrixHelper.gameMatrix.get(i)[j].cellType.default != null && gameMatrixHelper.gameMatrix[i][j].cellType.default == SpaceDef::class.java) {

                    if (gameMatrixHelper.gameMatrix[i][j].entityType != null) {
                        if (gameMatrixHelper.gameMatrix[i][j].entityType?.spaceShip != null) {
                            if (gameMatrixHelper.gameMatrix[i][j].entityType?.spaceShip?.fraction != null) {
                                if (gameMatrixHelper.gameMatrix[i][j].entityType?.spaceShip?.fraction?.fractionsEnum == TurnHandler.fraction) {
                                    val XY = HashMap<String, Int>()
                                    XY["X"] = i
                                    XY["Y"] = j
                                    return XY
                                }
                            }
                        }
                    }

                }
            }
        }
        return null
    }

    private fun moveFromBoard() {
        val selectPlayer = PlayersOnCell()
        selectPlayer.add(gameMatrixHelper.gameMatrixCellByOldXY?.entityType?.playersOnCell?.getPlayerByName(gameMatrixHelper.playerName!!)!!)

        gameMatrixHelper.gameMatrixCellByXY.addEntityType(EntityType(selectPlayer))
        gameMatrixHelper.gameMatrixCellByXY.cellType = CellType(gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible!!)
        deletePlayerOnBoard()
    }

    private fun deleteEntity(x: Int, y: Int) {
        gameMatrixHelper.gameMatrix[x][y].entityType = null
    }

    private fun deletePlayer() {
        if (gameMatrixHelper.gameMatrixCellByOldXY?.entityType!!.playersOnCell!!.playerList!!.size == 1) {
            gameMatrixHelper.gameMatrixCellByOldXY?.entityType = null
        } else {
            gameMatrixHelper.gameMatrixCellByOldXY?.entityType!!.playersOnCell!!.removePlayerByName(gameMatrixHelper.playerName!!)
        }
    }

    private fun deletePlayerOnBoard() {
        if (entityTypeOldXY.playersOnCell?.playerList!!.size == 1) {
            entityTypeOldXY.playersOnCell?.playerList = null
        } else {
            entityTypeOldXY.playersOnCell?.removePlayerByName(gameMatrixHelper.playerName!!)
        }
    }

    fun movePlayer() {

        val selectPlayer = PlayersOnCell()
        selectPlayer.add(entityTypeOldXY.playersOnCell?.getPlayerByName(gameMatrixHelper.playerName!!)!!)

        gameMatrixHelper.gameMatrixCellByXY.addEntityType(EntityType(selectPlayer))
        gameMatrixHelper.gameMatrixCellByXY.cellType = CellType(gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible!!)
        deletePlayer()
    }

    private fun moveShip() {
        gameMatrixHelper.gameMatrixCellByXY.addEntityType(gameMatrixHelper.gameMatrixCellByOldXY?.entityType!!)
        gameMatrixHelper.gameMatrixCellByXY.cellType = CellType(gameMatrixHelper.gameMatrixCellByXY.cellType.onVisible!!)
        gameMatrixHelper.gameMatrixCellByOldXY?.entityType = null
    }

    private fun isSpaceShip(x: Int, y: Int): Boolean {
        return getEntityType(x, y) != null && getEntityType(x, y)!!.spaceShip != null
    }

    private fun isPlayer(x: Int, y: Int): Boolean {
        return getEntityType(x, y) != null && getEntityType(x, y)!!.playersOnCell != null && getEntityType(x, y)!!.playersOnCell?.playerList?.size ?: 0 != 0
    }

    private fun getEntityType(x: Int, y: Int): EntityType? {
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
