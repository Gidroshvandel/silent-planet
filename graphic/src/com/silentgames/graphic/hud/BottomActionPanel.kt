package com.silentgames.graphic.hud

import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener

class BottomActionPanel(
    skin: Skin,
    leftButtonText: String,
    rightButtonText: String
) : Table(skin) {

    var onLeftActionButtonClick: (() -> Unit)? = null
        set(value) {
            leftActionButton.addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    var onRightActionButtonClick: (() -> Unit)? = null
        set(value) {
            rightActionButton.addCaptureListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    value?.invoke()
                }
            })
            field = value
        }

    private val leftActionButton = TextButton(
        leftButtonText,
        skin,
        "custom_button_dark"
    ).also {
        it.pad(20f)
    }

    private val rightActionButton = TextButton(
        rightButtonText,
        skin,
        "custom_button_dark"
    ).also {
        it.pad(20f)
    }

    init {
        pad(10f, 10f, 10f, 10f)
        add(leftActionButton).grow().space(20f)
        add(rightActionButton).grow()
    }

    fun setLeftActionButtonEnabled(enabled: Boolean) {
        leftActionButton.isDisabled = !enabled
    }

    fun setRightTurnButtonEnabled(enabled: Boolean) {
        rightActionButton.isDisabled = !enabled
    }
}
