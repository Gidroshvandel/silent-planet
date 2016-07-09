package com.silentgames.silent_planet.model;

import android.graphics.Bitmap;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class DefaultClass {

    private Bitmap bitmap;
    private boolean canMove;
    private boolean canFly;

    public DefaultClass() {
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public boolean isCanFly() {
        return canFly;
    }

    public void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
