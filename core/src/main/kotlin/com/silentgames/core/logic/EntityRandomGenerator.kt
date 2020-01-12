package com.silentgames.core.logic


import com.silentgames.core.Strings
import com.silentgames.core.logic.ecs.Axis
import com.silentgames.core.logic.ecs.component.Transport
import com.silentgames.core.logic.ecs.entity.unit.ground.AlienPlayer
import com.silentgames.core.logic.ecs.entity.unit.ground.HumanPlayer
import com.silentgames.core.logic.ecs.entity.unit.ground.PiratePlayer
import com.silentgames.core.logic.ecs.entity.unit.ground.RobotPlayer
import com.silentgames.core.logic.ecs.entity.unit.space.*
import java.util.*

class EntityRandomGenerator {

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
            RobotShip(axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        RobotPlayer(Strings.robot_player_name_one.getString(), axis),
                                        RobotPlayer(Strings.robot_player_name_two.getString(), axis),
                                        RobotPlayer(Strings.robot_player_name_three.getString(), axis)
                                )
                        )
                )
            }

    private fun spawnAliens(axis: Axis) =
            AlienShip(axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        AlienPlayer(Strings.alien_player_name_one.getString(), axis),
                                        AlienPlayer(Strings.alien_player_name_two.getString(), axis),
                                        AlienPlayer(Strings.alien_player_name_three.getString(), axis)
                                )
                        )
                )
            }

    private fun spawnPirates(axis: Axis) =
            PirateShip(axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        PiratePlayer(Strings.pirate_player_name_one.getString(), axis),
                                        PiratePlayer(Strings.pirate_player_name_two.getString(), axis),
                                        PiratePlayer(Strings.pirate_player_name_three.getString(), axis)
                                )
                        )
                )
            }

    private fun spawnHumans(axis: Axis) =
            HumanShip(axis).apply {
                addComponent(
                        Transport(
                                this.getMandatoryComponent(),
                                mutableListOf(
                                        HumanPlayer(Strings.human_player_name_one.getString(), axis),
                                        HumanPlayer(Strings.human_player_name_two.getString(), axis),
                                        HumanPlayer(Strings.human_player_name_three.getString(), axis)
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