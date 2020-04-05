package com.silentgames.graphic.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.silentgames.core.Strings
import com.silentgames.graphic.hud.BottomActionPanel
import com.silentgames.graphic.hud.LoadedData
import com.silentgames.graphic.hud.LoadedDataWidget
import com.silentgames.graphic.manager.game.GameManager
import com.silentgames.graphic.manager.game.GameSlot
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.mvp.game.GameScreen
import com.silentgames.graphic.screens.base.AppScreenAdapter
import com.silentgames.graphic.screens.base.Context

class GameManageScreen(context: Context) : AppScreenAdapter(context) {

    private val stage = Stage(ScreenViewport())

    private val bottomActionPanel = BottomActionPanel(context.assets.uiSkin, Strings.back.getString(), Strings.new_game.getString())

    private var selectedSlot: GameSlot? = null

    override fun hide() {
        stage.dispose()
    }

    override fun show() {
        stage.addActor(
                Table().apply {
                    setFillParent(true)
                    pad(20f, 20f, 10f, 20f)
                    top()
                    add(ScrollPane(getLoadedTable())).prefWidth(800f)
                    row()
                    add(bottomActionPanel).apply {
                        bottom()
                    }
                })

        bottomActionPanel.onLeftActionButtonClick = {
            context.game.screen = MenuScreen(context)
        }

        bottomActionPanel.onRightActionButtonClick = {
            selectedSlot?.number?.let { slotNumber ->
                GameManager.deleteSlot(slotNumber)
                context.game.screen = GameScreen(context, slotNumber)
            }
        }
        bottomActionPanel.setRightTurnButtonEnabled(false)

        InputMultiplexer.addProcessor(stage)
    }

    private fun getLoadedTable(): Table {
        val buttonGroup = ButtonGroup<LoadedDataWidget>()
        buttonGroup.setMaxCheckCount(1)
        buttonGroup.setMinCheckCount(0)
        return Table().apply {
            for (i in 1..5) {
                val data = GameManager.loadData()?.getSlot(i) ?: GameSlot(i)
                row().growX()
                val widget = LoadedDataWidget(
                        context.assets,
                        LoadedData(data.toSlotName(i))
                ) { _, checked ->
                    if (!checked) {
                        bottomActionPanel.setRightTurnButtonEnabled(true)
                        selectedSlot = data
                    }
                }
                buttonGroup.add(widget)
                add(widget).growX()
            }
        }
    }

    private fun GameSlot?.toSlotName(slotNumber: Int) =
            if (this == null || this.gameState == null)
                Strings.empty_slot.getString(slotNumber)
            else
                Strings.save_slot.getString(slotNumber)

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        stage.viewport.apply(true)

        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.setScreenSize(width, height)
        stage.viewport.update(width, height)

        stage.act()
        stage.draw()
    }

}