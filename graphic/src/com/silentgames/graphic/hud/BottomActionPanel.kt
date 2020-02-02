package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.silentgames.core.Strings

class BottomActionPanel(skin: Skin) : Table(skin) {

    var onGetCrystalClick: (() -> Unit)? = null
        set(value) {
            addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    private val crystalActionButton = TextButton(
            Strings.get_crystal_action.getString(),
            skin,
            "custom_button_dark"
    ).also {
        it.pad(20f)
    }

    init {
        pad(10f, 10f, 10f, 10f)
        add(crystalActionButton).grow()
    }

    fun setCrystalActionButtonEnabled(enabled: Boolean) {
        crystalActionButton.isDisabled = !enabled
    }

}