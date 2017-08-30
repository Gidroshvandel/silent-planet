package com.silentgames.silent_planet.model.entities.space;

import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.EntityTypeEx;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class SpaceShip extends EntityTypeEx {

    private int crystals = 0;

    public int getCrystals() {
        return crystals;
    }

    public void setCrystals(int crystals) {
        this.crystals = crystals;
    }
}
