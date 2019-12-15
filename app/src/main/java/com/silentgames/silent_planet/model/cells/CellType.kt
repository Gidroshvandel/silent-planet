package com.silentgames.silent_planet.model.cells

import android.content.Context
import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Event
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 09.07.16.
 */
abstract class CellType(
        val context: Context,
        override var crystals: Int = 0,
        override val closeBitmap: Bitmap = BitmapEditor.getCellBitmap(context, R.drawable.planet_background),
        override var isCanFly: Boolean = false,
        override var isDead: Boolean = false,
        override var isVisible: Boolean = false,
        name: String = "",
        description: String = ""
) : CellTypeProperties {

    override var name: String = name
        get() = if (isVisible) field else context.getString(R.string.unknown_cell_name)

    override var description: String = description
        get() = if (isVisible) field else context.getString(R.string.unknown_cell_description)

    open fun doEvent(event: Event) {
    }

    fun getCurrentBitmap(): Bitmap = if (isVisible) {
        bitmap
    } else {
        closeBitmap
    }
}
