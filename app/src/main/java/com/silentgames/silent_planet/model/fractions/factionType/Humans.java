package com.silentgames.silent_planet.model.fractions.factionType;

import com.silentgames.silent_planet.model.fractions.Fractions;
import com.silentgames.silent_planet.model.fractions.FractionsEnum;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class Humans extends Fractions {

    private static Humans instance;

    public static synchronized Humans getInstance(){
        if(instance == null){
            instance = new Humans();
        }
        return instance;
    }

    private Humans() {
        this.setFractionsEnum(FractionsEnum.Humans);
    }
}
