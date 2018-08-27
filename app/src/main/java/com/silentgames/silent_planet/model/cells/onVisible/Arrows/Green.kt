package com.silentgames.silent_planet.model.cells.onVisible.Arrows

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.EntityMove
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.utils.BitmapEditor

import java.util.HashMap

/**
 * Created by gidroshvandel on 09.12.16.
 */
class Green : Arrow() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell)
        super.isCanMove = true
        super.rotateAngle = BitmapEditor.RotateAngle.DEGREES0
        super.distance = 1
    }

}
