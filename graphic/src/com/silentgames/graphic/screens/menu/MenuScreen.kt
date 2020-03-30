package com.silentgames.graphic.screens.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.screens.AppScreenAdapter
import com.silentgames.graphic.screens.Context

class MenuScreen(context: Context) : AppScreenAdapter(context) {

    private val stage = Stage()

    private val startGameButton = createTextButton("Начать")
    private val exitButton = createTextButton("Выход")

    init {
        stage.addActor(
                Table().apply {
                    setFillParent(true)
                    pad(20f, 20f, 0f, 20f)
                    this.center()
                    add(startGameButton).growX()
                    row()
                    add(exitButton).growX()
                })

        startGameButton.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {

            }
        })

        exitButton.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                context.game.exit()
            }
        })

        InputMultiplexer.addProcessor(stage)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.viewport.apply(true)

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)

        stage.act()
        stage.draw()
    }

    private fun createTextButton(text: String) = TextButton(
            text,
            context.assets.uiSkin,
            "rounded_window"
    )

}