package com.silentgames.silent_planet.logic;

/**
 * Created by gidroshvandel on 26.09.16.
 */
public enum  Fractions  {
    Aliens, Humans, Pirates, Robots;

    private static Fractions[] vals = values();
    public Fractions next()
    {
        return vals[(this.ordinal()+1) % vals.length];
    }
}

