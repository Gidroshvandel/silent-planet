package com.silentgames.silent_planet.model.cells.onVisible.Arrow

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
abstract class Arrow : OnVisible() {

    var rotateAngle: BitmapEditor.RotateAngle? = null
    var destinationX: Int = 0
    var destinationY: Int = 0
    var distance: Int = 0

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
        if (checkBorders()) {
            if (gameMatrixHelper.gameMatrix[destinationX][destinationY].cellType.default != null
                    && gameMatrixHelper.gameMatrix[destinationX][destinationY].cellType.default!!.javaClass == SpaceDef::class.java
                    || gameMatrixHelper.gameMatrix[destinationX][destinationY].cellType.onVisible!!.javaClass == SpaceCell::class.java) {
                gameMatrixHelper.oldXY = gameMatrixHelper.currentXY
                entityMove.moveOnBoardAllyShip()
            } else {
                gameMatrixHelper.oldXY = gameMatrixHelper.currentXY
                gameMatrixHelper.currentXY = Axis(destinationX, destinationY)
                gameMatrixHelper.isEventMove = true
                entityMove.movePlayer()
            }
        } else {
            gameMatrixHelper.oldXY = gameMatrixHelper.currentXY
            entityMove.moveOnBoardAllyShip()
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
