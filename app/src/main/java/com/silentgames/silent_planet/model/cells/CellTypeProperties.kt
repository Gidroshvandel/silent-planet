package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.model.CellProperties

/**
 * Created by gidroshvandel on 27.09.16.
 */
interface CellTypeProperties : CellProperties {

    var closeBitmap: Bitmap
    var isVisible: Boolean

}
