package com.silentgames.silent_planet.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


object TextureLoader {

    fun load(context: Context, resourceName: String): Bitmap {
        //todo fix this boolshit
        if (resourceName == "space_texture" || resourceName == "planet_background") {
            return BitmapFactory.decodeStream(context.assets.open("image/$resourceName.jpg"))
        } else {
            return BitmapFactory.decodeStream(context.assets.open("image/$resourceName.png"))
        }
    }

}