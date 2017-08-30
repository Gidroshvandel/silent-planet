package com.silentgames.silent_planet.model.entities;

import com.silentgames.silent_planet.model.fractions.Fractions;
import com.silentgames.silent_planet.model.fractions.FractionsEnum;
import com.silentgames.silent_planet.model.CellEx;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public abstract class EntityTypeEx extends CellEx {
    private Fractions fraction;

    public Fractions getFraction() {
        return fraction;
    }

    public void setFraction(Fractions fraction) {
        this.fraction = fraction;
    }

    protected void setAll(EntityTypeEx defaultClass){
        super.setAll(defaultClass);
        setFraction(defaultClass.getFraction());
    }
}
