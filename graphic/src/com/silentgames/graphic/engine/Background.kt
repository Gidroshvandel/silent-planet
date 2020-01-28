package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Batch
import com.silentgames.core.logic.Constants
import com.silentgames.graphic.Assets
import com.silentgames.graphic.engine.base.Sprite

open class Background(
        axis: EngineAxis,
        bmpId: String,
        assets: Assets
) : Sprite(axis, bmpId, assets) {

    override fun draw(batch: Batch, width: Int, height: Int) {
        val size = (width / Constants.verticalCountOfCells).toFloat()
        val sprite = getResizedBitmap(size, size)

        val x = cellCenterNumeratorSquare(
                axis.x,
                width,
                sprite
        )
        val y = cellCenterNumeratorSquare(
                axis.y,
                height,
                sprite
        )
        sprite.setPosition(x, y)
        sprite.draw(batch)
    }

    private fun cellCenterNumeratorSquare(cell: Float, viewSize: Int, bitmap: com.badlogic.gdx.graphics.g2d.Sprite): Float {
        return cellCenterNumeratorPoint(cell, viewSize) - bitmap.width / 2
    }

    private fun cellCenterNumeratorPoint(cell: Float, viewSize: Int): Float {
        val lineCountOfCells = Constants.horizontalCountOfCells
        return 1f / (2 * lineCountOfCells) * viewSize + 1f / lineCountOfCells * cell * viewSize
    }
}