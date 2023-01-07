package com.silentgames.graphic

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.utils.Scaling
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.CoreLogger
import com.silentgames.graphic.mvp.game.GameScreen

fun Stage.resize(width: Int, height: Int, centerCamera: Boolean) {
    viewport.update(width, height, centerCamera)
    render()
}

fun Stage.render() {
    act()
    draw()
}

fun Image.setTexture(path: String, assets: Assets) {
    val sprite = assets.getSprite(path)
    val size = scaleImageForBoard(sprite.width, sprite.height, GameScreen.HEIGHT * 1.2f)
    sprite.setSize(size.x, size.y)
    this.drawable = SpriteDrawable(sprite)
}

fun TextButton.setTextColor(color: Assets.TextColor, skin: Skin) {
    this.style = TextButton.TextButtonStyle(this.style).also {
        it.fontColor = skin.getColor(color.colorName)
    }
}

fun Label.setColor(color: Assets.TextColor, skin: Skin) {
    this.style =
        Label.LabelStyle(skin.getFont(Assets.Font.REGULAR.fontName), skin.getColor(color.colorName))
            .also {
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
    unproject(
        vector.toVector3(),
        0f,
        0f,
        Gdx.graphics.height.toFloat(),
        Gdx.graphics.height.toFloat()
    )

fun Vector2.toVector3() = Vector3(x, y, 0f)

fun Vector3.toVector2() = Vector2(x, y)

object Logger {

    fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        CoreLogger.onMessageLogged = { tag, message ->
            logDebug(tag, message)
        }
    }

    fun logDebug(tag: String, message: String) {
        Gdx.app.debug(tag, message)
    }
}
