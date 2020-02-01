package com.silentgames.graphic

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.silentgames.core.logic.ecs.Axis

class InputMouse(private val onClick: (Axis) -> Unit) : InputAdapter() {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT || pointer > 0) return false
        println("screenX: " + screenX + " screenY: " + screenY + " pointer: " + pointer)
        onClick(Axis(screenX, screenY))
        return false
    }


}