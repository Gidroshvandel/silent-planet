package com.silentgames.silent_planet.model.cells.Crystal

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.utils.BitmapEditor
import java.util.*

enum class CrystalsEnum(val crystalsCount: Int, val image: Bitmap) {

    ONE(1, BitmapEditor.getCellBitmap(R.drawable.one_crystal)),
    TWO(2, BitmapEditor.getCellBitmap(R.drawable.two_crystals)),
    THREE(3, BitmapEditor.getCellBitmap(R.drawable.three_crystals));

//    private val values =
//            Collections.unmodifiableList<CrystalsEnum>(Arrays.asList<CrystalsEnum>(*values()))

    companion object {
        private val values = mutableListOf(*values())
        private val size = values.size
        private val random = Random()
        fun random(): CrystalsEnum {
            return values[random.nextInt(size)]
        }
    }


}