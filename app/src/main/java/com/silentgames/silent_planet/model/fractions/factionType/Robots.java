package com.silentgames.silent_planet.model.fractions.factionType;

import com.silentgames.silent_planet.model.fractions.Fractions;
import com.silentgames.silent_planet.model.fractions.FractionsEnum;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class Robots extends Fractions {

    private static Robots instance;

    public static synchronized Robots getInstance(){
        if(instance == null){
            instance = new Robots();
        }
        return instance;
    }

    private Robots() {
        this.setFractionsEnum(FractionsEnum.Robots);
    }
}
