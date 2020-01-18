package com.silentgames.graphic

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter

class InputMouse : InputAdapter() {
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button != Input.Buttons.LEFT || pointer > 0) return false;
        println("screenX: " + screenX + " screenY: " + screenY + " pointer: " + pointer)
//        scene?.let {
//            val eventX = (screenX + it.scrollAxis.x) / mScaleFactor
//            val eventY = (screenY + it.scrollAxis.y) / mScaleFactor
//            val x = ((Constants.horizontalCountOfCells) * eventX / (it.width)).toInt()
//            val y = ((Constants.verticalCountOfCells) * eventY / (it.height)).toInt()
//            callback.onSingleTapConfirmed(Axis(x, y))
//        }
        return true
    }


}