package com.silentgames.silent_planet.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object TextureLoader {

    fun load(context: Context, resourceName: String): Bitmap {
        return BitmapFactory.decodeStream(context.assets.open("image/$resourceName"))
    }
}
