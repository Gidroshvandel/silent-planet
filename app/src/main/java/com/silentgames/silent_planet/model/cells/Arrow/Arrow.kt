package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.Entity
import com.silentgames.silent_planet.logic.moveOnBoardAllyShip
import com.silentgames.silent_planet.logic.tryMovePlayer
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Event
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

    open fun rotate(rotateAngle: BitmapEditor.RotateAngle): Arrow {
        when (rotateAngle) {
            BitmapEditor.RotateAngle.DEGREES0 -> {
                destinationX = position.x + distance
                destinationY = position.y - distance
            }
            BitmapEditor.RotateAngle.DEGREES90 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90, bitmap)
                destinationX = position.x + distance
                destinationY = position.y + distance
            }
            BitmapEditor.RotateAngle.DEGREES180 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180, bitmap)
                destinationX = position.x - distance
                destinationY = position.y + distance
            }
            BitmapEditor.RotateAngle.DEGREES270 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270, bitmap)
                destinationX = position.x - distance
                destinationY = position.y - distance
            }
        }
        this.rotateAngle = rotateAngle
        return this
    }

    override fun doEvent(event: Event) {
        if (event.entity is Player) {
            if (checkBorders()) {
                event.gameMatrix.tryMovePlayer(Axis(destinationX, destinationY), Entity(event.entity, position))
            } else {
                event.gameMatrix.moveOnBoardAllyShip(Entity(event.entity, position))
            }
        }
    }

    private fun checkBorders(): Boolean {
        return destinationX <= Constants.verticalCountOfGroundCells &&
                destinationX >= 1 &&
                destinationY <= Constants.horizontalCountOfGroundCells &&
                destinationY >= 1
    }
}
