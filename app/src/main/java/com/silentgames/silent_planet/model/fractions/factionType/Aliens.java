package com.silentgames.silent_planet.model.fractions.factionType;

import com.silentgames.silent_planet.model.fractions.Fractions;
import com.silentgames.silent_planet.model.fractions.FractionsEnum;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class Aliens extends Fractions {

    private static Aliens instance;

    public static synchronized Aliens getInstance(){
        if(instance == null){
            instance = new Aliens();
        }
        return instance;
    }

    private Aliens() {
        this.setFractionsEnum(FractionsEnum.Aliens);
    }
}
