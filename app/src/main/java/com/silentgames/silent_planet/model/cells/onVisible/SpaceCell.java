package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceCell extends OnVisible {
    public SpaceCell() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.space_texture));
        super.setCanFly(true);
    }

    @Override
    public GameMatrixHelper doEvent(GameMatrixHelper gameMatrixHelper) {
        return gameMatrixHelper;
    }

    //    public SpaceDef(Resources res) {
//        super();
//        setCanFly(true);
//        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.space_texture);
//        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res)));
//    }
}
