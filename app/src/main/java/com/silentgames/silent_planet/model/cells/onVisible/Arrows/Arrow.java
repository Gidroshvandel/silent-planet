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

    public abstract T rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle);

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
