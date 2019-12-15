package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 28.08.2017.
 */

class ArrowRed(
        context: Context,
        override val position: Axis,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.arow_red_cell)
) : Arrow(
        context = context,
        distance = 3,
        rotateAngle = BitmapEditor.RotateAngle.DEGREES0,
        description = context.getString(R.string.arrow_red_cell_description)
) {

    override fun rotate(rotateAngle: BitmapEditor.RotateAngle): ArrowRed {
        when (rotateAngle) {
            BitmapEditor.RotateAngle.DEGREES0 -> {
                destinationX = position.x
                destinationY = position.y - distance
            }
            BitmapEditor.RotateAngle.DEGREES90 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90, bitmap)
                destinationX = position.x + distance
                destinationY = position.y
            }
            BitmapEditor.RotateAngle.DEGREES180 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180, bitmap)
                destinationX = position.x
                destinationY = position.y + distance
            }
            BitmapEditor.RotateAngle.DEGREES270 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270, bitmap)
                destinationX = position.x - distance
                destinationY = position.y
            }
        }
        this.rotateAngle = rotateAngle
        return this
    }
}
