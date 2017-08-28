package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;
import com.silentgames.silent_planet.utils.BitmapEditor;

/**
 * Created by gidroshvandel on 09.12.16.
 */
public abstract class Arrow<T> extends OnVisible {

    private BitmapEditor.RotateAngle rotateAngle;
    private int destinationX;
    private int destinationY;

    public Arrow<T> rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle){
        switch (rotateAngle){
            case DEGREES0:
                setDestinationX(x + 1);
                setDestinationY(y - 1);
                break;
            case DEGREES90:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90,getBitmap()));
                setDestinationX(x + 1);
                setDestinationY(y + 1);
                break;
            case DEGREES180:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180,getBitmap()));
                setDestinationX(x - 1);
                setDestinationY(y + 1);
                break;
            case DEGREES270:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270,getBitmap()));
                setDestinationX(x - 1);
                setDestinationY(y - 1);
                break;
        }
        setRotateAngle(rotateAngle);
        return this;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public BitmapEditor.RotateAngle getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(BitmapEditor.RotateAngle rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public void setDestinationX(int destinationX) {
        this.destinationX = destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }
}
