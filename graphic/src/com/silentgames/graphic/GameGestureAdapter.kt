package com.silentgames.graphic

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

class GameGestureAdapter(private val camera: OrthographicCamera) : GestureDetector.GestureAdapter() {
    private var scaleFactor: Float = 1f
    private var isNowPinch = false
    private var zoomPoint: Vector2? = null

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        val ratio: Float = initialDistance / distance

        var zoom = scaleFactor * ratio

        if (zoom > 1) {
            zoom = 1f
        } else if (zoom < 0.5) {
            zoom = 0.5f
        }
        camera.zoom = zoom
        checkCameraBorders()
        return false
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        if (count == 2) {
            if (scaleFactor < 1f) {
                camera.zoom = 1f
                scaleFactor = camera.zoom
            } else {
                camera.zoom = 0.5f
                scaleFactor = camera.zoom
                val vector = camera.unProject(Vector2(x, y))
                camera.position.x = vector.x
                camera.position.y = vector.y
            }
            checkCameraBorders()
            return true
        }
        return false
    }


    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        if (!isNowPinch) {
            camera.translate(-deltaX * 0.5f, -deltaY * 0.5f)
            checkCameraBorders()
        }
        return false
    }


    override fun pinch(initialPointer1: Vector2, initialPointer2: Vector2, pointer1: Vector2, pointer2: Vector2): Boolean {
        isNowPinch = true
        val initialPointerFirst = camera.unProject(initialPointer1)
        val initialPointerSecond = camera.unProject(initialPointer2)
        if (zoomPoint == null) {
            zoomPoint = Vector2((initialPointerFirst.x + initialPointerSecond.x) / 2, (initialPointerFirst.y + initialPointerSecond.y) / 2)
        }
        zoomPoint?.let {
            camera.position.x = it.x
            camera.position.y = it.y
        }
        return false
    }

    override fun pinchStop() {
        isNowPinch = false
        zoomPoint = null
        scaleFactor = camera.zoom
        super.pinchStop()
    }

    private fun checkCameraBorders() {
        val effectiveViewportWidth: Float = camera.viewportWidth * camera.zoom
        val effectiveViewportHeight: Float = camera.viewportHeight * camera.zoom
        camera.position.x = MathUtils.clamp(
                camera.position.x,
                effectiveViewportWidth / 2f,
                camera.viewportWidth - effectiveViewportWidth / 2f
        )
        camera.position.y = MathUtils.clamp(
                camera.position.y,
                effectiveViewportHeight / 2f,
                camera.viewportHeight - effectiveViewportHeight / 2f
        )
    }

}