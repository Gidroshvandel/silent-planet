package com.silentgames.silent_planet.model.cells.onVisible

import android.content.res.Resources

import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.GameMatrixHelper
import com.silentgames.silent_planet.utils.BitmapEditor

/**
 * Created by gidroshvandel on 07.07.16.
 */
class SpaceCell : OnVisible() {
    init {
        super.bitmap = BitmapEditor.getCellBitmap(R.drawable.space_texture)
        super.isCanFly = true
    }

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
