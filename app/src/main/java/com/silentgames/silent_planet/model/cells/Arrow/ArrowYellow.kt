package com.silentgames.silent_planet.model.cells.Arrow

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 28.08.2017.
 */

class ArrowYellow(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell)
) : Arrow(rotateAngle = BitmapEditor.RotateAngle.DEGREES0, distance = 2)
