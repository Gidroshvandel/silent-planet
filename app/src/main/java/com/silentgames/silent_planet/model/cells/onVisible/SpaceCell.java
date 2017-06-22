package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceCell extends OnVisible {
    public SpaceCell(Resources res) {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.space_texture,res));
        super.setCanFly(true);
    }

    @Override
    public Cell doEvent(Cell gameMatrixCell) {
        return gameMatrixCell;
    }


//    public SpaceDef(Resources res) {
//        super();
//        setCanFly(true);
//        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.space_texture);
//        setBitmap(BitmapEditor.resize(bitmap, Converter.convertDpToPixel(Constants.cellSize,res),Converter.convertDpToPixel(Constants.cellSize,res)));
//    }
}
