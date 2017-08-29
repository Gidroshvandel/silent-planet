package com.silentgames.silent_planet.logic;

import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef;
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;
import com.silentgames.silent_planet.model.entities.ground.utils.DeadPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gidroshvandel on 23.09.16.
 */
public class EntityMove {

    private GameMatrixHelper gameMatrixHelper;

    public EntityMove(GameMatrixHelper gameMatrixHelper) {
        this.gameMatrixHelper = gameMatrixHelper;
    }

    public GameMatrixHelper canMove(){
//        int oldX = Integer.parseInt(oldXY.get("X"));
//        int oldY = Integer.parseInt(oldXY.get("Y"));
//        if(block){
//            return gameMatrix[x][y].getCellType().getOnVisible().doEvent(oldX, oldY, gameMatrix);
//        }else {
            return moveCheck();
//        }
    }

    public GameMatrixHelper doEvent(){
        return gameMatrixHelper.getGameMatrixCellByXY().getCellType().getOnVisible().doEvent(gameMatrixHelper);
    }

    private GameMatrixHelper moveCheck(){
        int x = gameMatrixHelper.getX();
        int y = gameMatrixHelper.getY();
        int oldX = gameMatrixHelper.getOldXY().get("X");
        int oldY = gameMatrixHelper.getOldXY().get("Y");
        if (isMoveAtDistance(x, y, oldX, oldY) && isPlayable(oldX, oldY) && isCurrentPlayer(oldX, oldY)) {
            if (isSpaceShip(oldX, oldY) && gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().isCanFly() && !isSpaceShip(x, y)) {
                if (isCanFlyToCell()) {
                    moveShip();
                    TurnHandler.turnCount();
                } else if (gameMatrixHelper.getPlayerName() != null) {
                    moveFromBoard();
                    gameMatrixHelper = gameMatrixHelper.getGameMatrixCellByXY().getCellType().getOnVisible().doEvent(gameMatrixHelper);
//                    TurnHandler.turnCount();
                }
            } else if (isPlayer(oldX, oldY)) {
                if (isCanMovePlayer()) {
                    if (isCanMovePlayer() == gameMatrixHelper.getGameMatrixCellByXY().getCellType().isCanMove()) {
                        movePlayer();
                        gameMatrixHelper = gameMatrixHelper.getGameMatrixCellByXY().getCellType().getOnVisible().doEvent(gameMatrixHelper);
//                        TurnHandler.turnCount();
                    } else if (isSpaceShip(x, y) && isEntityBelongFraction()) {
                        moveOnBoard();
                    }
                }
            }
            return gameMatrixHelper;
        } else {
            return null;
        }
    }

    private Boolean isEntityBelongFraction(){
        if (gameMatrixHelper.getGameMatrixCellByXY().getEntityType().getFraction() == gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().getFraction()){
            return true;
        }else {
            return false;
        }
    }

