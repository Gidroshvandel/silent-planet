package com.silentgames.silent_planet.model

/**
 * Created by gidroshvandel on 28.06.17.
 */
class GameMatrixHelper(
        var gameMatrix: Array<Array<Cell>>,
        var x: Int = 0,
        var y: Int = 0,
        var oldXY: Axis? = null,
        var playerName: String? = null,
        var isEventMove: Boolean = false) {

    var gameMatrixCellByOldXY: Cell? = null
        get() = oldXY?.let { gameMatrix[it.x][it.y] }

    var gameMatrixCellByXY: Cell
        get() = gameMatrix[x][y]
        set(gameMatrixCell) {
            this.gameMatrix[x][y] = gameMatrixCell
        }


    fun getGameMatrixCell(x: Int, y: Int): Cell {
        return gameMatrix[x][y]
    }

    fun setGameMatrixCell(x: Int, y: Int, gameMatrixCell: Cell) {
        this.gameMatrix[x][y] = gameMatrixCell
    }
}
