package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by Lantiets on 28.08.2017.
 */

public class Red extends Arrow {

    public Red() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.arow_red_cell));
        super.setCanMove(true);
        super.setRotateAngle(BitmapEditor.RotateAngle.DEGREES0);
        super.setDistance(3);
    }

    public Red rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle){
        switch (rotateAngle){
            case DEGREES0:
                setDestinationX(x);
                setDestinationY(y - getDistance());
                break;
            case DEGREES90:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90,getBitmap()));
                setDestinationX(x + getDistance());
                setDestinationY(y);
                break;
            case DEGREES180:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180,getBitmap()));
                setDestinationX(x);
                setDestinationY(y + getDistance());
                break;
            case DEGREES270:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270,getBitmap()));
                setDestinationX(x - getDistance());
                setDestinationY(y);
                break;
        }
        setRotateAngle(rotateAngle);
        return this;
    }
}
