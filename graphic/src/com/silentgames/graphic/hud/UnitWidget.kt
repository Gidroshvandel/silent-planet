package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.silentgames.core.Strings
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.game.EntityData
import com.silentgames.graphic.mvp.game.GameScreen
import com.silentgames.graphic.scaleImageForBoard
import com.silentgames.graphic.setColor

class UnitWidget(
        val assets: Assets,
        entityData: EntityData,
        onClick: (EntityData) -> Unit
) : Button(assets.uiSkin) {

    init {
        pad(30f, 35f, 38f, 35f)
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

            if (entityData.captured) {
                add(getCapturedDescriptionTable(entityData.description)).prefWidth(150f).growX().space(5f)
                add(getBuyBackCrystalsTable(entityData.crystalCount)).space(5f)
            } else {
                add(getDescriptionLabel(entityData.description)).prefWidth(150f).growX().space(5f)
                if (entityData.crystalCount.toInt() > 0) {
                    add(getEntityCrystalsTable(entityData.crystalCount)).space(5f)
                }
            }

        }
    }

    private fun getCapturedDescriptionTable(description: String) =
            Table().also {
                it.row()
                it.add(
                        getDescriptionLabel(Strings.captive.getString()).also { label ->
                            label.setColor(Assets.TextColor.RED, skin)
                        }
                ).pad(2f).center()
                it.row()
                it.add(getDescriptionLabel(description)).prefWidth(150f).growX().space(5f)
            }

    private fun getDescriptionLabel(description: String) = Label(description, skin).also {
        it.setWrap(true)
        it.setAlignment(Align.center)
    }

    private fun getEntityCrystalsTable(crystalCount: String) =
            Table().also {
                it.row()
                it.add(Image().apply { setTexture("crystal") }).pad(2f).center()
                it.row()
                it.add(
                        Label(crystalCount, skin).also { label ->
                            label.setAlignment(Align.center)
                        }
                ).pad(2f).center()
            }

    private fun getBuyBackCrystalsTable(crystalCount: String) =
            Table().also {
                it.add(
                        Label(Strings.buyout.getString(), skin).also { label ->
                            label.setAlignment(Align.center)
                            label.setColor(Assets.TextColor.RED, skin)
                        }
                ).pad(2f).center()
                it.row()
                it.add(Image().apply { setTexture("crystal") }).pad(2f).center()
                it.row()
                it.add(
                        Label(crystalCount, skin).also { label ->
                            label.setAlignment(Align.center)
                        }
                ).pad(2f).center()
            }

    private fun Image.setTexture(path: String) {
        val sprite = assets.getSprite(path)
        val size = scaleImageForBoard(sprite.width, sprite.height, GameScreen.HEIGHT * 1.2f)
        sprite.setSize(size.x, size.y)
        this.drawable = SpriteDrawable(sprite)
    }

}