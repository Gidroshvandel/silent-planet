package com.silentgames.silent_planet.model;

import com.silentgames.silent_planet.model.cells.ClassType;
import com.silentgames.silent_planet.model.entities.EntityType;

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
}
