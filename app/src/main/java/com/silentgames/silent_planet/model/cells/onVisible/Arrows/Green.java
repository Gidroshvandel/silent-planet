package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef;
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.utils.BitmapEditor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gidroshvandel on 09.12.16.
 */
public class Green extends Arrow {

    public Green() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.arrow_green_cell));
        super.setCanMove(true);
        super.setRotateAngle(BitmapEditor.RotateAngle.DEGREES0);
//        super.setCrystals(1);

    }

    @Override
    public Cell[][] doEvent(int x, int y, Cell[][] gameMatrix) {
        Cell gameMatrixCell = gameMatrix[x][y];

        EntityType entityType = gameMatrixCell.getEntityType();
        if (gameMatrix[getDestinationX()][getDestinationY()].getCellType().getaDefault() == null){
            return gameMatrix;
        }else if (gameMatrix[getDestinationX()][getDestinationY()].getCellType().getaDefault().getClass() == SpaceDef.class ||
                gameMatrix[getDestinationX()][getDestinationY()].getCellType().getOnVisible().getClass() == SpaceCell.class){
            EntityMove entityMove = new EntityMove(gameMatrix);
            Map<String,String> oldXY = new HashMap<>();
            oldXY.put("X", String.valueOf(x));
            oldXY.put("Y", String.valueOf(y));
            oldXY.put("name", gameMatrixCell.getEntityType().getPlayersOnCell().getPlayerList().get(0).getPlayerName());
            entityMove.moveOnBoardAllyShip(oldXY);
        }else {
            gameMatrix[getDestinationX()][getDestinationY()].setEntityType(entityType);
            gameMatrixCell.setEntityType(null);
            gameMatrix[x][y] = gameMatrixCell;
            Map<String,String> oldXY = new HashMap<>();
            oldXY.put("X", String.valueOf(x));
            oldXY.put("Y", String.valueOf(y));
            Constants.oldXY = oldXY;
            Constants.x = getDestinationX();
            Constants.y = getDestinationY();
            Constants.eventMove = true;
        }
        return gameMatrix;
    }

    @Override
    public Green rotate(int x, int y, BitmapEditor.RotateAngle rotateAngle) {
        switch (rotateAngle){
            case DEGREES0:
                setDestinationX(x + 1);
                setDestinationY(y - 1);
                break;
            case DEGREES90:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES90,getBitmap()));
                setDestinationX(x + 1);
                setDestinationY(y + 1);
                break;
            case DEGREES180:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES180,getBitmap()));
                setDestinationX(x - 1);
                setDestinationY(y + 1);
                break;
            case DEGREES270:
                setBitmap(BitmapEditor.rotateBitmap(BitmapEditor.RotateAngle.DEGREES270,getBitmap()));
                setDestinationX(x - 1);
                setDestinationY(y - 1);
                break;
        }
        setRotateAngle(rotateAngle);
        return this;
    }
}
