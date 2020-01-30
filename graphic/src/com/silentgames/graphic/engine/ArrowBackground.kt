package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Batch
import com.silentgames.core.logic.Constants
import com.silentgames.core.logic.ecs.component.RotateAngle
import com.silentgames.graphic.Assets

class ArrowBackground(
        axis: EngineAxis,
        bmpId: String,
        assets: Assets,
        private val rotateAngle: RotateAngle
) : Background(axis, bmpId, assets) {

    override fun draw(batch: Batch, width: Int, height: Int, stateTime: Float) {
        val axis = getCoordinates(axis, width, height)
        runningAnimation?.getKeyFrame(stateTime, true)?.let {
            val viewWidth = getSize(width, Constants.verticalCountOfCells)
            val viewHeight = getSize(height, Constants.horizontalCountOfCells)
            batch.draw(
                    it,
                    axis.x,
                    axis.y,
                    viewWidth / 2,
                    viewHeight / 2,
                    viewWidth,
                    viewHeight,
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