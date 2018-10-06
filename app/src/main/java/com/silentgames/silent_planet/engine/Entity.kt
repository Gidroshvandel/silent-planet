package com.silentgames.silent_planet.engine

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.silent_planet.engine.base.Sprite
import com.silentgames.silent_planet.model.Axis
import com.silentgames.silent_planet.utils.Calculator

class Entity(
        private var canvasSize: Float,
        axis: Axis,
        bmp: Bitmap
) : Sprite(axis, bmp) {

    override fun update() {}

    override fun draw(canvas: Canvas, paint: Paint) {
        val x = Calculator.CellCenterNumeratorSquare(axis.x.toFloat(), canvasSize.toInt(), bmp)
        val y = Calculator.CellCenterNumeratorSquare(axis.y.toFloat(), canvasSize.toInt(), bmp)
        canvas.drawBitmap(bmp, x, y, paint)
    }

}