package com.silentgames.silent_planet.logic;

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

    public Cell[][] canMove(int x, int y, Map<String,String> oldXY){
        int oldX = Integer.parseInt(oldXY.get("X"));
        int oldY = Integer.parseInt(oldXY.get("Y"));

        if(isMoveAtDistance(x, y, oldXY) && isCurrentPlayer(oldX, oldY)) {
            if(isSpaceShip(oldX, oldY) && gameMatrix[oldX][oldY].getEntityType().isCanFly()) {
                if(gameMatrix[oldX][oldY].getEntityType().isCanFly() == gameMatrix[x][y].getCellType().isCanFly()){
                    moveShip(x, y, oldXY);
                }else
                if(oldXY.get("name") != null){
                    moveFromBoard(x, y, oldXY);
                    event(x,y);
                }
            }else
            if(isPlayer(oldX, oldY)) {
                Boolean s = gameMatrix[oldX][oldY].getEntityType().getPlayersOnCell().getPlayerByName(oldXY.get("name")).isCanMove();
                if(s) {
                    if (s == gameMatrix[x][y].getCellType().isCanMove()) {
                        movePlayer(x, y, oldXY);
                        event(x, y);
                    } else if (isSpaceShip(x, y)) {
                        moveOnBoard(x, y, oldXY);
                    }
                }
            }
            return gameMatrix;
        }
        else {
            return null;
        }
    }

    private Boolean isCurrentPlayer(int x, int y){
       if(gameMatrix[x][y].getEntityType().getFraction() == Constants.getFraction()){
           return true;
       }
       else {
           return false;
       }
    }

    private void event(int x, int y){
        if(gameMatrix[x][y].getCellType().isDead() && !gameMatrix[x][y].getEntityType().isDead()){
            PlayersOnCell playerList = new PlayersOnCell();
            playerList.add(new DeadPlayer(gameMatrix[x][y].getEntityType().getPlayersOnCell().getPlayerList().get(0)));
            gameMatrix[x][y].setEntityType(new EntityType(playerList));
        }
    }

    public void moveOnBoard(int x, int y, Map<String,String> oldXY){
        gameMatrix[x][y].getEntityType().getPlayersOnCell().add(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().getPlayerByName(oldXY.get("name")));
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
        if(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().getPlayerList().size() == 1){
            gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().setPlayerList(null);
        }else {
            gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().removePlayerByName(oldXY.get("name"));
        }
    }

    public void movePlayer(int x, int y, Map<String,String> oldXY){

        PlayersOnCell selectPlayer = new PlayersOnCell();
        selectPlayer.add(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayersOnCell().getPlayerByName(oldXY.get("name")));

        gameMatrix[x][y].addEntityType(new EntityType(selectPlayer));
        gameMatrix[x][y].setCellType(new CellType(gameMatrix[x][y].getCellType().getOnVisible()));
        deletePlayer(oldXY);
    }

    public void moveShip(int x, int y, Map<String,String> oldXY) {
        gameMatrix[x][y].addEntityType(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType());
        gameMatrix[x][y].setCellType(new CellType(gameMatrix[x][y].getCellType().getOnVisible()));
        gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].setEntityType(null);
    }

    public boolean isSpaceShip(int x, int y){
        if(gameMatrix[x][y].getEntityType() != null){
            if(gameMatrix[x][y].getEntityType().getSpaceShip() != null){
                return true;
            }
        }
        return false;
    }

    public boolean isPlayer(int x, int y){
        if(gameMatrix[x][y].getEntityType() != null){
            if(gameMatrix[x][y].getEntityType().getPlayersOnCell() != null && gameMatrix[x][y].getEntityType().getPlayersOnCell().getPlayerList().size() != 0){
                return true;
            }
        }
        return false;
    }

    //Проверки перемещения юнитов
    public Boolean isMoveAtDistance(int x, int y, Map<String,String> oldXY){

        Boolean isMoveAtDistance = false;

        int oldX = Integer.parseInt(oldXY.get("X"));
        int oldY = Integer.parseInt(oldXY.get("Y"));
        if(!(oldX == x && oldY == y)) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((Integer.parseInt(oldXY.get("X")) + j - 1 == x) && (Integer.parseInt(oldXY.get("Y")) + i - 1 == y)) {
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
