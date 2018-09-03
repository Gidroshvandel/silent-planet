package com.silentgames.silent_planet.model

import com.silentgames.silent_planet.model.entities.EntityType

/**
 * Created by gidroshvandel on 28.06.17.
 */
class GameMatrixHelper(
        var gameMatrix: Array<Array<Cell>>,
        var currentXY: Axis = Axis(0, 0),
        var oldXY: Axis? = null,
        var selectedEntity: EntityType? = null,
        var playerName: String? = null,
        var isEventMove: Boolean = false) {

    var gameMatrixCellByOldXY: Cell? = null
        get() = oldXY?.let { gameMatrix[it.x][it.y] }

    var gameMatrixCellByXY: Cell
        get() = gameMatrix[currentXY.x][currentXY.y]
        set(gameMatrixCell) {
            this.gameMatrix[currentXY.x][currentXY.y] = gameMatrixCell
        }


    fun getGameMatrixCell(x: Int, y: Int): Cell {
        return gameMatrix[x][y]
    }

    fun setGameMatrixCell(x: Int, y: Int, gameMatrixCell: Cell) {
        this.gameMatrix[x][y] = gameMatrixCell
    }
}
