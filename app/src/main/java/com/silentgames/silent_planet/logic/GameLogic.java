package com.silentgames.silent_planet.logic;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.silentgames.silent_planet.MainActivity;
import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.ClassType;
import com.silentgames.silent_planet.model.cells.onVisible.DeadCell;
import com.silentgames.silent_planet.model.cells.ground.GroundClass;
import com.silentgames.silent_planet.model.cells.onVisible.OnVisible;
import com.silentgames.silent_planet.model.cells.space.SpaceClass;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.space.SpaceShip;
import com.silentgames.silent_planet.view.GameView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        fillBattleGround();
        Player player = new Player(view.getResources(), "123");

        Constants.getHorizontalCountOfCells();
        Constants constants = new Constants(view.getContext());

    }


    public void fillBattleGround(){
        int CountOfCells = Constants.getHorizontalCountOfCells();
        gameMatrix = new Cell[Constants.getVerticalCountOfCells()+1][Constants.getHorizontalCountOfCells()+1];
        for(int x=0;x< CountOfCells +1;x++) {
            for (int y = 0; y < CountOfCells + 1; y++){
                if(x==0 || x == CountOfCells - 1 || y == 0 || y == CountOfCells - 1){
                    gameMatrix[x][y] = new Cell(new ClassType(new SpaceClass(view.getResources())), null);
                    gameMatrix[x][y].getCellType().setOnVisible(new OnVisible(new SpaceClass(view.getResources())));
//                    gameMatrix[x][y].getCellType().setOnVisible(new OnVisible(new SpaceClass(view.getResources())));

                }
                else {
                    gameMatrix[x][y] = new Cell(new ClassType(new GroundClass(view.getResources())), null);
                    gameMatrix[x][y].getCellType().setOnVisible(new OnVisible(new DeadCell(view.getResources())));
                }
            }
        }
        spawnShips();
        view.drawBattleGround(gameMatrix);
    }

    public void spawnShips(){
        List<Player> playerList = new ArrayList<>();
        playerList.add(new Player(view.getResources(), "Maxim"));
        playerList.add(new Player(view.getResources(), "Maxim2"));
        playerList.add(new Player(view.getResources(), "Maxim3"));
        gameMatrix[0][0].setEntityType(new EntityType(new SpaceShip(view.getResources())));
        gameMatrix[0][1].setEntityType(new EntityType(playerList));
    }


    private void onCellList(final int x, final int y){
        List<String> data = new ArrayList<>();

        if(gameMatrix[x][y].getEntityType().getPlayerList() != null) {
            for (Player player : gameMatrix[x][y].getEntityType().getPlayerList()) {
                data.add(player.getPlayerName());
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, data );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) activity.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        spinner.setPrompt("Title");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                Toast.makeText(activity.getBaseContext(), "Выбран: " + adapter.getItem(position), Toast.LENGTH_SHORT).show();
//                Player player = gameMatrix[x][y].getEntityType().getSpaceShip().getPlayerByName(adapter.getItem(position));
                Constants.oldXY = select(x,y, null, adapter.getItem(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public Map<String,String> select(int x, int y, Map<String,String> oldXY, String name){
        ImageView viewIm=(ImageView)activity.findViewById(R.id.imageView);
        EntityType en = gameMatrix[x][y].getEntityType();

        if(oldXY == null) {
            if (en != null) {
                oldXY = new HashMap<>();
                if(isSpaceShip(x,y) && name == null){
                    onCellList(x,y);
                }
                if(isSpaceShip(x,y) && name != null){
                    oldXY.put("name",name);
                }
                oldXY.put("X",String.valueOf(x));
                oldXY.put("Y",String.valueOf(y));
                viewIm.setImageBitmap(gameMatrix[x][y].getEntityType().getBitmap());
                return oldXY;
            }
            else {
                viewIm.setImageBitmap(gameMatrix[x][y].getCellType().getBitmap());

                return null;
            }
        }
        else {
            canMove(x,y, oldXY);
            return null;
        }

    }

    public void canMove(int x, int y, Map<String,String> oldXY){
        int oldX = Integer.parseInt(oldXY.get("X"));
        int oldY = Integer.parseInt(oldXY.get("Y"));

        if(isMoveAtDistance(x, y, oldXY)) {
            if(isSpaceShip(oldX, oldY)) {
                if(gameMatrix[oldX][oldY].getEntityType().isCanFly() == gameMatrix[x][y].getCellType().isCanFly()){
                    moveEntity(x, y, oldXY);
                }else
                if(oldXY.get("name") != null){
                    moveFromBoard(x, y, oldXY);
                }
            }
            if(isPlayer(oldX, oldY)) {
                if(gameMatrix[oldX][oldY].getEntityType().isCanMove() == gameMatrix[x][y].getCellType().isCanMove()){
                    moveEntity(x, y, oldXY);
                }else
                if(isSpaceShip(x, y)){
                    moveOnBoard(x, y, oldXY);
                }
            }
            view.drawBattleGround(gameMatrix);
        }
    }

    public void moveOnBoard(int x, int y, Map<String,String> oldXY){
        gameMatrix[x][y].getEntityType().setPlayerList(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayerList());
        gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].setEntityType(null);
    }

    public void moveFromBoard(int x, int y, Map<String,String> oldXY){
        gameMatrix[x][y].setEntityType(new EntityType(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().getPlayerList()));
        gameMatrix[x][y].setCellType(new ClassType(gameMatrix[x][y].getCellType().getOnVisible()));
        gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType().setPlayerList(null);
    }

    public void moveEntity(int x, int y, Map<String,String> oldXY){

        gameMatrix[x][y].setEntityType(gameMatrix[Integer.parseInt(oldXY.get("X"))][Integer.parseInt(oldXY.get("Y"))].getEntityType());
        gameMatrix[x][y].setCellType(new ClassType(gameMatrix[x][y].getCellType().getOnVisible()));
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
            if(gameMatrix[x][y].getEntityType().getPlayerList() != null && gameMatrix[x][y].getEntityType().getPlayerList().size() != 0){
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
