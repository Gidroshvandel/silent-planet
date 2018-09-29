package com.silentgames.silent_planet.model

import android.content.Context
import android.graphics.Bitmap

interface BaseProperties {
    val context: Context
    var bitmap: Bitmap
    var isCanMove: Boolean
    var isCanFly: Boolean
    var isDead: Boolean
    var crystals: Int
    var name: String
    var description: String
}