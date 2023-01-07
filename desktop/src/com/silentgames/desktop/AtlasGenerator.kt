package com.silentgames.desktop

import com.badlogic.gdx.tools.texturepacker.TexturePacker

object AtlasGenerator {

    @JvmStatic
    fun main(args: Array<String>) {
        val settings = TexturePacker.Settings()
        settings.maxWidth = 2048
        settings.maxHeight = 2048
        TexturePacker.process(settings, "android/assets/texture", "android/assets/atlas", "game")
    }
}
