package com.silentgames.silent_planet.mvp.main

import android.content.Context
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.TurnHandler
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.Arrow.Arrow
import com.silentgames.silent_planet.model.cells.Arrow.ArrowGreen
import com.silentgames.silent_planet.model.cells.Arrow.ArrowRed
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.cells.Crystal.Crystal
import com.silentgames.silent_planet.model.cells.Crystal.CrystalsEnum
import com.silentgames.silent_planet.model.cells.EmptyCell
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
class MainModel(val context: Context) {

    fun fillBattleGround(): Array<Array<Cell>> {
        val vCountOfCells = Constants.verticalCountOfCells
        val hCountOfCells = Constants.horizontalCountOfCells
        val gameMatrix = Array(hCountOfCells) { x ->
            Array(vCountOfCells) { y ->
                if (x == 0 || x == hCountOfCells - 1 || y == 0 || y == vCountOfCells - 1) {
                    Cell(SpaceCell(context))
                } else {
                    Cell(getRandomCell(Axis(x, y)))
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
                    entityType.playersOnBord.forEach { data.add(it.name) }
                } else if (entityType is Player) {
                    data.add(entityType.name)
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
                Robot(context, "Maxim"),
                Robot(context, "Oxik"),
                Robot(context, "Andrea")
        )
        gameMatrixCell.entityType.add(RobotShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnAliens(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Alien(context, "Maxim"),
                Alien(context, "Oxik"),
                Alien(context, "Andrea")
        )
        gameMatrixCell.entityType.add(AlienShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnPirates(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Pirate(context, "Maxim"),
                Pirate(context, "Oxik"),
                Pirate(context, "Andrea")
        )
        gameMatrixCell.entityType.add(PirateShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnHumans(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Human(context, "Maxim"),
                Human(context, "Oxik"),
                Human(context, "Andrea")
        )
        gameMatrixCell.entityType.add(HumanShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun randomArrow(axis: Axis): Arrow {
        return when (Random().nextInt(Constants.countArrowCells - 1)) {
            0 -> ArrowGreen(context).rotate(axis.x, axis.y, BitmapEditor.RotateAngle.randomAngle())
            else -> ArrowRed(context).rotate(axis.x, axis.y, BitmapEditor.RotateAngle.randomAngle())
        }
    }

    private fun getRandomCell(axis: Axis): CellType {
        return when (Random().nextInt(3)) {
            0 -> Crystal(context, CrystalsEnum.random())
            1 -> randomArrow(axis)
            2 -> randomArrow(axis)
            else -> EmptyCell(context)
        }
    }
}
