package com.silentgames.silent_planet.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.silentgames.core.logic.ecs.component.RotateAngle

class ArrowBackground(
    context: Context,
    axis: EngineAxis,
    bmpId: String,
    private val rotateAngle: RotateAngle
) : Background(context, axis, bmpId) {

    override fun initBitmap(bmpResourceId: String): Bitmap {
        return super.initBitmap(bmpResourceId).rotateBitmap(rotateAngle)
    }

    private fun Bitmap.rotateBitmap(rotateAngle: RotateAngle): Bitmap {
        val matrix = Matrix()
        when (rotateAngle) {
            RotateAngle.DEGREES90 -> matrix.postRotate(90f)
            RotateAngle.DEGREES180 -> matrix.postRotate(180f)
            RotateAngle.DEGREES270 -> matrix.postRotate(270f)
            RotateAngle.DEGREES0 -> matrix.postRotate(0f)
        }
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    override fun getBitmapId(): Int = (super.getBitmapId().toString() + rotateAngle.name).hashCode()
}
