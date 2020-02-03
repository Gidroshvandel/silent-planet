package com.silentgames.graphic

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Scaling
import com.silentgames.core.logic.Constants

fun Label.setColor(color: Assets.TextColor, skin: Skin) {
    this.style = Label.LabelStyle(skin.getFont(Assets.Font.REGULAR.fontName), skin.getColor(color.colorName)).also {
        it.background = this.style.background
    }
}

fun scaleImageForBoard(width: Float, height: Float, size: Float): Vector2 {
    return scaleImageForBoard(width, height, size, Constants.verticalCountOfCells)
}

fun scaleImageForBoard(width: Float, height: Float, size: Float, countOfCells: Int): Vector2 {
    val targetSize = (size / countOfCells)
    return Scaling.fit.apply(width, height, targetSize, targetSize)
}

fun Camera.unProject(vector: Vector2): Vector3 =
        unproject(vector.toVector3(), 0f, 0f, Gdx.graphics.height.toFloat(), Gdx.graphics.height.toFloat())

fun Vector2.toVector3() = Vector3(x, y, 0f)

fun Vector3.toVector2() = Vector2(x, y)

object Logger {

    init {
        Gdx.app.logLevel = LOG_DEBUG
    }

    fun logDebug(tag: String, message: String) {
        Gdx.app.debug(tag, message)
    }

}