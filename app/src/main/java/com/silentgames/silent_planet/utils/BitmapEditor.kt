package com.silentgames.silent_planet.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.support.annotation.DrawableRes
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.utils.BitmapEditor.RotateAngle.*
import java.util.*

/**
 * Created by gidroshvandel on 07.07.16.
 */
object BitmapEditor {

    private fun resize(bit: Bitmap, newWidth: Float, newHeight: Float): Bitmap {
        val width = bit.width
        val height = bit.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeight / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bit, 0, 0,
                width, height, matrix, true)
    }

    fun getEntityBitmap(context: Context, @DrawableRes DefaultBitId: Int): Bitmap {
        val res = context.resources
        val bitmap = BitmapFactory.decodeResource(res, DefaultBitId)
        return resize(
                bitmap,
                Converter.convertDpToPixel(Constants.entityImageSize, res),
                Converter.convertDpToPixel(Constants.entityImageSize, res)
        )
    }

    fun getCellBitmap(context: Context, DefaultBitId: Int): Bitmap {
        val res = context.resources
        val bitmap = BitmapFactory.decodeResource(res, DefaultBitId)
        return resize(
                bitmap,
                Converter.convertDpToPixel(Constants.cellImageSize, res),
                Converter.convertDpToPixel(Constants.cellImageSize, res)
        )
    }

    fun rotateBitmap(rotateAngle: RotateAngle, bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        when (rotateAngle) {
            DEGREES90 -> matrix.postRotate(90f)
            DEGREES180 -> matrix.postRotate(180f)
            DEGREES270 -> matrix.postRotate(270f)
            DEGREES0 -> matrix.postRotate(0f)
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    enum class RotateAngle {
        DEGREES0, DEGREES90, DEGREES180, DEGREES270;

        companion object {
            private val VALUES = Collections.unmodifiableList(Arrays.asList(*values()))
            private val SIZE = VALUES.size
            private val RANDOM = Random()
            fun randomAngle(): RotateAngle {
                return VALUES[RANDOM.nextInt(SIZE)]
            }
        }
    }
}