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
import com.silentgames.graphic.mvp.main.EntityData
import com.silentgames.graphic.mvp.main.SilentPlanetGame
import com.silentgames.graphic.scaleImageForBoard

class UnitWidget(
        val assets: Assets,
        entityData: EntityData,
        onClick: (EntityData) -> Unit
) : Button(assets.uiSkin) {

    init {
        pad(15f, 35f, 15f, 45f)
        addWidget(entityData)
        addCaptureListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                onClick(entityData)
            }
        })
    }

    private fun Table.addWidget(entityData: EntityData) {
        this.apply {

            add(Table().also {
                it.row()
                it.add(Image().apply { setTexture(entityData.texture) }).pad(5f).center()
                it.row()
                it.add(
                        Label(entityData.name, skin).also { label ->
                            label.setAlignment(Align.center)
                        }
                ).pad(5f).center()
            }).space(5f).center()

            add(
                    Label(entityData.description, skin).also {
                        it.setWrap(true)
                        it.setAlignment(Align.center)
                    }).prefWidth(150f).growX().space(5f)

            if (entityData.crystalCount.toInt() > 0) {
                add(Table().also {
                    it.row()
                    it.add(Image().apply { setTexture("crystal") }).pad(2f).center()
                    it.row()
                    it.add(
                            Label(entityData.crystalCount, skin).also { label ->
                                label.setAlignment(Align.center)
                            }
                    ).pad(2f).center()
                }).space(5f)
            }

        }
    }

    private fun Image.setTexture(path: String) {
        val sprite = assets.getSprite(path)
        val size = scaleImageForBoard(sprite.width, sprite.height, SilentPlanetGame.HEIGHT * 1.2f)
        sprite.setSize(size.x, size.y)
        this.drawable = SpriteDrawable(sprite)
    }

}