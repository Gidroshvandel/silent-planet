package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.logic.moveOnBoardAllyShip
import com.silentgames.silent_planet.logic.tryMovePlayer
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.Event
import com.silentgames.silent_planet.model.GameMatrix
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.ground.Player
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.model.getCell
import com.silentgames.silent_planet.utils.BitmapEditor
import com.silentgames.silent_planet.utils.getSpaceShip

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

    val destination: Axis get() = Axis(destinationX, destinationY)

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

    override fun doEvent(event: Event): Boolean {
        if (event.entity is Player) {
            if (checkBorders() && event.gameMatrix.isCyclicMove()) {
                event.gameMatrix.getCell(destination).cellType.isVisible = true
                event.gameMatrix.moveOnBoardAllyShip(event.entity)
            } else if (checkGroundBorders()) {
                event.gameMatrix.tryMovePlayer(destination, event.entity)
            } else {
                event.gameMatrix.moveOnBoardAllyShip(event.entity)
            }
            return true
        }
        return false
    }

    private fun GameMatrix.isCyclicMove(): Boolean {
        val destinationCell = this.getCell(destination).cellType
        return destinationCell is Arrow && destinationCell.destination == position
    }

    fun getDestination(gameMatrix: GameMatrix, fractionsType: FractionsType): Axis? =
            if (checkBorders() && checkGroundBorders()
                    || checkBorders() && gameMatrix.isSpaceShipFraction(fractionsType)) {
                destination
            } else {
                null
            }

    private fun GameMatrix.isSpaceShipFraction(fractionsType: FractionsType) =
            this.getCell(destination).entityType.getSpaceShip()?.fraction?.fractionsType == fractionsType

    private fun checkGroundBorders(): Boolean {
        return destination.x <= Constants.verticalCountOfGroundCells &&
                destination.x >= 1 &&
                destination.y <= Constants.horizontalCountOfGroundCells &&
                destination.y >= 1
    }

    private fun checkBorders(): Boolean {
        return destination.x < Constants.verticalCountOfCells &&
                destination.x >= 0 &&
                destination.y < Constants.horizontalCountOfCells &&
                destination.y >= 0
    }
}
