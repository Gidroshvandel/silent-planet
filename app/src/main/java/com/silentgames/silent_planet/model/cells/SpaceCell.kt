package com.silentgames.silent_planet.model.cells

import android.graphics.Bitmap
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 07.07.16.
 */
class SpaceCell(
        override var bitmap: Bitmap = BitmapEditor.getCellBitmap(R.drawable.space_texture),
        override var isCanMove: Boolean = false
) : CellType(isCanFly = true, closeBitmap = BitmapEditor.getCellBitmap(R.drawable.space_texture)) {

    override fun doEvent(gameMatrixHelper: GameMatrixHelper): GameMatrixHelper {
        return gameMatrixHelper
    }

    //    public SpaceDef(Resources res) {
    //        super();
    //        setCanFly(true);
    //        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.space_texture);
    //        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellImageSize,res),Converter.convertDpToPixel(Constants.cellImageSize,res)));
    //    }
}
