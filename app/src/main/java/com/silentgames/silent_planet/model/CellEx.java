package com.silentgames.silent_planet.model;

import android.graphics.Bitmap;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class CellEx {

    private Bitmap bitmap;
    private boolean canMove = false;
    private boolean canFly = false;
    private boolean isDead = false;
    private int crystals = 0;

    public CellEx() {
    }

    public void setAll(CellEx cellEx){
        setBitmap(cellEx.getBitmap());
        setCanFly(cellEx.isCanFly());
        setCanMove(cellEx.isCanMove());
        setDead(cellEx.isDead());
        setCrystals(cellEx.getCrystals());
    }

    public int getCrystals() {
        return crystals;
    }

    public void setCrystals(int crystals) {
        this.crystals = crystals;
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

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
