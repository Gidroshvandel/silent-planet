package com.silentgames.silent_planet.mvp.main

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.Arrow.Arrow
import com.silentgames.silent_planet.model.cells.Arrow.ArrowGreen
import com.silentgames.silent_planet.model.cells.Arrow.ArrowRed
import com.silentgames.silent_planet.model.cells.Arrow.ArrowYellow
import com.silentgames.silent_planet.model.cells.Crystal.Crystal
import com.silentgames.silent_planet.model.cells.Crystal.CrystalsEnum
import com.silentgames.silent_planet.model.cells.SpaceCell
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.entities.ground.fractions.Alien
import com.silentgames.silent_planet.model.entities.ground.fractions.Human
import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate
import com.silentgames.silent_planet.model.entities.ground.fractions.Robot
import com.silentgames.silent_planet.model.entities.space.SpaceShip
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip
import com.silentgames.silent_planet.model.fractions.factionType.Aliens
import com.silentgames.silent_planet.model.fractions.factionType.Humans
import com.silentgames.silent_planet.model.fractions.factionType.Pirates
import com.silentgames.silent_planet.model.fractions.factionType.Robots
import com.silentgames.silent_planet.utils.BitmapEditor
import java.util.*

/**
 * Created by gidroshvandel on 22.06.17.
 */
class MainModel {

    fun fillBattleGround(): Array<Array<Cell>> {
        val vCountOfCells = Constants.verticalCountOfCells
        val hCountOfCells = Constants.horizontalCountOfCells
        val gameMatrix = Array(hCountOfCells) { x ->
            Array(vCountOfCells) { y ->
                if (x == 0 || x == hCountOfCells - 1 || y == 0 || y == vCountOfCells - 1) {
                    Cell(SpaceCell())
                } else {
                    Cell(Crystal(CrystalsEnum.random()))
                }
            }
        }
        spawnShips(gameMatrix)
        return gameMatrix
    }

    fun getPlayersNameOnCell(gameMatrixCell: Cell): List<String> {
        val data = ArrayList<String>()

        if (gameMatrixCell.entityType.isNotEmpty()) {
            for (entityType in gameMatrixCell.entityType) {
                if (entityType is SpaceShip) {
                    entityType.playersOnBord.forEach { data.add(it.playerName) }
                } else if (entityType is Player) {
                    data.add(entityType.playerName)
                }
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
        val playerList = mutableListOf(
                Robot("Maxim"),
                Robot("Oxik"),
                Robot("Andrea")
        )
        gameMatrixCell.entityType.add(RobotShip().apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnAliens(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Alien("Maxim"),
                Alien("Oxik"),
                Alien("Andrea")
        )
        gameMatrixCell.entityType.add(AlienShip().apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnPirates(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Pirate("Maxim"),
                Pirate("Oxik"),
                Pirate("Andrea")
        )
        gameMatrixCell.entityType.add(PirateShip().apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnHumans(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Human("Maxim"),
                Human("Oxik"),
                Human("Andrea")
        )
        gameMatrixCell.entityType.add(HumanShip().apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun randomArrow(x: Int, y: Int): Arrow? {
        when (Random().nextInt(Constants.countArrowCells - 1)) {
            0 -> return ArrowGreen().rotate(x, y, BitmapEditor.RotateAngle.randomAngle())
            1 -> return ArrowRed().rotate(x, y, BitmapEditor.RotateAngle.randomAngle())
            2 -> return ArrowYellow().rotate(x, y, BitmapEditor.RotateAngle.randomAngle())
            else -> return null
        }
    }
}
