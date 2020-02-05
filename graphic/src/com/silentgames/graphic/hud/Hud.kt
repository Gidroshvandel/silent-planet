package com.silentgames.graphic.hud

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport
import com.silentgames.core.logic.ecs.component.FractionsType
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.InputMultiplexer
import com.silentgames.graphic.mvp.main.EntityData
import ktx.style.get

class Hud(gameViewport: Viewport, private val assets: Assets) {

    private val uiSkin by lazy { assets.uiSkin }

    private val background by lazy { uiSkin.get<Sprite>("ui/bg_space") }

    val stage = Stage(
            object : Viewport() {

                init {
                    camera = OrthographicCamera()
                }

                override fun update(screenWidth: Int, screenHeight: Int, centerCamera: Boolean) {
                    val freeSpaceWidth = screenWidth - gameViewport.screenWidth

                    setScreenBounds(gameViewport.screenWidth, 0, freeSpaceWidth, screenHeight)
                    setWorldSize(freeSpaceWidth.toFloat(), screenHeight.toFloat())
                    apply(centerCamera)
                }
            }
    )

    fun dispose() {
        stage.dispose()
    }

    private val table = Table()

    private val topScorePanel = TopScorePanel(uiSkin)

    private var bottomActionPanelCell: Cell<BottomActionPanel>? = null

    init {
        stage.addActor(
                Table().apply {
                    setFillParent(true)
                    pad(20f, 20f, 0f, 20f)
                    this.top()
                    add(topScorePanel).growX()
                    row().grow()
                    add(ScrollPane(table))
                    row().expandX()
                    bottomActionPanelCell = add(BottomActionPanel(uiSkin)).apply {
                        setVisibleSettings()
                    }
                })
        InputMultiplexer.addProcessor(stage)
    }

    fun drawBackground() {
        stage.batch.begin()
        stage.batch.disableBlending()
        stage.batch.draw(background, 0f, 0f, stage.viewport.worldWidth, stage.viewport.worldHeight)
        stage.batch.enableBlending()
        stage.batch.end()
    }

    fun setBottomActionPanelVisibility(visible: Boolean) {
        if (visible) {
            bottomActionPanelCell?.setVisibleSettings()
        } else {
            bottomActionPanelCell?.setInvisibleSettings()
        }
    }

    fun setCrystalActionButtonEnabled(enabled: Boolean) {
        bottomActionPanelCell?.actor?.setCrystalActionButtonEnabled(enabled)
    }

    fun setSkipTurnButtonEnabled(enabled: Boolean) {
        bottomActionPanelCell?.actor?.setSkipTurnButtonEnabled(enabled)
    }

    fun update(entityList: List<EntityData>, onClick: (EntityData) -> Unit) {
        table.clear()
        entityList.forEach { entityData ->
            table.row().growX()
            table.add(UnitWidget(assets, entityData, onClick)).growX()
        }
    }

    private fun Cell<BottomActionPanel>.setVisibleSettings() {
        this.prefHeight(100f)?.growX()
        this.actor.isVisible = true
    }

    private fun Cell<BottomActionPanel>.setInvisibleSettings() {
        this.size(0f)
        this.actor.isVisible = false
    }

    fun onGetCrystalClick(click: () -> Unit) {
        bottomActionPanelCell?.actor?.onGetCrystalClick = click
    }

    fun onSkipTurnClick(click: () -> Unit) {
        bottomActionPanelCell?.actor?.onSkipTurnClick = click
    }

    fun changeFractionCrystalOnBoard(fractionsType: FractionsType, count: Int) {
        topScorePanel.changeFractionCrystalOnBoard(fractionsType, count)
    }

    fun selectFraction(fractionsType: FractionsType) {
        topScorePanel.selectFraction(fractionsType)
    }

}