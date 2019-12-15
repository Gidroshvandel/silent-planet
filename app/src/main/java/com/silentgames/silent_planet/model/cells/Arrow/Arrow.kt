package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.Entity
import com.silentgames.silent_planet.logic.moveOnBoardAllyShip
import com.silentgames.silent_planet.logic.tryMovePlayer
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
abstract class Arrow(
        context: Context,
        var rotateAngle: BitmapEditor.RotateAngle,
        override var isCanMove: Boolean = true,
        var destinationX: Int = 0,
        var destinationY: Int = 0,
        var distance: Int = 0,
        description: String = ""
) : CellType(
        context = context,
        name = context.getString(R.string.arrow_cell_name),
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
        val current = gameMatrixHelper.currentXY
        val player = gameMatrixHelper.selectedEntity as Player
        val target = Axis(destinationX, destinationY)
        if (checkBorders()) {
            gameMatrixHelper.gameMatrix.tryMovePlayer(target, Entity(player, current))
        } else {
            gameMatrixHelper.gameMatrix.moveOnBoardAllyShip(Entity(player, current))
        }
        return gameMatrixHelper
    }

    private fun checkBorders(): Boolean {
        return destinationX <= Constants.verticalCountOfGroundCells &&
                destinationX >= 1 &&
                destinationY <= Constants.horizontalCountOfGroundCells &&
                destinationY >= 1
    }
}
