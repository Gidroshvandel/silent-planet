package com.silentgames.silent_planet.model.fractions;

/**
 * Created by gidroshvandel on 26.09.16.
 */
public enum FractionsEnum {
    Aliens, Humans, Pirates, Robots;

    private static FractionsEnum[] vals = values();
    public FractionsEnum next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}

