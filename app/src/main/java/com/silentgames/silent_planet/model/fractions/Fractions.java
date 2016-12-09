package com.silentgames.silent_planet.model.fractions;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class Fractions {

    private FractionsEnum fractionsEnum;
    private Boolean isPlayable = false;

    public FractionsEnum getFractionsEnum() {
        return fractionsEnum;
    }

    public void setFractionsEnum(FractionsEnum fractionsEnum) {
        this.fractionsEnum = fractionsEnum;
    }

    public Boolean isPlayable() {
        return isPlayable;
    }

    public void setPlayable(Boolean playable) {
        isPlayable = playable;
    }
}
