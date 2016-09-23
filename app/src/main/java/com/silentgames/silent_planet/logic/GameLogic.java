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
//        Player player = new Player(view.getResources(), "123");

//        Constants.getHorizontalCountOfCells();
//        Constants constants = new Constants(view.getContext());
    }


    public void fillBattleGround(){
        int CountOfCells = Constants.getHorizontalCountOfCells();
        gameMatrix = new Cell[Constants.getVerticalCountOfCells()+1][Constants.getHorizontalCountOfCells()+1];
        for(int x=0;x< CountOfCells +1;x++) {
            for (int y = 0; y < CountOfCells + 1; y++){
                if(x==0 || x == CountOfCells - 1 || y == 0 || y == CountOfCells - 1){
                    gameMatrix[x][y] = new Cell(new ClassType(new SpaceClass(view.getResources())), null);
                    gameMatrix[x][y].getCellType().setOnVisible(new OnVisible(new SpaceClass(view.getResources())));

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
        playerList.add(new Player(view.getResources(), "Oxik"));
        playerList.add(new Player(view.getResources(), "Andrea"));
        gameMatrix[0][0].setEntityType(new EntityType(new SpaceShip(view.getResources())));
        gameMatrix[0][0].getEntityType().getSpaceShip().setPlayersOnBoard(playerList);
    }

    private void onCellList(final int x, final int y){


        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, findPlayer(x,y) );
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
                Constants.oldXY = select(x,y, null, adapter.getItem(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private List<String> findPlayer(final int x, final int y){
        List<String> data = new ArrayList<>();

        if(gameMatrix[x][y].getEntityType().getPlayerList() != null) {
            for (Player player : gameMatrix[x][y].getEntityType().getPlayerList()) {
                data.add(player.getPlayerName());
            }
        }
        if(gameMatrix[x][y].getEntityType().getSpaceShip() != null) {
            if (gameMatrix[x][y].getEntityType().getSpaceShip().getPlayersOnBoard() != null) {
                for (Player player : gameMatrix[x][y].getEntityType().getSpaceShip().getPlayersOnBoard()) {
                    data.add(player.getPlayerName());
                }
            }
        }
        return data;
    }

    public Map<String,String> select(int x, int y, Map<String,String> oldXY, String name){
        ImageView viewIm=(ImageView)activity.findViewById(R.id.imageView);
        EntityType en = gameMatrix[x][y].getEntityType();

        if(oldXY == null) {
            if (en != null) {
                oldXY = new HashMap<>();
                if(name == null){
                    onCellList(x,y);
                }
                if(name != null){
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
            Cell[][] mat = new EntityMove(gameMatrix).canMove(x,y,oldXY);
            if(mat != null){
                view.drawBattleGround(mat);
            }
            return null;
        }

    }

}
