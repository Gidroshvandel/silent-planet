package com.silentgames.silent_planet.model.cells;

import com.silentgames.silent_planet.model.DefaultClass;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class ClassType extends DefaultClass {

    private GroundClass groundCell;
    private SpaceClass spaceCell;
    private OnVisible onVisible;

    public ClassType(OnVisible onVisible) {
        this.onVisible = onVisible;
        setBitmap(onVisible.getBitmap());
        setCanFly(onVisible.isCanFly());
        setCanMove(onVisible.isCanMove());
    }

    public ClassType(GroundClass groundCell) {
        this.groundCell = groundCell;
        setBitmap(groundCell.getBitmap());
        setCanFly(groundCell.isCanFly());
        setCanMove(groundCell.isCanMove());
    }

    public ClassType(SpaceClass spaceCell) {
        this.spaceCell = spaceCell;
        setBitmap(spaceCell.getBitmap());
        setCanFly(spaceCell.isCanFly());
        setCanMove(spaceCell.isCanMove());
    }

    public OnVisible getOnVisible() {
        return onVisible;
    }

    public void setOnVisible(OnVisible onVisible) {
        this.onVisible = onVisible;
    }

    public GroundClass getGroundCell() {
        return groundCell;
    }

    public void setGroundCell(GroundClass groundCell) {
        this.groundCell = groundCell;
    }

    public SpaceClass getSpaceCell() {
        return spaceCell;
    }

    public void setSpaceCell(SpaceClass spaceCell) {
        this.spaceCell = spaceCell;
    }
}
