package com.silentgames.silent_planet.logic

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.entities.ground.fractions.Alien
import com.silentgames.silent_planet.model.entities.ground.fractions.Human
import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate
import com.silentgames.silent_planet.model.entities.ground.fractions.Robot
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip
import com.silentgames.silent_planet.model.fractions.FractionsType
import java.util.*

class EntityRandomGenerator(val context: Context) {

    fun spawnShips(gameMatrix: Array<Array<Cell>>) {
        val axisList = BoardSide.values().toList().shuffled().map {
            val borderInset = 1
            when (it) {
                BoardSide.TOP, BoardSide.BOTTOM -> it.getAxis(Random().nextInt(
                        borderInset,
                        Constants.horizontalCountOfCells - borderInset
                ))
                BoardSide.RIGHT, BoardSide.LEFT -> it.getAxis(Random().nextInt(
                        borderInset,
                        Constants.horizontalCountOfCells - borderInset
                ))
            }
        }
        FractionsType.values().forEach {
            when (it) {
                FractionsType.HUMAN -> spawnHumans(gameMatrix[axisList[0].x][axisList[0].y])
                FractionsType.PIRATE -> spawnPirates(gameMatrix[axisList[1].x][axisList[1].y])
                FractionsType.ROBOT -> spawnRobots(gameMatrix[axisList[2].x][axisList[2].y])
                FractionsType.ALIEN -> spawnAliens(gameMatrix[axisList[3].x][axisList[3].y])
            }
        }
    }

    private fun spawnRobots(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Robot(context, context.getString(R.string.robot_player_name_one)),
                Robot(context, context.getString(R.string.robot_player_name_two)),
                Robot(context, context.getString(R.string.robot_player_name_three))
        )
        gameMatrixCell.entityType.add(RobotShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnAliens(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Alien(context, context.getString(R.string.alien_player_name_one)),
                Alien(context, context.getString(R.string.alien_player_name_two)),
                Alien(context, context.getString(R.string.alien_player_name_three))
        )
        gameMatrixCell.entityType.add(AlienShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnPirates(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Pirate(context, context.getString(R.string.pirate_player_name_one)),
                Pirate(context, context.getString(R.string.pirate_player_name_two)),
                Pirate(context, context.getString(R.string.pirate_player_name_three))
        )
        gameMatrixCell.entityType.add(PirateShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private fun spawnHumans(gameMatrixCell: Cell) {
        val playerList = mutableListOf(
                Human(context, context.getString(R.string.human_player_name_one)),
                Human(context, context.getString(R.string.human_player_name_two)),
                Human(context, context.getString(R.string.human_player_name_three))
        )
        gameMatrixCell.entityType.add(HumanShip(context).apply {
            playersOnBord = playerList.toMutableList()
        })
    }

    private enum class BoardSide {
        TOP {
            override fun getAxis(linePosition: Int) = Axis(linePosition, 0)
        },
        BOTTOM {
            override fun getAxis(linePosition: Int) = Axis(linePosition, Constants.verticalCountOfCells - 1)
        },
        LEFT {
            override fun getAxis(linePosition: Int) = Axis(0, linePosition)
        },
        RIGHT {
            override fun getAxis(linePosition: Int) = Axis(Constants.horizontalCountOfCells - 1, linePosition)
        };

        abstract fun getAxis(linePosition: Int): Axis
    }

    private fun Random.nextInt(startValue: Int, endValue: Int) = nextInt(endValue - startValue) + startValue

}