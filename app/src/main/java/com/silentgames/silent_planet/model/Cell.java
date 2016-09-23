package com.silentgames.silent_planet.model;

import com.silentgames.silent_planet.model.cells.ClassType;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class Cell {

    ClassType cellType;
    EntityType entityType;

    public Cell(ClassType cellType, EntityType entityType) {
        this.cellType = cellType;
        this.entityType = entityType;
    }

    public ClassType getCellType() {
        return cellType;
    }

    public void setCellType(ClassType cellType) {
        this.cellType = cellType;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public void addEntityType(EntityType entityType) {
        if(this.entityType == null){
            this.entityType = entityType;
        }
        else {
            if (entityType.getSpaceShip() != null) {
                this.entityType.setSpaceShip(entityType.getSpaceShip());
            }
            if (entityType.getPlayerList() != null) {
                List<Player> playerList = new ArrayList<>();
                for (Player newPlayer : entityType.getPlayerList()
                        ) {
                    playerList.add(newPlayer);
                }
                for (Player thisPlayer : this.entityType.getPlayerList()
                        ) {
                    playerList.add(thisPlayer);
                }
                this.entityType.setPlayerList(playerList);
            }
        }
    }
}
