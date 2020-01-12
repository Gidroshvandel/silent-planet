package com.silentgames.silent_planet.engine

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import com.silentgames.silent_planet.utils.BitmapEditor

class ArrowBackground(
        context: Context,
        axis: EngineAxis,
        bmpId: Int,
        private val rotateAngle: BitmapEditor.RotateAngle
) : Background(context, axis, bmpId) {

    override fun initBitmap(bmpResourceId: Int): Bitmap {
        return super.initBitmap(bmpResourceId).rotateBitmap(rotateAngle)
    }

    private fun Bitmap.rotateBitmap(rotateAngle: BitmapEditor.RotateAngle): Bitmap {
        val matrix = Matrix()
        when (rotateAngle) {
            BitmapEditor.RotateAngle.DEGREES90 -> matrix.postRotate(90f)
            BitmapEditor.RotateAngle.DEGREES180 -> matrix.postRotate(180f)
            BitmapEditor.RotateAngle.DEGREES270 -> matrix.postRotate(270f)
            BitmapEditor.RotateAngle.DEGREES0 -> matrix.postRotate(0f)
        }
        return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
    }

    override fun getBitmapId(): Int = (super.getBitmapId().toString() + rotateAngle.name).hashCode()

}