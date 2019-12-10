package com.silentgames.silent_planet.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.silent_planet.engine.base.Sprite
import com.silentgames.silent_planet.logic.Constants

class Background(
        axis: EngineAxis,
        bmp: Bitmap
) : Sprite(axis, bmp) {

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
}