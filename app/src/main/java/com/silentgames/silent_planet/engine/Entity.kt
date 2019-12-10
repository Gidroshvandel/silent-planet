package com.silentgames.silent_planet.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.silent_planet.engine.base.Sprite
import com.silentgames.silent_planet.logic.Constants

class Entity(
        val id: Int,
        axis: EngineAxis,
        bmp: Bitmap
) : Sprite(axis, bmp) {

    private var speed = 0.1

    private var destinationAxis = axis

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

    fun move(axis: EngineAxis) {
        destinationAxis = axis
    }

    fun move(fromAxis: EngineAxis, toAxis: EngineAxis) {
        axis = fromAxis
        destinationAxis = toAxis
    }

    override fun update() {
        if (destinationAxis != axis) {
            val difX = destinationAxis.x - axis.x
            val difY = destinationAxis.y - axis.y
            val x = difX * speed
            val y = difY * speed
            axis = EngineAxis(axis.x + x.toFloat(), axis.y + y.toFloat())
        }
    }

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