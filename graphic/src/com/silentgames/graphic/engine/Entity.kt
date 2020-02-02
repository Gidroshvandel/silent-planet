package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.silentgames.graphic.Assets
import com.silentgames.graphic.engine.base.Sprite
import kotlin.math.max
import kotlin.math.min

class Entity(
        val id: String,
        axis: EngineAxis,
        bmpResourceId: String,
        assets: Assets
) : Sprite(axis, bmpResourceId, assets) {

    private var speed = 0.2

    private var destinationAxis = axis

    var isMove = false

    override fun initAnimation(textures: Array<TextureRegion>): Animation<TextureRegion> =
            Animation(0.033f, textures, Animation.PlayMode.NORMAL)

    override fun draw(batch: Batch, width: Int, height: Int, stateTime: Float) {
        runningAnimation?.getKeyFrame(stateTime, true)?.let {
            val axis = getCoordinates(axis, width, height, it)
            val size = getSize(it, width)
            batch.draw(
                    it,
                    axis.x,
                    axis.y,
                    size.x,
                    size.y
            )
        }
    }

    override fun getSize(textureRegion: TextureRegion, batchSize: Int): Vector2 {
        return super.getSize(textureRegion, batchSize).apply {
            x *= 0.9f
            y *= 0.9f
        }
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