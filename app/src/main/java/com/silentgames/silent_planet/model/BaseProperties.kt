package com.silentgames.silent_planet.model

import android.graphics.Bitmap

interface BaseProperties {
    var bitmap: Bitmap
    var isCanMove: Boolean
    var isCanFly: Boolean
    var isDead: Boolean
    var crystals: Int
    var name: String
    var description: String
}