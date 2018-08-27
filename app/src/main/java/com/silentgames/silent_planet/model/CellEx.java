package com.silentgames.silent_planet.model;

import android.graphics.Bitmap;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public abstract class CellEx {

    private Bitmap bitmap;
    private boolean canMove = false;
    private boolean canFly = false;
    private boolean isDead = false;


    public CellEx() {
    }

    protected void setAll(CellEx cellEx){
        setBitmap(cellEx.getBitmap());
        setCanFly(cellEx.isCanFly());
        setCanMove(cellEx.isCanMove());
        setDead(cellEx.isDead());
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

    protected void setCanFly(boolean canFly) {
        this.canFly = canFly;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isDead() {
        return isDead;
    }

    protected void setDead(boolean dead) {
        isDead = dead;
    }
}
