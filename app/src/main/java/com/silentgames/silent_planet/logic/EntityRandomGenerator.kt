package com.silentgames.silent_planet.logic

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.ecs.Axis
import com.silentgames.silent_planet.logic.ecs.component.Transport
import com.silentgames.silent_planet.logic.ecs.entity.unit.ground.AlienPlayer
import com.silentgames.silent_planet.logic.ecs.entity.unit.ground.HumanPlayer
import com.silentgames.silent_planet.logic.ecs.entity.unit.ground.PiratePlayer
import com.silentgames.silent_planet.logic.ecs.entity.unit.ground.RobotPlayer
import com.silentgames.silent_planet.logic.ecs.entity.unit.space.*
import java.util.*

class EntityRandomGenerator(val context: Context) {

    fun generateShips(): List<SpaceShip> {
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

        return listOf(
                    spawnHumans(axisList[0]),
                    spawnPirates(axisList[1]),
                    spawnRobots(axisList[2]),
                    spawnAliens(axisList[3])
            )
        }

    private fun spawnRobots(axis: Axis) =
            RobotShip(context, axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        RobotPlayer(context, context.getString(R.string.robot_player_name_one), axis),
                                        RobotPlayer(context, context.getString(R.string.robot_player_name_two), axis),
                                        RobotPlayer(context, context.getString(R.string.robot_player_name_three), axis)
                                )
                        )
                )
            }

    private fun spawnAliens(axis: Axis) =
            AlienShip(context, axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        AlienPlayer(context, context.getString(R.string.alien_player_name_one), axis),
                                        AlienPlayer(context, context.getString(R.string.alien_player_name_two), axis),
                                        AlienPlayer(context, context.getString(R.string.alien_player_name_three), axis)
                                )
                        )
                )
            }

    private fun spawnPirates(axis: Axis) =
            PirateShip(context, axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        PiratePlayer(context, context.getString(R.string.pirate_player_name_one), axis),
                                        PiratePlayer(context, context.getString(R.string.pirate_player_name_two), axis),
                                        PiratePlayer(context, context.getString(R.string.pirate_player_name_three), axis)
                                )
                        )
                )
            }

    private fun spawnHumans(axis: Axis) =
            HumanShip(context, axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        HumanPlayer(context, context.getString(R.string.human_player_name_one), axis),
                                        HumanPlayer(context, context.getString(R.string.human_player_name_two), axis),
                                        HumanPlayer(context, context.getString(R.string.human_player_name_three), axis)
                                )
                        )
                )
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