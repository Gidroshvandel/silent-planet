package com.silentgames.silent_planet.model.cells.Arrow

import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.cells.SpaceCell
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
abstract class Arrow(
        var rotateAngle: BitmapEditor.RotateAngle,
        override var isCanMove: Boolean = true,
        var destinationX: Int = 0,
        var destinationY: Int = 0,
        var distance: Int = 0,
        description: String = ""
) : CellType(
        name = App.getContext().getString(R.string.arrow_cell_name),
        description = description
) {

    open fun rotate(x: Int, y: Int, rotateAngle: BitmapEditor.RotateAngle): Arrow {
        when (rotateAngle) {
            BitmapEditor.RotateAngle.DEGREES0 -> {
                destinationX = x + distance
                destinationY = y - distance
            }
            BitmapEditor.RotateAngle.DEGREES90 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90, bitmap)
                destinationX = x + distance
                destinationY = y + distance
            }
            BitmapEditor.RotateAngle.DEGREES180 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180, bitmap)
                destinationX = x - distance
                destinationY = y + distance
            }
            BitmapEditor.RotateAngle.DEGREES270 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270, bitmap)
                destinationX = x - distance
                destinationY = y - distance
            }
        }
        this.rotateAngle = rotateAngle
        return this
    }

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        val entityMove = EntityMove(gameMatrixHelper)
        val player = gameMatrixHelper.selectedEntity as Player
        if (checkBorders()) {
            if (gameMatrixHelper.gameMatrix[destinationX][destinationY].cellType is SpaceCell) {
                gameMatrixHelper.oldXY = gameMatrixHelper.currentXY
                entityMove.moveOnBoardAllyShip(player)
            } else {
                gameMatrixHelper.oldXY = gameMatrixHelper.currentXY
                gameMatrixHelper.currentXY = Axis(destinationX, destinationY)
                gameMatrixHelper.isEventMove = true
                entityMove.movePlayer(player)
            }
        } else {
            gameMatrixHelper.oldXY = gameMatrixHelper.currentXY
            entityMove.moveOnBoardAllyShip(player)
        }
        return gameMatrixHelper
    }

    private fun checkBorders(): Boolean {
        return destinationX <= Constants.verticalCountOfCells &&
                destinationX >= 0 &&
                destinationY <= Constants.horizontalCountOfCells &&
                destinationY >= 0
    }
}
