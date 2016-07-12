package com.silentgames.silent_planet.model.entities;

import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class EntityType extends DefaultClass {
    private SpaceShip spaceShip;
    private List<Player> playerList = new ArrayList<>();

    public EntityType(List<Player> player) {
            this.playerList = player;
            setBitmap(player.get(0).getBitmap());
            setCanMove(player.get(0).isCanMove());
            setCanFly(player.get(0).isCanFly());
    }

    public EntityType(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
        setBitmap(spaceShip.getBitmap());
        setCanMove(spaceShip.isCanMove());
        setCanFly(spaceShip.isCanFly());
    }

    public SpaceShip getSpaceShip() {
        return spaceShip;
    }

    public void setSpaceShip(SpaceShip spaceShip) {
        this.spaceShip = spaceShip;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
            this.playerList = playerList;
    }
}
