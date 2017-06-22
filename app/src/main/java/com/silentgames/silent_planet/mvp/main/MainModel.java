package com.silentgames.silent_planet.mvp.main;

import android.content.res.Resources;

import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.TurnHandler;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.cells.defaultCell.GroundDef;
import com.silentgames.silent_planet.model.cells.defaultCell.SpaceDef;
import com.silentgames.silent_planet.model.cells.onVisible.Arrows.ArrowGreen;
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
import com.silentgames.silent_planet.utils.BitmapEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gidroshvandel on 22.06.17.
 */
public class MainModel {

    public Cell[][] fillBattleGround(Resources resources){
        int CountOfCells = Constants.getHorizontalCountOfCells();
        Cell[][] gameMatrix = new Cell[Constants.getVerticalCountOfCells()+1][Constants.getHorizontalCountOfCells()+1];
        for(int x=0;x< CountOfCells +1;x++) {
            for (int y = 0; y < CountOfCells + 1; y++){
                if(x==0 || x == CountOfCells - 1 || y == 0 || y == CountOfCells - 1){
                    gameMatrix[x][y] = new Cell(new CellType(new SpaceDef(resources)), null);
                    gameMatrix[x][y].getCellType().setOnVisible(new SpaceCell(resources));
                }
                else {
                    gameMatrix[x][y] = new Cell(new CellType(new GroundDef(resources)), null);

                    gameMatrix[x][y].getCellType().setOnVisible(new ArrowGreen(resources).rotate(x, y, BitmapEditor.RotateAngle.randomAngle()));
                }
            }
        }
        spawnShips(gameMatrix, resources);
        return gameMatrix;
    }

    public List<String> findPlayerOnCell(Cell gameMatrixCell){
        List<String> data = new ArrayList<>();

        if(gameMatrixCell.getEntityType().getPlayersOnCell() != null && gameMatrixCell.getEntityType().getPlayersOnCell().getPlayerList() != null) {
            for (Player player : gameMatrixCell.getEntityType().getPlayersOnCell().getPlayerList()) {
                data.add(player.getPlayerName());
            }
        }
        return data;
    }

    private void spawnShips(Cell[][] gameMatrix, Resources resources){
        spawnHumans(gameMatrix[0][0], resources);
        spawnPirates(gameMatrix[0][1], resources);
        spawnRobots(gameMatrix[0][2], resources);
        spawnAliens(gameMatrix[0][3], resources);
        TurnHandler.start(Humans.getInstance());
        TurnHandler.setPlayable(Aliens.getInstance());
        TurnHandler.setPlayable(Humans.getInstance());
        TurnHandler.setPlayable(Pirates.getInstance());
        TurnHandler.setPlayable(Robots.getInstance());
    }

    private void spawnRobots(Cell gameMatrixCell, Resources resources){
        PlayersOnCell playerList = new PlayersOnCell();
        playerList.add(new Robot(resources, "Maxim"));
        playerList.add(new Robot(resources, "Oxik"));
        playerList.add(new Robot(resources, "Andrea"));
        gameMatrixCell.setEntityType(new EntityType(new RobotShip(resources)));
        gameMatrixCell.getEntityType().setPlayersOnCell(playerList);
    }

    private void spawnAliens(Cell gameMatrixCell, Resources resources){
        PlayersOnCell playerList = new PlayersOnCell();
        playerList.add(new Alien(resources, "Maxim"));
        playerList.add(new Alien(resources, "Oxik"));
        playerList.add(new Alien(resources, "Andrea"));
        gameMatrixCell.setEntityType(new EntityType(new AlienShip(resources)));
        gameMatrixCell.getEntityType().setPlayersOnCell(playerList);
    }
    private void spawnPirates(Cell gameMatrixCell, Resources resources){
        PlayersOnCell playerList = new PlayersOnCell();
        playerList.add(new Pirate(resources, "Maxim"));
        playerList.add(new Pirate(resources, "Oxik"));
        playerList.add(new Pirate(resources, "Andrea"));
        gameMatrixCell.setEntityType(new EntityType(new PirateShip(resources)));
        gameMatrixCell.getEntityType().setPlayersOnCell(playerList);
    }
    private void spawnHumans(Cell gameMatrixCell, Resources resources){
        PlayersOnCell playerList = new PlayersOnCell();

        playerList.add(new Human(resources, "Maxim"));
        playerList.add(new Human(resources, "Oxik"));
        playerList.add(new Human(resources, "Andrea"));
        gameMatrixCell.setEntityType(new EntityType(new HumanShip(resources)));
        gameMatrixCell.getEntityType().setPlayersOnCell(playerList);
    }
}
