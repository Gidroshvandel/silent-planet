package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 28.08.2017.
 */

class ArrowRed(
        context: Context,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.arow_red_cell)
) : Arrow(
        context = context,
        distance = 3,
        rotateAngle = BitmapEditor.RotateAngle.DEGREES0,
        description = context.getString(R.string.arrow_red_cell_description)
) {

    override fun rotate(x: Int, y: Int, rotateAngle: BitmapEditor.RotateAngle): ArrowRed {
        when (rotateAngle) {
            BitmapEditor.RotateAngle.DEGREES0 -> {
                destinationX = x
                destinationY = y - distance
            }
            BitmapEditor.RotateAngle.DEGREES90 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90, bitmap)
                destinationX = x + distance
                destinationY = y
            }
            BitmapEditor.RotateAngle.DEGREES180 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180, bitmap)
                destinationX = x
                destinationY = y + distance
            }
            BitmapEditor.RotateAngle.DEGREES270 -> {
                bitmap = BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270, bitmap)
                destinationX = x - distance
                destinationY = y
            }
        }
        this.rotateAngle = rotateAngle
        return this
    }
}