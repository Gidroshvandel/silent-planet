package com.silentgames.silent_planet.model.cells.defaultCell;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class GroundCellDef extends DefaultCell {

    public GroundCellDef(Resources res) {
        super(BitmapEditor.getCellBitmap(R.drawable.planet_background,res));
        super.setCanMove(true);
    }
//    public GroundCellDef(Resources res) {
//        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.planet_background);
//        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res)));
//    }
}
