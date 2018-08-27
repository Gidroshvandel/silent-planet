package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef;
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell;
import com.silentgames.silent_planet.utils.BitmapEditor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gidroshvandel on 09.12.16.
 */
public abstract class Arrow<T> extends OnVisible {

    private BitmapEditor.RotateAngle rotateAngle;
    private int destinationX;
    private int destinationY;
    private int distance;

    public Arrow<T> rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle){
        switch (rotateAngle){
            case DEGREES0:
                setDestinationX(x + distance);
                setDestinationY(y - distance);
                break;
            case DEGREES90:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90,getBitmap()));
                setDestinationX(x + distance);
                setDestinationY(y + distance);
                break;
            case DEGREES180:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180,getBitmap()));
                setDestinationX(x - distance);
                setDestinationY(y + distance);
                break;
            case DEGREES270:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270,getBitmap()));
                setDestinationX(x - distance);
                setDestinationY(y - distance);
                break;
        }
        setRotateAngle(rotateAngle);
        return this;
    }

    @Override
    public GameMatrixHelper doEvent(GameMatrixHelper gameMatrixHelper) {
        EntityMove entityMove = new EntityMove(gameMatrixHelper);
        if(checkBorders()) {
            if (gameMatrixHelper.getGameMatrix()[getDestinationX()][getDestinationY()].getCellType().getDefault() != null &&
                    gameMatrixHelper.getGameMatrix()[getDestinationX()][getDestinationY()].getCellType().getDefault().getClass() == SpaceDef.class ||
                    gameMatrixHelper.getGameMatrix()[getDestinationX()][getDestinationY()].getCellType().getOnVisible().getClass() == SpaceCell.class) {
                Map<String, Integer> oldXY = new HashMap<>();
                oldXY.put("X", gameMatrixHelper.getX());
                oldXY.put("Y", gameMatrixHelper.getY());
                gameMatrixHelper.setOldXY(oldXY);
                entityMove.moveOnBoardAllyShip();
            } else {
                Map<String, Integer> oldXY = new HashMap<>();
                oldXY.put("X", gameMatrixHelper.getX());
                oldXY.put("Y", gameMatrixHelper.getY());
                gameMatrixHelper.setOldXY(oldXY);
                gameMatrixHelper.setX(getDestinationX());
                gameMatrixHelper.setY(getDestinationY());
                gameMatrixHelper.setEventMove(true);
                entityMove.movePlayer();
            }
        }else {
            Map<String, Integer> oldXY = new HashMap<>();
            oldXY.put("X", gameMatrixHelper.getX());
            oldXY.put("Y", gameMatrixHelper.getY());
            gameMatrixHelper.setOldXY(oldXY);
            entityMove.moveOnBoardAllyShip();
        }
        return gameMatrixHelper;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDestinationX() {
        return destinationX;
    }

    public BitmapEditor.RotateAngle getRotateAngle() {
        return rotateAngle;
    }

    public void setRotateAngle(BitmapEditor.RotateAngle rotateAngle) {
        this.rotateAngle = rotateAngle;
    }

    public void setDestinationX(int destinationX) {
        this.destinationX = destinationX;
    }

    public int getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(int destinationY) {
        this.destinationY = destinationY;
    }

    private boolean checkBorders(){
        if (getDestinationX() <= Constants.getVerticalCountOfCells() &&
                getDestinationX() >= 0 &&
                getDestinationY() <= Constants.getHorizontalCountOfCells() &&
                getDestinationY() >= 0){
            return true;
        }else{
            return false;
        }
    }
}
