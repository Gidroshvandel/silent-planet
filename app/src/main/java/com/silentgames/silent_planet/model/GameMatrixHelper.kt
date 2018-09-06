package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip
import com.silentgames.silent_planet.utils.findSpaceShip

/**
 * Created by gidroshvandel on 28.06.17.
 */
class GameMatrixHelper(
        var gameMatrix: Array<Array<Cell>>,
        var currentXY: Axis = Axis(0, 0),
        var oldXY: Axis? = null,
        var selectedEntity: EntityType? = null,
        var isEventMove: Boolean = false,
        val humanShip: HumanShip = gameMatrix.findSpaceShip()!!,
        val robotShip: RobotShip = gameMatrix.findSpaceShip()!!,
        val pirateShip: PirateShip = gameMatrix.findSpaceShip()!!,
        val alienShip: AlienShip = gameMatrix.findSpaceShip()!!) {

    var gameMatrixCellByOldXY: Cell? = null
        get() = oldXY?.let { gameMatrix[it.x][it.y] }

    var gameMatrixCellByXY: Cell
        get() = gameMatrix[currentXY.x][currentXY.y]
        set(gameMatrixCell) {
            this.gameMatrix[currentXY.x][currentXY.y] = gameMatrixCell
        }
}
