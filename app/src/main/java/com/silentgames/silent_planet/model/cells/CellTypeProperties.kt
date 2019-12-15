package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.model.BaseProperties

/**
 * Created by gidroshvandel on 27.09.16.
 */
interface CellTypeProperties : BaseProperties {

    val position: Axis
    val closeBitmap: Bitmap
    var isVisible: Boolean

}
