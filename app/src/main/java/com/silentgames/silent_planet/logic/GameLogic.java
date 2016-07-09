package com.silentgames.silent_planet.logic;

import com.silentgames.silent_planet.MainActivity;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.ClassType;
import com.silentgames.silent_planet.model.cells.GroundClass;
import com.silentgames.silent_planet.model.cells.SpaceClass;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.view.GameView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gidroshvandel on 05.07.16.
 */
public class GameLogic {

    private final GameView view;
    private final MainActivity activity;


    private Cell[][] gameMatrix;

    public GameLogic(GameView view, MainActivity activity){
        this.view = view;
        this.activity = activity;
        view.drawBattleGround();
        fillBattleGround();
        spawnShips();
    }

    public void fillBattleGround(){
        int CountOfCells = Constants.getHorizontalCountOfCells();
        gameMatrix = new Cell[Constants.getVerticalCountOfCells()+1][Constants.getHorizontalCountOfCells()+1];
        for(int x=0;x< CountOfCells +1;x++) {
            for (int y = 0; y < CountOfCells + 1; y++){
                if(x==0 || x == CountOfCells - 1 || y == 0 || y == CountOfCells - 1){
                    gameMatrix[x][y] = new Cell(new ClassType(new SpaceClass(view.getResources())), null);
                }
                else
                    gameMatrix[x][y] = new Cell(new ClassType(new GroundClass(view.getResources())), null);
            }
        }
    }

    public void spawnShips(){
        gameMatrix[0][0].setEntityType(new EntityType(new SpaceShip(view.getResources())));
        view.reDraw(0,0,new SpaceShip(view.getResources()).getBitmap());
        gameMatrix[0][1].setEntityType(new EntityType(new Player(view.getResources())));
        view.reDraw(0,1,new Player(view.getResources()).getBitmap());

    }

    public Map<String,Integer> selectFirst(int x, int y, Map<String,Integer> oldXY){

        EntityType en = gameMatrix[x][y].getEntityType();
        if(oldXY == null) {
            if (en != null) {
                oldXY = new HashMap<>();
                oldXY.put("X",x);
                oldXY.put("Y",y);
                return oldXY;
            }
            else {
                return null;
            }
        }
        else {
            canMove(x,y, oldXY);
            return null;
        }

    }

    public void canMove(int x, int y, Map<String,Integer> oldXY){
        boolean canMove = false;
        if(!(oldXY.get("X") == x && oldXY.get("Y") == y)){
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if ((oldXY.get("X") + j - 1 == x) && (oldXY.get("Y") + i - 1 == y)) {
                        canMove = true;
                    }
                }
            }
            if ((gameMatrix[oldXY.get("X")][oldXY.get("Y")].getEntityType().isCanFly() == gameMatrix[x][y].getCellType().isCanFly()) && canMove &&
                    ((gameMatrix[oldXY.get("X")][oldXY.get("Y")].getEntityType().isCanMove() == gameMatrix[x][y].getCellType().isCanMove()))) {
                moveEntity(x, y, oldXY);
            }
        }
    }

    public void moveEntity(int x, int y, Map<String,Integer> oldXY){

        view.reDraw(oldXY.get("X"),oldXY.get("Y"),gameMatrix[oldXY.get("X")][oldXY.get("Y")].getCellType().getBitmap());

        gameMatrix[x][y].setEntityType(gameMatrix[oldXY.get("X")][oldXY.get("Y")].getEntityType());

        view.reDraw(x,y, gameMatrix[oldXY.get("X")][oldXY.get("Y")].getEntityType().getBitmap());

        gameMatrix[oldXY.get("X")][oldXY.get("Y")].setEntityType(null);

    }





}
