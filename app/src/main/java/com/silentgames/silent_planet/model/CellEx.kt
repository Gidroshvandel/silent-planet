package com.silentgames.silent_planet.model

import android.graphics.Bitmap


/**
 * Created by gidroshvandel on 09.07.16.
 */
abstract class CellEx {

    var bitmap: Bitmap? = null
    var isCanMove = false
    var isCanFly = false
        protected set
    var isDead = false
        protected set

    protected fun setAll(cellEx: CellEx) {
        bitmap = cellEx.bitmap
        isCanFly = cellEx.isCanFly
        isCanMove = cellEx.isCanMove
        isDead = cellEx.isDead
    }
}
