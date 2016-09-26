package com.silentgames.silent_planet.model.entities;

import com.silentgames.silent_planet.logic.Fractions;
import com.silentgames.silent_planet.model.DefaultClass;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class EntityTypeEx extends DefaultClass {
    Fractions fraction;

    public Fractions getFraction() {
        return fraction;
    }

    public void setFraction(Fractions fraction) {
        this.fraction = fraction;
    }

    public void setAll(EntityTypeEx defaultClass){
        setBitmap(defaultClass.getBitmap());
        setCanFly(defaultClass.isCanFly());
        setCanMove(defaultClass.isCanMove());
        setDead(defaultClass.isDead());
        setFraction(defaultClass.getFraction());
    }
}
