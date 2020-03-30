package com.silentgames.graphic.screens

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.silentgames.graphic.Assets
import com.silentgames.graphic.screens.menu.MenuScreen

class SilentPlanetGame : Game() {

    private val assets by lazy { Assets() }

    override fun create() {
        setScreen(MenuScreen(Context(assets, this)))
    }

    fun exit() {
        Gdx.app.exit()
    }

}