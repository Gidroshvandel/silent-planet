package com.silentgames.graphic

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Camera.unProject(vector: Vector2) =
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