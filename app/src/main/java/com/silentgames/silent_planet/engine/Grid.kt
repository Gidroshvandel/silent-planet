package com.silentgames.silent_planet.engine

import android.graphics.Canvas
import android.graphics.Paint
import com.silentgames.silent_planet.engine.base.Basic
import com.silentgames.silent_planet.logic.Constants

class Grid(private var canvasSize: Float) : Basic() {
    private fun paintSettings(paint: Paint) {
        //определяем параметры кисти
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = -0x1
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    override fun update() {}

    override fun draw(canvas: Canvas, paint: Paint) {
        paintSettings(paint)
        //рисуем сетку
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells + 1) {
            canvas.drawLine(
                    x.toFloat() * canvasSize / horizontalCountOfCells,
                    0f,
                    x.toFloat() * canvasSize / horizontalCountOfCells,
                    canvasSize,
                    paint
            )
        }
        for (y in 0 until verticalCountOfCells + 1) {
            canvas.drawLine(
                    0f,
                    y.toFloat() * canvasSize / verticalCountOfCells,
                    canvasSize,
                    y.toFloat() * canvasSize / verticalCountOfCells,
                    paint
            )
        }
    }
}