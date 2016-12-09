package com.silentgames.silent_planet.model.fractions.factionType;

import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate;
import com.silentgames.silent_planet.model.fractions.Fractions;
import com.silentgames.silent_planet.model.fractions.FractionsEnum;

/**
 * Created by gidroshvandel on 27.09.16.
 */
public class Pirates extends Fractions {

    private static Pirates instance;

    public static synchronized Pirates getInstance(){
        if(instance == null){
            instance = new Pirates();
        }
        return instance;
    }

    private Pirates() {
        this.setFractionsEnum(FractionsEnum.Pirates);
    }
}
