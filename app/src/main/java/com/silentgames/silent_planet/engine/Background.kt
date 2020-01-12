package com.silentgames.silent_planet.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.core.logic.Constants
import com.silentgames.silent_planet.engine.base.Sprite

open class Background(
        context: Context,
        axis: EngineAxis,
        bmpId: String
) : Sprite(context, axis, bmpId) {

    override fun draw(canvas: Canvas, paint: Paint) {
        val size = (canvas.width / Constants.verticalCountOfCells).toFloat()
        val bitmap = getResizedBitmap(size, size)
        val x = cellCenterNumeratorSquare(
                axis.x,
                canvas.width,
                bitmap
        )
        val y = cellCenterNumeratorSquare(
                axis.y,
                canvas.height,
                bitmap
        )
        canvas.drawBitmap(bitmap, x, y, paint)
    }

    private fun cellCenterNumeratorSquare(cell: Float, viewSize: Int, bitmap: Bitmap): Float {
        return cellCenterNumeratorPoint(cell, viewSize) - bitmap.width / 2
    }

    private fun cellCenterNumeratorPoint(cell: Float, viewSize: Int): Float {
        val lineCountOfCells = Constants.horizontalCountOfCells
        return 1f / (2 * lineCountOfCells) * viewSize + 1f / lineCountOfCells * cell * viewSize
    }
}