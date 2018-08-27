package com.silentgames.silent_planet.model.cells.defaultCell

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 07.07.16.
 */
class GroundDef : Default() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.planet_background)
        super.isCanMove = true
    }
}
