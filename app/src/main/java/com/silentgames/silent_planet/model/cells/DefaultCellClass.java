package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.DefaultClass;

/**
 * Created by gidroshvandel on 17.07.16.
 */
public class DefaultCellClass<T> extends DefaultClass {
    public boolean isDead = false;

    public DefaultCellClass() {
        setCanFly(false);
        setCanMove(true);
    }

    public DefaultCellClass(boolean isDead) {
        this.isDead = isDead;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
