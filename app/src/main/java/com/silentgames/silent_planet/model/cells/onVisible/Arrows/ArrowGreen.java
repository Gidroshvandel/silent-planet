package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 09.12.16.
 */
public class ArrowGreen extends Arrow {

    public ArrowGreen(Resources res) {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell,res));
        super.setCanMove(true);
//        super.setCrystals(1);
    }

    @Override
    public Cell doEvent(Cell gameMatrixCell) {

        return gameMatrixCell;
    }

    @Override
    public ArrowGreen rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle) {
        switch (rotateAngle){
            case DEGREES0:
                x = x - 1;
                y = y + 1;
                break;
            case DEGREES90:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90,getBitmap()));
                x = x + 1;
                y = y + 1;
                break;
            case DEGREES180:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180,getBitmap()));
                x = x + 1;
                y = y - 1;
                break;
            case DEGREES360:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES360,getBitmap()));
                x = x - 1;
                y = y - 1;
                break;
        }
        return this;
    }
}
