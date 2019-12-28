package com.silentgames.silent_planet.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.silent_planet.engine.base.Sprite
import com.silentgames.silent_planet.logic.Constants
import kotlin.math.max
import kotlin.math.min

class Entity(
        val id: String,
        axis: EngineAxis,
        bmp: Bitmap
) : Sprite(axis, bmp) {

    private var speed = 0.2

    private var destinationAxis = axis

    var isMove = false

    override fun draw(canvas: Canvas, paint: Paint) {
        val x = cellCenterNumeratorSquare(
                axis.x,
                canvas.width,
                bmp
        )
        val y = cellCenterNumeratorSquare(
                axis.y,
                canvas.height,
                bmp
        )
        canvas.drawBitmap(bmp, x, y, paint)
    }

    private fun cellCenterNumeratorSquare(cell: Float, viewSize: Int, bitmap: Bitmap): Float {
        return cellCenterNumeratorPoint(cell, viewSize) - bitmap.width / 2
    }

    private fun cellCenterNumeratorPoint(cell: Float, viewSize: Int): Float {
        val lineCountOfCells = Constants.horizontalCountOfCells
        return 1f / (2 * lineCountOfCells) * viewSize + 1f / lineCountOfCells * cell * viewSize
    }

    fun move(fromAxis: EngineAxis, toAxis: EngineAxis) {
        isMove = true
        axis = fromAxis
        destinationAxis = toAxis
    }

    override fun update(onUpdated: ((Boolean) -> Unit)) {
        if (destinationAxis != axis) {
            isMove = true
            val difX = (destinationAxis.x - axis.x).getDiff()
            val difY = (destinationAxis.y - axis.y).getDiff()
            var x = (axis.x + (difX * speed).toFloat())
            var y = (axis.y + (difY * speed).toFloat())
            if (difX >= 0 && x > destinationAxis.x || difX < 0 && x < destinationAxis.x) {
                x = destinationAxis.x
            }
            if (difY >= 0 && y > destinationAxis.y || difY < 0 && y < destinationAxis.y) {
                y = destinationAxis.y
            }
            axis = EngineAxis(x, y)
        } else {
            if (isMove) {
                isMove = false
                onUpdated.invoke(true)
            } else {
                onUpdated.invoke(false)
            }
        }
    }

    private fun Float.getDiff() = if (this >= 0) max(this, 0.01f) else min(this, -0.01f)

    override fun equals(other: Any?): Boolean {
        return if (other is Entity) {
            other.id == id
        } else {
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}