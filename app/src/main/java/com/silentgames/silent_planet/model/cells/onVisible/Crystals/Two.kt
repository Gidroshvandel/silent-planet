package com.silentgames.silent_planet.model.cells.onVisible.Crystals

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by Lantiets on 29.08.2017.
 */

class Two : Crystals() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.two_crystals)
        super.isCanMove = true
        super.crystals = 2
    }
}
