package com.silentgames.silent_planet.logic;

import android.graphics.Bitmap;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by gidroshvandel on 23.09.16.
 */
public class EntityMove {

    private Cell[][] gameMatrix;

    public EntityMove(Cell[][] gameMatrix) {
        this.gameMatrix = gameMatrix;
    }

    public Cell[][] canMove(Boolean block, int x, int y, Map<String,String> oldXY){
        int oldX = Integer.parseInt(oldXY.get("X"));
        int oldY = Integer.parseInt(oldXY.get("Y"));
        if(block){
            gameMatrix[oldX][oldY] = gameMatrix[x][y].getCellType().getOnVisible().doEvent(gameMatrix[oldX][oldY]);
            return gameMatrix;
        }else {
            return moveCheck(x,y, oldXY);
        }
    }

    private Cell[][] moveCheck(int x, int y, Map<String,String> oldXY){
        int oldX = Integer.parseInt(oldXY.get("X"));
        int oldY = Integer.parseInt(oldXY.get("Y"));
        if (isMoveAtDistance(x, y, oldX, oldY) && isPlayable(oldX, oldY) && isCurrentPlayer(oldX, oldY)) {
            if (isSpaceShip(oldX, oldY) && gameMatrix[oldX][oldY].getEntityType().isCanFly() && !isSpaceShip(x, y)) {
                if (isCanFlyToCell(x, y, oldX, oldY)) {
                    moveShip(x, y, oldX, oldY);
                    TurnHandler.turnCount();
                } else if (oldXY.get("name") != null) {
                    moveFromBoard(x, y, oldXY);
                    gameMatrix[x][y] = gameMatrix[x][y].getCellType().getOnVisible().doEvent(gameMatrix[x][y]);
//                    TurnHandler.turnCount();
                }
            } else if (isPlayer(oldX, oldY)) {
                if (isCanMovePlayer(oldXY)) {
                    if (isCanMovePlayer(oldXY) == gameMatrix[x][y].getCellType().isCanMove()) {
                        movePlayer(x, y, oldXY);
                        gameMatrix[x][y] = gameMatrix[x][y].getCellType().getOnVisible().doEvent(gameMatrix[x][y]);
//                        TurnHandler.turnCount();
                    } else if (isSpaceShip(x, y) && isEntityBelongFraction(x, y, oldX, oldY)) {
                        moveOnBoard(x, y, oldXY);
                    }
                }
            }
            return gameMatrix;
        } else {
            return null;
        }
    }

    private Boolean isEntityBelongFraction(int x, int y, int oldX, int oldY){
        if (gameMatrix[x][y].getEntityType().getFraction() == gameMatrix[oldX][oldY].getEntityType().getFraction()){
            return true;
        }else {
            return false;
        }
    }

    private Boolean isCanMovePlayer(Map<String,String> oldXY){
        int oldX = Integer.parseInt(oldXY.get("X"));
        int oldY = Integer.parseInt(oldXY.get("Y"));
        if(gameMatrix[oldX][oldY].getEntityType().getPlayersOnCell().getPlayerByName(oldXY.get("name")).isCanMove()){
            return true;
        }else {
            return false;
        }
    }

    private Boolean isCanFlyToCell(int x, int y, int oldX, int oldY){
       if(gameMatrix[oldX][oldY].getEntityType().isCanFly() == gameMatrix[x][y].getCellType().isCanFly()){
           return true;
       }else {
           return false;
       }
    }

    private Boolean isCurrentPlayer(int x, int y){
       if(getEntityType(x,y).getFraction().getFractionsEnum() == TurnHandler.getFraction()){
           return true;
       }
       else {
           return false;
       }
    }

    private Boolean isPlayable(int x, int y){
        if(getEntityType(x,y).getFraction().isPlayable()){
            return true;
        }
        else {
            return false;
        }
    }

