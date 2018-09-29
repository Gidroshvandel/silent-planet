package com.silentgames.silent_planet.model.cells.Crystal

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor
import java.util.*

enum class CrystalsEnum(val crystalsCount: Int) {

    ONE(1) {
        override fun getImage(context: Context) =
                BitmapEditor.getCellBitmap(context, R.drawable.one_crystal)
    },
    TWO(2) {
        override fun getImage(context: Context) =
                BitmapEditor.getCellBitmap(context, R.drawable.two_crystals)
    },
    THREE(3) {
        override fun getImage(context: Context) =
                BitmapEditor.getCellBitmap(context, R.drawable.three_crystals)
    };

    abstract fun getImage(context: Context): Bitmap

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): CrystalsEnum {
            return values[random.nextInt(size)]
        }
    }


}