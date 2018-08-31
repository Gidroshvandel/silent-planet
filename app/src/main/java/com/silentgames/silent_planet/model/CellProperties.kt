package com.silentgames.silent_planet.model

import android.graphics.Bitmap

interface CellProperties {
    var bitmap: Bitmap
    var isCanMove: Boolean
    var isCanFly: Boolean
    var isDead: Boolean
    var crystals: Int
}