package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Batch
import com.silentgames.core.logic.Constants
import com.silentgames.graphic.engine.base.Sprite
import kotlin.math.max
import kotlin.math.min

class Entity(
        val id: String,
        axis: EngineAxis,
        bmpResourceId: String
) : Sprite(axis, bmpResourceId) {

    private var speed = 0.2

    private var destinationAxis = axis

    var isMove = false

    override fun draw(batch: Batch, width: Int, height: Int) {
        val size = ((width / Constants.verticalCountOfCells) * 0.7).toFloat()
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