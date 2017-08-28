package com.silentgames.silent_planet.model.cells.onVisible.Arrows;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.GameMatrixHelper;
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
    public GameMatrixHelper doEvent(GameMatrixHelper gameMatrixHelper) {
        EntityMove entityMove = new EntityMove(gameMatrixHelper);
        if (gameMatrixHelper.getGameMatrix()[getDestinationX()][getDestinationY()].getCellType().getDefault() == null){
            return gameMatrixHelper;
        }else if (gameMatrixHelper.getGameMatrix()[getDestinationX()][getDestinationY()].getCellType().getDefault().getClass() == SpaceDef.class ||
                gameMatrixHelper.getGameMatrix()[getDestinationX()][getDestinationY()].getCellType().getOnVisible().getClass() == SpaceCell.class){
            Map<String,Integer> oldXY = new HashMap<>();
            oldXY.put("X", gameMatrixHelper.getX());
            oldXY.put("Y", gameMatrixHelper.getY());
            gameMatrixHelper.setOldXY(oldXY);
            entityMove.moveOnBoardAllyShip();
        }else {
            Map<String,Integer> oldXY = new HashMap<>();
            oldXY.put("X", gameMatrixHelper.getX());
            oldXY.put("Y", gameMatrixHelper.getY());
            gameMatrixHelper.setOldXY(oldXY);
            gameMatrixHelper.setX(getDestinationX());
            gameMatrixHelper.setY(getDestinationY());
            gameMatrixHelper.setEventMove(true);
            entityMove.movePlayer();
        }
        return gameMatrixHelper;
    }

}
