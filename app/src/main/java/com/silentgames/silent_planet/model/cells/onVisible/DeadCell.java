package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;

import com.silentgames.silent_planet.App;
import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer;
import com.silentgames.silent_planet.utils.BitmapEditor;

public class DeadCell extends OnVisible {

    public DeadCell() {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.dead_cell));
        super.setDead(true);
        super.setCanMove(true);
    }


    @Override
    public GameMatrixHelper doEvent(GameMatrixHelper gameMatrixHelper) {

        Cell gameMatrixCell = gameMatrixHelper.getGameMatrixCellByXY();

        if(gameMatrixCell.getCellType().isDead() && ! gameMatrixCell.getEntityType().isDead()){
            PlayersOnCell playerList = new PlayersOnCell();
            playerList.add(new DeadPlayer( gameMatrixCell.getEntityType().getPlayersOnCell().getPlayerList().get(0)));
            gameMatrixCell.setEntityType(new EntityType(playerList));
        }

        gameMatrixHelper.setGameMatrixCellByXY(gameMatrixCell);

        return gameMatrixHelper;
    }

}