    private void event(int x, int y){
        if(gameMatrix[x][y].getCellType().isDead() && !getEntityType(x,y).isDead()){
            PlayersOnCell playerList = new PlayersOnCell();
            playerList.add(new DeadPlayer(getEntityType(x,y).getPlayersOnCell().getPlayerList().get(0)));
            gameMatrix[x][y].setEntityType(new EntityType(playerList));
        }
    }

    public void moveOnBoard(int x, int y, Map<String,String> oldXY){
        getEntityType(x,y).getPlayersOnCell().add(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().getPlayerByName(oldXY.get("name")));
        deletePlayer(oldXY);
    }

    public void moveFromBoard(int x, int y, Map<String,String> oldXY){
        PlayersOnCell selectPlayer = new PlayersOnCell();
        selectPlayer.add(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().getPlayerByName(oldXY.get("name")));

        gameMatrix[x][y].addEntityType(new EntityType(selectPlayer));
        gameMatrix[x][y].setCellType(new CellType(gameMatrix[x][y].getCellType().getOnVisible()));
        deletePlayerOnBoard(oldXY);
    }

    private void deleteEntity(int x, int y){
        gameMatrix[x][y].setEntityType(null);
    }

    private void deletePlayer(Map<String,String> oldXY){
        if(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().getPlayerList().size() == 1){
            gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].setEntityType(null);
        }else {
            gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().removePlayerByName(oldXY.get("name"));
        }
    }

    private void deletePlayerOnBoard(Map<String,String> oldXY){
        if(getEntityType(Integer.parseInt(oldXY.get("X")),Integer.parseInt(oldXY.get("Y"))).getPlayersOnCell().getPlayerList().size() == 1){
            getEntityType(Integer.parseInt(oldXY.get("X")),Integer.parseInt(oldXY.get("Y"))).getPlayersOnCell().setPlayerList(null);
        }else {
            getEntityType(Integer.parseInt(oldXY.get("X")),Integer.parseInt(oldXY.get("Y"))).getPlayersOnCell().removePlayerByName(oldXY.get("name"));
        }
    }

    public void movePlayer(int x, int y, Map<String,String> oldXY){

        PlayersOnCell selectPlayer = new PlayersOnCell();
        selectPlayer.add(getEntityType(Integer.parseInt(oldXY.get("X")),Integer.parseInt(oldXY.get("Y"))).getPlayersOnCell().getPlayerByName(oldXY.get("name")));

        gameMatrix[x][y].addEntityType(new EntityType(selectPlayer));
        gameMatrix[x][y].setCellType(new CellType(gameMatrix[x][y].getCellType().getOnVisible()));
        deletePlayer(oldXY);
    }

    public void moveShip(int x, int y, int oldX, int oldY) {
        gameMatrix[x][y].addEntityType(gameMatrix[oldX][oldY].getEntityType());
        gameMatrix[x][y].setCellType(new CellType(gameMatrix[x][y].getCellType().getOnVisible()));
        gameMatrix[oldX][oldY].setEntityType(null);
    }

    public boolean isSpaceShip(int x, int y){
        if(getEntityType(x,y) != null){
            if(getEntityType(x,y).getSpaceShip() != null){
                return true;
            }
        }
        return false;
    }

    public boolean isPlayer(int x, int y){
        if(getEntityType(x, y) != null){
            if(getEntityType(x,y).getPlayersOnCell() != null && getEntityType(x,y).getPlayersOnCell().getPlayerList().size() != 0){
                return true;
            }
        }
        return false;
    }

    private EntityType getEntityType(int x, int y){
        return gameMatrix[x][y].getEntityType();
    }

    //Проверки перемещения юнитов
    public Boolean isMoveAtDistance(int x, int y, int oldX, int oldY){

        Boolean isMoveAtDistance = false;

        if(!(oldX == x && oldY == y)) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((oldX + j - 1 == x) && (oldY + i - 1 == y)) {
                        isMoveAtDistance = true;
                    }
                }
            }
            return isMoveAtDistance;
        }
        else {
            return  isMoveAtDistance;
        }
    }
}
