package com.silentgames.graphic.engine

import com.badlogic.gdx.graphics.g2d.Sprite
import com.silentgames.core.logic.ecs.component.RotateAngle

class ArrowBackground(
        axis: EngineAxis,
        bmpId: String,
        private val rotateAngle: RotateAngle
) : Background(axis, bmpId) {

    override fun getResizedBitmap(width: Float, height: Float): Sprite {
        return super.getResizedBitmap(width, height).rotateBitmap(rotateAngle)
    }

    private fun Sprite.rotateBitmap(rotateAngle: RotateAngle): Sprite {
        this.setOriginCenter()
        when (rotateAngle) {
            RotateAngle.DEGREES90 -> this.setRotation(90f)
            RotateAngle.DEGREES180 -> this.setRotation(180f)
            RotateAngle.DEGREES270 -> this.setRotation(270f)
            RotateAngle.DEGREES0 -> this.setRotation(0f)
        }
        return this
    }

    override fun getBitmapId(): Int = (super.getBitmapId().toString() + rotateAngle.name).hashCode()

}