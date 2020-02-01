package com.silentgames.graphic

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.Axis

class InputMouse(private val camera: Camera, private val onClick: (Axis) -> Unit) : InputAdapter() {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT || pointer > 0) return false
        val vector = camera.unProject(Vector2(screenX.toFloat(), screenY.toFloat()))
        val x = ((Constants.horizontalCountOfCells) * vector.x / camera.viewportWidth).toInt()
        val y = ((Constants.verticalCountOfCells) * vector.y / camera.viewportHeight).toInt()
        val axis = Axis(x, y)
        Logger.logDebug(
                "InputMouse",
                "$axis gameVector: $vector screenX: $screenX screenY: $screenY"
        )
        if (x < Constants.horizontalCountOfCells && y < Constants.verticalCountOfCells) {
            onClick(axis)
        }
        return false
    }


}