package com.silentgames.silent_planet.mvp.main;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.logic.TurnHandler;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.cells.defaultCell.GroundCellDef;
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceCellDef;
import com.silentgames.silent_planet.model.cells.onVisible.DeadCell;
import com.silentgames.silent_planet.model.cells.onVisible.SpaceCell;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.model.entities.ground.Player;
import com.silentgames.silent_planet.model.entities.ground.PlayersOnCell;
import com.silentgames.silent_planet.model.entities.ground.fractions.Alien;
import com.silentgames.silent_planet.model.entities.ground.fractions.Human;
import com.silentgames.silent_planet.model.entities.ground.fractions.Pirate;
import com.silentgames.silent_planet.model.entities.ground.fractions.Robot;
import com.silentgames.silent_planet.model.entities.space.fractions.AlienShip;
import com.silentgames.silent_planet.model.entities.space.fractions.HumanShip;
import com.silentgames.silent_planet.model.entities.space.fractions.PirateShip;
import com.silentgames.silent_planet.model.entities.space.fractions.RobotShip;
import com.silentgames.silent_planet.model.fractions.factionType.Aliens;
import com.silentgames.silent_planet.model.fractions.factionType.Humans;
import com.silentgames.silent_planet.model.fractions.factionType.Pirates;
import com.silentgames.silent_planet.model.fractions.factionType.Robots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private MainViewModel viewModel;

    private Cell[][] gameMatrix;

    public MainPresenter(MainContract.View view, MainViewModel viewModel) {
        this.view = view;
        this.viewModel = viewModel;
    }

    @Override
    public void onSingleTapConfirmed(int x, int y) {
        viewModel.setOldXY(select(x,y, viewModel.getOldXY(), null));
    }

    @Override
    public void onCellListItemSelectedClick(int x, int y, String text) {
        view.showToast("Выбран: " + text);
        viewModel.setOldXY(select(x,y, null, text));
    }

    @Override
    public void onCreate() {
        fillBattleGround();
    }

    public void fillBattleGround(){
        int CountOfCells = Constants.getHorizontalCountOfCells();
        gameMatrix = new Cell[Constants.getVerticalCountOfCells()+1][Constants.getHorizontalCountOfCells()+1];
        for(int x=0;x< CountOfCells +1;x++) {
            for (int y = 0; y < CountOfCells + 1; y++){
                if(x==0 || x == CountOfCells - 1 || y == 0 || y == CountOfCells - 1){
                    gameMatrix[x][y] = new Cell(new CellType(new SpaceCellDef(view.getResources())), null);
                    gameMatrix[x][y].getCellType().setOnVisible(new SpaceCell(view.getResources()));

                }
                else {
                    gameMatrix[x][y] = new Cell(new CellType(new GroundCellDef(view.getResources())), null);
                    gameMatrix[x][y].getCellType().setOnVisible(new DeadCell(view.getResources()));
                }
            }
        }
        spawnShips();
        view.drawBattleGround(gameMatrix);
    }

    private void spawnShips(){
        spawnHumans(0,0);
        spawnPirates(0,1);
        spawnRobots(0,2);
        spawnAliens(0,3);
        TurnHandler.start(Humans.getInstance());
        TurnHandler.setPlayable(Aliens.getInstance());
        TurnHandler.setPlayable(Humans.getInstance());
        TurnHandler.setPlayable(Pirates.getInstance());
        TurnHandler.setPlayable(Robots.getInstance());
    }

    public void spawnRobots(int x, int y){
        PlayersOnCell playerList = new PlayersOnCell();
        playerList.add(new Robot(view.getResources(), "Maxim"));
        playerList.add(new Robot(view.getResources(), "Oxik"));
        playerList.add(new Robot(view.getResources(), "Andrea"));
        gameMatrix[x][y].setEntityType(new EntityType(new RobotShip(view.getResources())));
        gameMatrix[x][y].getEntityType().setPlayersOnCell(playerList);
    }

    public void spawnAliens(int x, int y){
        PlayersOnCell playerList = new PlayersOnCell();
        playerList.add(new Alien(view.getResources(), "Maxim"));
        playerList.add(new Alien(view.getResources(), "Oxik"));
        playerList.add(new Alien(view.getResources(), "Andrea"));
        gameMatrix[x][y].setEntityType(new EntityType(new AlienShip(view.getResources())));
        gameMatrix[x][y].getEntityType().setPlayersOnCell(playerList);
    }
    public void spawnPirates(int x, int y){
        PlayersOnCell playerList = new PlayersOnCell();
        playerList.add(new Pirate(view.getResources(), "Maxim"));
        playerList.add(new Pirate(view.getResources(), "Oxik"));
        playerList.add(new Pirate(view.getResources(), "Andrea"));
        gameMatrix[x][y].setEntityType(new EntityType(new PirateShip(view.getResources())));
        gameMatrix[x][y].getEntityType().setPlayersOnCell(playerList);
    }
    public void spawnHumans(int x, int y){
        PlayersOnCell playerList = new PlayersOnCell();

        playerList.add(new Human(view.getResources(), "Maxim"));
        playerList.add(new Human(view.getResources(), "Oxik"));
        playerList.add(new Human(view.getResources(), "Andrea"));
        gameMatrix[x][y].setEntityType(new EntityType(new HumanShip(view.getResources())));
        gameMatrix[x][y].getEntityType().setPlayersOnCell(playerList);
    }

    private List<String> findPlayer(final int x, final int y){
        List<String> data = new ArrayList<>();

        if(gameMatrix[x][y].getEntityType().getPlayersOnCell() != null && gameMatrix[x][y].getEntityType().getPlayersOnCell().getPlayerList() != null) {
            for (Player player : gameMatrix[x][y].getEntityType().getPlayersOnCell().getPlayerList()) {
                data.add(player.getPlayerName());
            }
        }
        return data;
    }

    public Map<String,String> select(int x, int y, Map<String,String> oldXY, String name){
        EntityType en = gameMatrix[x][y].getEntityType();

        if(oldXY == null && !Constants.block) {
            if (en != null) {
                oldXY = new HashMap<>();
                if(name == null){
                    view.showCellListItem(x, y, findPlayer(x,y));
                }
                if(name != null){
                    oldXY.put("name",name);
                }
                oldXY.put("X",String.valueOf(x));
                oldXY.put("Y",String.valueOf(y));
                view.showObjectIcon(gameMatrix[x][y]);
                return oldXY;
            }
            else {
                view.showObjectIcon(gameMatrix[x][y]);
                return null;
            }
        }
        else {
            Cell[][] newGameMatrix = new EntityMove(gameMatrix).canMove(x,y,oldXY);
            if(newGameMatrix != null){
                view.drawBattleGround(newGameMatrix);
                view.showToast("Now turn " + TurnHandler.getFraction().toString());
            }
            if(Constants.block){
                return oldXY;
            }else {
                return null;
            }
        }

    }
}
