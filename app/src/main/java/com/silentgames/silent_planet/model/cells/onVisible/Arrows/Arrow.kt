package com.silentgames.silent_planet.model.cells.onVisible.Arrows

import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell
import com.silentgames.silent_planet.utils.BitmapEditor

import java.util.HashMap

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
            if (gameMatrixHelper.gameMatrix!![destinationX][destinationY].cellType!!.default != null && gameMatrixHelper.gameMatrix!![destinationX][destinationY].cellType!!.default!!.javaClass == SpaceDef::class.java || gameMatrixHelper.gameMatrix!![destinationX][destinationY].cellType!!.onVisible!!.javaClass == SpaceCell::class.java) {
                val oldXY = HashMap<String, Int>()
                oldXY["X"] = gameMatrixHelper.x
                oldXY["Y"] = gameMatrixHelper.y
                gameMatrixHelper.oldXY = oldXY
                entityMove.moveOnBoardAllyShip()
            } else {
                val oldXY = HashMap<String, Int>()
                oldXY["X"] = gameMatrixHelper.x
                oldXY["Y"] = gameMatrixHelper.y
                gameMatrixHelper.oldXY = oldXY
                gameMatrixHelper.x = destinationX
                gameMatrixHelper.y = destinationY
                gameMatrixHelper.isEventMove = true
                entityMove.movePlayer()
            }
        } else {
            val oldXY = HashMap<String, Int>()
            oldXY["X"] = gameMatrixHelper.x
            oldXY["Y"] = gameMatrixHelper.y
            gameMatrixHelper.oldXY = oldXY
            entityMove.moveOnBoardAllyShip()
        }
        return gameMatrixHelper
    }

    private fun checkBorders(): Boolean {
        return destinationX <= Constants.getVerticalCountOfCells() &&
                destinationX >= 0 &&
                destinationY <= Constants.getHorizontalCountOfCells() &&
                destinationY >= 0
    }
}
