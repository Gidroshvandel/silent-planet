package com.silentgames.silent_planet.mvp.main

import android.widget.Switch

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.cells.defaultCell.GroundDef
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef
import com.silentgames.silent_planet.model.cells.onVisible.Arrows.Arrow
import com.silentgames.silent_planet.model.cells.onVisible.Arrows.Green
import com.silentgames.silent_planet.model.cells.onVisible.Arrows.Red
import com.silentgames.silent_planet.model.cells.onVisible.Arrows.Yellow
import com.silentgames.silent_planet.model.cells.onVisible.Crystals.Two
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell
import com.silentgames.silent_planet.model.entities.ground.fractions.Alien
import com.silentgames.silent_planet.model.entities.ground.fractions.Human
import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate
import com.silentgames.silent_planet.model.entities.ground.fractions.Robot
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import com.silentgames.silent_planet.utils.BitmapEditor

import java.util.ArrayList
import java.util.Random

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel {

    fun fillBattleGround(): Array<Array<Cell>> {
        val CountOfCells = Constants.getHorizontalCountOfCells()
        val gameMatrix = Array<Array<Cell?>>(Constants.getVerticalCountOfCells() + 1) { arrayOfNulls(Constants.getHorizontalCountOfCells() + 1) }
        for (x in 0 until CountOfCells + 1) {
            for (y in 0 until CountOfCells + 1) {
                if (x == 0 || x == CountOfCells - 1 || y == 0 || y == CountOfCells - 1) {
                    gameMatrix[x][y] = Cell(CellType(SpaceDef()), null)
                    gameMatrix[x][y]!!.cellType.onVisible = SpaceCell()
                } else {
                    gameMatrix[x][y] = Cell(CellType(GroundDef()), null)
                    gameMatrix[x][y]!!.cellType.onVisible = Two()
                }
            }
        }
        spawnShips(gameMatrix as Array<Array<Cell>>)
        return gameMatrix
    }

    fun findPlayerOnCell(gameMatrixCell: Cell): List<String> {
        val data = ArrayList<String>()

        if (gameMatrixCell.entityType!!.playersOnCell != null && gameMatrixCell.entityType!!.playersOnCell!!.getPlayerList() != null) {
            for (player in gameMatrixCell.entityType!!.playersOnCell!!.getPlayerList()!!) {
                data.add(player.playerName!!)
            }
        }
        return data
    }

    private fun spawnShips(gameMatrix: Array<Array<Cell>>) {
        spawnHumans(gameMatrix[0][0])
        spawnPirates(gameMatrix[0][1])
        spawnRobots(gameMatrix[0][2])
        spawnAliens(gameMatrix[0][3])
        TurnHandler.start(Humans.getInstance())
        TurnHandler.setPlayable(Aliens.getInstance())
        TurnHandler.setPlayable(Humans.getInstance())
        TurnHandler.setPlayable(Pirates.getInstance())
        TurnHandler.setPlayable(Robots.getInstance())
    }

    private fun spawnRobots(gameMatrixCell: Cell) {
        val playerList = PlayersOnCell()
        playerList.add(Robot("Maxim"))
        playerList.add(Robot("Oxik"))
        playerList.add(Robot("Andrea"))
        gameMatrixCell.entityType = EntityType(RobotShip())
        gameMatrixCell.entityType!!.playersOnCell = playerList
    }

    private fun spawnAliens(gameMatrixCell: Cell) {
        val playerList = PlayersOnCell()
        playerList.add(Alien("Maxim"))
        playerList.add(Alien("Oxik"))
        playerList.add(Alien("Andrea"))
        gameMatrixCell.entityType = EntityType(AlienShip())
        gameMatrixCell.entityType!!.playersOnCell = playerList
    }

    private fun spawnPirates(gameMatrixCell: Cell) {
        val playerList = PlayersOnCell()
        playerList.add(Pirate("Maxim"))
        playerList.add(Pirate("Oxik"))
        playerList.add(Pirate("Andrea"))
        gameMatrixCell.entityType = EntityType(PirateShip())
        gameMatrixCell.entityType!!.playersOnCell = playerList
    }

    private fun spawnHumans(gameMatrixCell: Cell) {
        val playerList = PlayersOnCell()

        playerList.add(Human("Maxim"))
        playerList.add(Human("Oxik"))
        playerList.add(Human("Andrea"))
        gameMatrixCell.entityType = EntityType(HumanShip())
        gameMatrixCell.entityType!!.playersOnCell = playerList
    }

    private fun randomArrow(x: Int, y: Int): Arrow? {
        when (Random().nextInt(Constants.countArrowCells - 1)) {
            0 -> return Green().rotate(x, y, BitmapEditor.RotateAngle.randomAngle())
            1 -> return Red().rotate(x, y, BitmapEditor.RotateAngle.randomAngle())
            2 -> return Yellow().rotate(x, y, BitmapEditor.RotateAngle.randomAngle())
            else -> return null
        }
    }
}
