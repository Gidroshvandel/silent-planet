package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 28.08.2017.
 */

class ArrowYellow(
        context: Context,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.arrow_green_cell)
) : Arrow(context = context, rotateAngle = BitmapEditor.RotateAngle.DEGREES0, distance = 2)
