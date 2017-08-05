package com.silentgames.silent_planet.model;

import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class Cell {

    private CellType cellType;
    private EntityType entityType;

    public Cell(CellType cellType, EntityType entityType) {
        this.cellType = cellType;
        this.entityType = entityType;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
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
            if (entityType.getPlayersOnCell().getPlayerList() != null) {
                List<Player> playerList = new ArrayList<>();
                for (Player newPlayer : entityType.getPlayersOnCell().getPlayerList()
                        ) {
                    playerList.add(newPlayer);
                }
                for (Player thisPlayer : this.entityType.getPlayersOnCell().getPlayerList()
                        ) {
                    playerList.add(thisPlayer);
                }
                this.entityType.getPlayersOnCell().setPlayerList(playerList);
            }
        }
    }
}
