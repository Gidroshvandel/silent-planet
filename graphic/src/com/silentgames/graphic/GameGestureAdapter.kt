package com.silentgames.graphic

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2

class GameBoardZoomGestureDetector(
        private val listener: GameBoardZoomGestureAdapter
) : GestureDetector(listener) {

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        return listener.mouseScrolled(amountY.toInt())
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return listener.mouseMoved(screenX, screenY)
    }

    open class GameBoardZoomGestureAdapter() : GestureAdapter() {

        open fun mouseScrolled(amount: Int): Boolean = false

        open fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    }
}

class GameGestureAdapter(private val camera: OrthographicCamera) : GameBoardZoomGestureDetector.GameBoardZoomGestureAdapter() {
    private var scaleFactor: Float = 1f
    private var isNowPinch = false
    private var zoomPoint: Vector2? = null

    private var mousePosition = Vector2()

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        mousePosition = Vector2(screenX.toFloat(), screenY.toFloat())
        return super.mouseMoved(screenX, screenY)
    }

    override fun mouseScrolled(amount: Int): Boolean {
        camera.setPosition(camera.unProject(mousePosition).toVector2())
        if (amount > 0) {
            scaleFactor += 0.1f
        } else {
            scaleFactor -= 0.1f
        }
        zoomCamera(scaleFactor)
        scaleFactor = camera.zoom
        return super.mouseScrolled(amount)
    }

    private fun zoomCamera(zoom: Float) {
        camera.zoom = when {
            zoom > 1 -> {
                1f
            }
            zoom < 0.5 -> {
                0.5f
            }
            else -> {
                zoom
            }
        }
        checkCameraBorders()
    }

    override fun zoom(initialDistance: Float, distance: Float): Boolean {
        val ratio: Float = initialDistance / distance
        zoomCamera(scaleFactor * ratio)
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
        zoomPoint?.let { camera.setPosition(it) }
        return false
    }

    override fun pinchStop() {
        isNowPinch = false
        zoomPoint = null
        scaleFactor = camera.zoom
        super.pinchStop()
    }

    private fun Camera.setPosition(vector2: Vector2) {
        position.x = vector2.x
        position.y = vector2.y
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