package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Batch
import com.silentgames.graphic.engine.base.Basic

class Grid : Basic() {

    override fun draw(batch: Batch, width: Int, height: Int) {

//        batch.end()
//        val shapeRenderer = ShapeRenderer()
//        shapeRenderer.setColor(1f, 1f, 1f, 1f)
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
//        //рисуем сетку
//        val horizontalCountOfCells = Constants.horizontalCountOfCells
//        val verticalCountOfCells = Constants.verticalCountOfCells
//        for (x in 0 until horizontalCountOfCells + 1) {
//            shapeRenderer.line(
//                    x.toFloat() * width / horizontalCountOfCells,
//                    0f,
//                    x.toFloat() * height / horizontalCountOfCells,
//                    height.toFloat()
//            )
//        }
//        for (y in 0 until verticalCountOfCells + 1) {
//            shapeRenderer.line(
//                    0f,
//                    y.toFloat() * width / verticalCountOfCells,
//                    width.toFloat(),
//                    y.toFloat() * height / verticalCountOfCells
//            )
//        }
//        shapeRenderer.end()
//        batch.begin()
    }
}