    private Boolean isCanMovePlayer(){
        if(gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()).isCanMove()){
            return true;
        }else {
            return false;
        }
    }

    private Boolean isCanFlyToCell(){
       if(gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().isCanFly() == gameMatrixHelper.getGameMatrixCellByXY().getCellType().isCanFly()){
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
        if(gameMatrixHelper.getGameMatrixCellByXY().getCellType().isDead() && !getEntityType(x,y).isDead()){
            PlayersOnCell playerList = new PlayersOnCell();
            playerList.add(new DeadPlayer(getEntityType(x,y).getPlayersOnCell().getPlayerList().get(0)));
            gameMatrixHelper.getGameMatrixCellByXY().setEntityType(new EntityType(playerList));
        }
    }

    public void moveOnBoard(){
        getEntityTypeXY().getPlayersOnCell().add(gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()));
        deletePlayer();
    }

    public void moveOnBoardAllyShip(){
        Map<String, Integer> XY = findAllyShip();
        gameMatrixHelper.setX(XY.get("X"));
        gameMatrixHelper.setY(XY.get("Y"));
        moveOnBoard();
    }

    public Map<String, Integer> findAllyShip(){
        for (int i = 0; i < Constants.getHorizontalCountOfCells(); i++) {
            for (int j = 0; j < Constants.getVerticalCountOfCells(); j++) {
                if((gameMatrixHelper.getGameMatrix()[i][j].getCellType().getOnVisible() != null &&
                        gameMatrixHelper.getGameMatrix()[i][j].getCellType().getOnVisible().getClass() == SpaceCell.class) ||
                        (gameMatrixHelper.getGameMatrix()[i][j].getCellType().getDefault() != null &&
                                gameMatrixHelper.getGameMatrix()[i][j].getCellType().getDefault().getClass() == SpaceDef.class )){

                    if(gameMatrixHelper.getGameMatrix()[i][j].getEntityType() != null){
                        if(gameMatrixHelper.getGameMatrix()[i][j].getEntityType().getSpaceShip() != null){
                            if(gameMatrixHelper.getGameMatrix()[i][j].getEntityType().getSpaceShip().getFraction() != null){
                                if(gameMatrixHelper.getGameMatrix()[i][j].getEntityType().getSpaceShip().getFraction().getFractionsEnum() == TurnHandler.getFraction()) {
                                    Map<String, Integer> XY = new HashMap<>();
                                    XY.put("X", i);
                                    XY.put("Y", j);
                                    return XY;
                                }
                            }
                        }
                    }

                }
            }
        }
        return null;
    }

    public void moveFromBoard(){
        PlayersOnCell selectPlayer = new PlayersOnCell();
        selectPlayer.add(gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()));

        gameMatrixHelper.getGameMatrixCellByXY().addEntityType(new EntityType(selectPlayer));
        gameMatrixHelper.getGameMatrixCellByXY().setCellType(new CellType(gameMatrixHelper.getGameMatrixCellByXY().getCellType().getOnVisible()));
        deletePlayerOnBoard();
    }

    private void deleteEntity(int x, int y){
        gameMatrixHelper.getGameMatrix()[x][y].setEntityType(null);
    }

    private void deletePlayer(){
        if(gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().getPlayersOnCell().getPlayerList().size() == 1){
            gameMatrixHelper.getGameMatrixCellByOldXY().setEntityType(null);
        }else {
            gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType().getPlayersOnCell().removePlayerByName(gameMatrixHelper.getPlayerName());
        }
    }

    private void deletePlayerOnBoard(){
        if(getEntityTypeOldXY().getPlayersOnCell().getPlayerList().size() == 1){
            getEntityTypeOldXY().getPlayersOnCell().setPlayerList(null);
        }else {
            getEntityTypeOldXY().getPlayersOnCell().removePlayerByName(gameMatrixHelper.getPlayerName());
        }
    }

    public void movePlayer(){

        PlayersOnCell selectPlayer = new PlayersOnCell();
        selectPlayer.add(getEntityTypeOldXY().getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()));

        gameMatrixHelper.getGameMatrixCellByXY().addEntityType(new EntityType(selectPlayer));
        gameMatrixHelper.getGameMatrixCellByXY().setCellType(new CellType( gameMatrixHelper.getGameMatrixCellByXY().getCellType().getOnVisible()));
        deletePlayer();
    }

    public void moveShip() {
        gameMatrixHelper.getGameMatrixCellByXY().addEntityType(gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType());
        gameMatrixHelper.getGameMatrixCellByXY().setCellType(new CellType(gameMatrixHelper.getGameMatrixCellByXY().getCellType().getOnVisible()));
        gameMatrixHelper.getGameMatrixCellByOldXY().setEntityType(null);
    }

    public boolean isSpaceShip(int x, int y){
        if(getEntityType(x, y) != null){
            if(getEntityType(x, y).getSpaceShip() != null){
                return true;
            }
        }
        return false;
    }

    public boolean isPlayer(int x, int y){
        if(getEntityType(x, y) != null){
            if(getEntityType(x, y).getPlayersOnCell() != null && getEntityType(x, y).getPlayersOnCell().getPlayerList().size() != 0){
                return true;
            }
        }
        return false;
    }

    private EntityType getEntityType(int x, int y){
        return gameMatrixHelper.getGameMatrix()[x][y].getEntityType();
    }

    private EntityType getEntityTypeXY(){
        return gameMatrixHelper.getGameMatrixCellByXY().getEntityType();
    }

    private EntityType getEntityTypeOldXY(){
        return gameMatrixHelper.getGameMatrixCellByOldXY().getEntityType();
    }

    public GameMatrixHelper getCrystal(){
        EntityType entityType = gameMatrixHelper.getGameMatrixCellByXY().getEntityType();
        CellType cellType = gameMatrixHelper.getGameMatrixCellByXY().getCellType();

        if( cellType.getOnVisible().getCrystals() > 0 ){
            entityType.getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()).setCrystals(entityType.getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()).getCrystals() + 1);
            cellType.getOnVisible().setCrystals(cellType.getOnVisible().getCrystals() - 1);
        };
        return gameMatrixHelper;
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
