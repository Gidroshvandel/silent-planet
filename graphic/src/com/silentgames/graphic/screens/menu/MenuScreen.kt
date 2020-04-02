package com.silentgames.graphic.screens.menu

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.screens.LoadManageScreen
import com.silentgames.graphic.screens.base.AppScreenAdapter
import com.silentgames.graphic.screens.base.Context

class MenuScreen(context: Context) : AppScreenAdapter(context) {

    private val stage = Stage(ScreenViewport())

    private val startGameButton = createTextButton("Начать")
    private val loadButton = createTextButton("Загрузка")
    private val exitButton = createTextButton("Выход")

    override fun hide() {
        InputMultiplexer.removeProcessor(stage)
        stage.dispose()
    }

    override fun show() {
        stage.addActor(
                Table().apply {
                    setFillParent(true)
                    pad(20f, 20f, 20f, 20f)
                    center()
                    add(startGameButton).width(400f)
                    row()
                    add(loadButton).width(400f)
                    row()
                    add(exitButton).width(400f)
                })

        startGameButton.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
//                context.game.screen = GameScreen(context)
            }
        })

        loadButton.addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                context.game.screen = LoadManageScreen(context)
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