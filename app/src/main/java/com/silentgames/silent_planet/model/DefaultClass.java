package com.silentgames.silent_planet.model;

import android.app.Activity;
import android.graphics.Bitmap;

import com.silentgames.silent_planet.logic.Fractions;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class DefaultClass{

    private Bitmap bitmap;
    private boolean canMove = false;
    private boolean canFly = false;
    private boolean isDead = false;

    public DefaultClass() {
    }

    public void setAll(DefaultClass defaultClass){
        setBitmap(defaultClass.getBitmap());
        setCanFly(defaultClass.isCanFly());
        setCanMove(defaultClass.isCanMove());
        setDead(defaultClass.isDead());
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
