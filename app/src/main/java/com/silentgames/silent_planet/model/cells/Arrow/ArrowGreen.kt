package com.silentgames.silent_planet.model.cells.Arrow

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
class ArrowGreen(
        context: Context,
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.arrow_green_cell)
) : Arrow(
        context = context,
        distance = 1,
        rotateAngle = BitmapEditor.RotateAngle.DEGREES0,
        description = context.getString(R.string.arrow_green_cell_description)
)
