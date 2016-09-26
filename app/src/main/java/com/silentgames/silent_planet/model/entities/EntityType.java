package com.silentgames.silent_planet.model.entities;

import android.graphics.Bitmap;

import com.silentgames.silent_planet.logic.Fractions;
import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gidroshvandel on 09.07.16.
 */

public class EntityType extends EntityTypeEx {

    private PlayersOnCell playersOnCell;

    private SpaceShip spaceShip;


    public EntityType(PlayersOnCell player) {
        this.playersOnCell = player;
        setAll(player.getPlayerList().get(0));
    }

    public EntityType(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
        setAll(spaceShip);
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public PlayersOnCell getPlayersOnCell() {
        return playersOnCell;
    }

    public void setPlayersOnCell(PlayersOnCell playerList) {
        this.playersOnCell = playerList;
    }
}
