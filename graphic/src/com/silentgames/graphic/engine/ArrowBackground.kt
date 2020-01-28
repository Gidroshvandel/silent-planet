package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Sprite
import com.silentgames.core.logic.ecs.component.RotateAngle
import com.silentgames.graphic.Assets

class ArrowBackground(
        axis: EngineAxis,
        bmpId: String,
        assets: Assets,
        private val rotateAngle: RotateAngle
) : Background(axis, bmpId, assets) {

    override fun getResizedBitmap(width: Float, height: Float): Sprite {
        return super.getResizedBitmap(width, height).rotateBitmap(rotateAngle)
    }

    private fun Sprite.rotateBitmap(rotateAngle: RotateAngle): Sprite {
        this.setOriginCenter()
        when (rotateAngle) {
            RotateAngle.DEGREES90 -> this.rotation = 90f
            RotateAngle.DEGREES180 -> this.rotation = 180f
            RotateAngle.DEGREES270 -> this.rotation = 270f
            RotateAngle.DEGREES0 -> this.rotation = 0f
        }
        return this
    }

    override fun getBitmapId(): Int = (super.getBitmapId().toString() + rotateAngle.name).hashCode()

}