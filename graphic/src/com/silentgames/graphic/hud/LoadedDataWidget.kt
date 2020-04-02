package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.game.GameScreen
import com.silentgames.graphic.scaleImageForBoard

class LoadedDataWidget(
        val assets: Assets,
        loadedData: LoadedData,
        onClick: (LoadedData, Boolean) -> Unit
) : Button(assets.uiSkin, "custom_checked_window") {

    init {
        pad(30f, 35f, 38f, 35f)
        addWidget(loadedData)
        addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onClick(loadedData, isChecked)
            }
        })
    }

    private fun Table.addWidget(loadedData: LoadedData) {
        this.apply {
            add(Table().also {
                it.row()
                it.add(Image().apply { setTexture(loadedData.texture) }).pad(10f).center()
            }).space(5f).center()
            add(getCapturedDescriptionTable(loadedData.name, loadedData.date)).prefWidth(150f).growX().space(5f)
        }
    }

    private fun getCapturedDescriptionTable(description: String, date: String) =
            Table().also {
                it.row()
                it.add(getDescriptionLabel(description)).prefWidth(150f).growX().space(5f)
                if (date.isNotEmpty()) {
                    it.row()
                    it.add(getDescriptionLabel(date)).prefWidth(150f).growX().space(5f)
                }
            }

    private fun getDescriptionLabel(description: String) = Label(description, skin).also {
        it.setWrap(true)
        it.setAlignment(Align.center)
    }

    private fun Image.setTexture(path: String) {
        val sprite = assets.getSprite(path)
        val size = scaleImageForBoard(sprite.width, sprite.height, GameScreen.HEIGHT * 1.2f)
        sprite.setSize(size.x, size.y)
        this.drawable = SpriteDrawable(sprite)
    }

}

class LoadedData(
        val name: String,
        val date: String = "",
        val texture: String = "ui/ic_diskette"
)