package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Batch
import com.silentgames.core.logic.ecs.component.RotateAngle
import com.silentgames.graphic.Assets

class ArrowBackground(
        axis: EngineAxis,
        bmpId: String,
        assets: Assets,
        private val rotateAngle: RotateAngle
) : Background(axis, bmpId, assets) {

    override fun draw(batch: Batch, width: Int, height: Int, stateTime: Float) {
        runningAnimation?.getKeyFrame(stateTime, true)?.let {
            val axis = getCoordinates(axis, width, height, it)
            val size = getSize(it, height)
            batch.draw(
                    it,
                    axis.x,
                    axis.y,
                    size.x / 2,
                    size.y / 2,
                    size.x,
                    size.y,
                    1f,
                    1f,
                    rotate(rotateAngle)
            )
        }
    }

    private fun rotate(rotateAngle: RotateAngle) =
            when (rotateAngle) {
                RotateAngle.DEGREES90 -> 90f
                RotateAngle.DEGREES180 -> 180f
                RotateAngle.DEGREES270 -> 270f
                RotateAngle.DEGREES0 -> 0f
            }

    override fun getBitmapId(): Int = (super.getBitmapId().toString() + rotateAngle.name).hashCode()

}