package com.silentgames.silent_planet.model.entities.ground;

import com.silentgames.silent_planet.model.entities.EntityTypeEx;

/**
 * Created by gidroshvandel on 10.07.16.
 */
public class Player extends EntityTypeEx {

    private int crystals = 0;

    private String playerName;

    public int getCrystals() {
        return crystals;
    }

    public void setCrystals(int crystals) {
        this.crystals = crystals;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

}
