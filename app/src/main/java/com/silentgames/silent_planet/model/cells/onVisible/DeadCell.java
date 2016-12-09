package com.silentgames.silent_planet.model.cells.onVisible;

import android.content.res.Resources;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer;
import com.silentgames.silent_planet.utils.BitmapEditor;

public class DeadCell extends OnVisible {

    public DeadCell(Resources res) {
        super.setBitmap(BitmapEditor.getCellBitmap(R.drawable.dead_cell,res));
        super.setDead(true);
        super.setCanMove(true);
//        super.setCrystals(1);
    }


    @Override
    public Cell[][] doEvent(int x, int y, Cell[][] gameMatrix) {

        if(gameMatrix[x][y].getCellType().isDead() && ! gameMatrix[x][y].getEntityType().isDead()){
            PlayersOnCell playerList = new PlayersOnCell();
            playerList.add(new DeadPlayer( gameMatrix[x][y].getEntityType().getPlayersOnCell().getPlayerList().get(0)));
            gameMatrix[x][y].setEntityType(new EntityType(playerList));
        }
        return gameMatrix;
    }

}
