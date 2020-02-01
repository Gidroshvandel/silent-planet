package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Align
import com.silentgames.core.logic.Constants
import com.silentgames.graphic.Assets
import com.silentgames.graphic.mvp.main.EntityData
import com.silentgames.graphic.mvp.main.SilentPlanetGame

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
                it.add(Image().apply { setTexture(entityData.texture) }).pad(5f)
                it.row()
                it.add(
                        Label(entityData.name, skin).also { label ->
                            label.setAlignment(Align.center)
                        }
                ).pad(5f)
            }).space(5f)
            add(
                    Label(entityData.description, skin).also {
                        it.setWrap(true)
                        it.setAlignment(Align.center)
                    }).prefWidth(150f).growX().space(5f)
            add(Image().apply { setTexture("crystal.png") }).space(5f).center()
        }
    }

    private fun Image.setTexture(path: String) {
        val size = (SilentPlanetGame.HEIGHT / Constants.verticalCountOfCells)
        val sprite = assets.getSprite(path)
        sprite.setSize(size, size)
        this.drawable = SpriteDrawable(sprite)
    }

}