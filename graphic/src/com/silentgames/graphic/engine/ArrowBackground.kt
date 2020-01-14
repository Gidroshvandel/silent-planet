package com.silentgames.graphic.engine

import com.silentgames.core.logic.ecs.component.RotateAngle

class ArrowBackground(
        axis: EngineAxis,
        bmpId: String,
        private val rotateAngle: RotateAngle
) : Background(axis, bmpId) {

//    override fun initBitmap(bmpResourceId: String): Texture {
//        return super.initBitmap(bmpResourceId).rotateBitmap(rotateAngle)
//    }

//    private fun Texture.rotateBitmap(rotateAngle: RotateAngle): Texture {
//        val matrix = Matrix()
//        when (rotateAngle) {
//            RotateAngle.DEGREES90 -> matrix.postRotate(90f)
//            RotateAngle.DEGREES180 -> matrix.postRotate(180f)
//            RotateAngle.DEGREES270 -> matrix.postRotate(270f)
//            RotateAngle.DEGREES0 -> matrix.postRotate(0f)
//        }
//        return Texture.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
//    }

    override fun getBitmapId(): Int = (super.getBitmapId().toString() + rotateAngle.name).hashCode()

}