package com.silentgames.silent_planet.model.entities;

import com.silentgames.silent_planet.model.DefaultClass;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class EntityType extends DefaultClass {
    SpaceShip spaceShip;
    Player player;

    public EntityType(Player player) {
        this.player = player;
        setBitmap(player.getBitmap());
        setCanMove(player.isCanMove());
        setCanFly(player.isCanFly());
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
