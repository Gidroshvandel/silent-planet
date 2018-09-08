package com.silentgames.silent_planet.model.cells.Arrow

import android.graphics.Bitmap
import com.silentgames.silent_planet.App
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.12.16.
 */
class ArrowGreen(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell)
) : Arrow(
        distance = 1,
        rotateAngle = BitmapEditor.RotateAngle.DEGREES0,
        description = App.getContext().getString(R.string.arrow_green_cell_description)
)
