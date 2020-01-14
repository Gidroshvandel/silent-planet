package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.Texture


object TextureLoader {

    fun load(resourceName: String): Texture {
        return Texture(resourceName)
    }

}