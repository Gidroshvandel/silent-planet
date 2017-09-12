package com.silentgames.silent_planet.mvp.main;

import com.silentgames.silent_planet.App;
import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.logic.TurnHandler;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.GameMatrixHelper;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.entities.EntityType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View view;

    private MainViewModel viewModel;

    private MainModel model;

    public MainPresenter(MainContract.View view, MainViewModel viewModel, MainModel model) {
        this.view = view;
        this.viewModel = viewModel;
        this.model = model;
    }

    @Override
    public void onSingleTapConfirmed(int x, int y) {
        select(x, y, viewModel.getGameMatrixHelper().getOldXY(), null);
    }

    @Override
    public void onCellListItemSelectedClick(int x, int y, String text) {
        view.showToast(App.getContext().getResources().getString(R.string.selectPlayer) + " " + text);
        selectEntity(x, y, text);
    }

    @Override
    public void onActionButtonClick() {
        viewModel.setGameMatrixHelper(getCrystal(viewModel.getGameMatrixHelper()));
        if (!overZeroCrystels()) {
            view.enableButton(false);
        }
        if (viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType().getSpaceShip() != null){
            view.setImageCrystalText(String.valueOf(viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType().getSpaceShip().getCrystals()));
        }else{
            if(viewModel.getGameMatrixHelper().getPlayerName() != null)
            view.setImageCrystalText(String.valueOf(viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType().getPlayersOnCell().getPlayerByName(viewModel.getGameMatrixHelper().getPlayerName()).getCrystals()));
        }
    }

    @Override
    public void onCreate() {
        GameMatrixHelper gameMatrixHelper = new GameMatrixHelper();
        gameMatrixHelper.setEventMove(false);
        viewModel.setGameMatrixHelper(gameMatrixHelper);
        viewModel.getGameMatrixHelper().setGameMatrix(model.fillBattleGround());
        viewModel.setDoubleClick(false);


        view.drawBattleGround(viewModel.getGameMatrixHelper().getGameMatrix());
        view.enableButton(false);

    }

    private void select(int x, int y, Map<String, Integer> oldXY, String name) {
        viewModel.getGameMatrixHelper().setX(x);
        viewModel.getGameMatrixHelper().setY(y);

        EntityType en = viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType();

        if (oldXY == null || isDoubleClick()) {
            if (en != null && !viewModel.isDoubleClick()) {
                selectEntity(x, y, name);
            } else {
                selectCell();
            }
        } else {
            tryMove();
        }
    }

    private boolean overZeroCrystels() {
        if (viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getCellType().getOnVisible().getCrystals() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isDoubleClick() {
        if (viewModel.getGameMatrixHelper().getOldXY() != null) {
            if (viewModel.getGameMatrixHelper().getOldXY().get("X") == viewModel.getGameMatrixHelper().getX() &&
                    viewModel.getGameMatrixHelper().getOldXY().get("Y") == viewModel.getGameMatrixHelper().getY()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void selectEntity(int x, int y, String name) {
        EntityType en = viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType();
        viewModel.setDoubleClick(true);
        if (en.getSpaceShip() != null){
            view.setImageCrystalText(String.valueOf(en.getSpaceShip().getCrystals()));
        }
        Map<String, Integer> oldXY = new HashMap<>();
        if (name == null) {
            view.showCellListItem(x, y, model.findPlayerOnCell(viewModel.getGameMatrixHelper().getGameMatrixCellByXY()));
        }
        if (name != null) {
            viewModel.getGameMatrixHelper().setPlayerName(name);
            if (en.getSpaceShip() == null){
                view.setImageCrystalText(String.valueOf(en.getPlayersOnCell().getPlayerByName(name).getCrystals()));
            }
        }
        if (overZeroCrystels()) {
            view.enableButton(true);
        } else {
            view.enableButton(false);
        }
        oldXY.put("X", x);
        oldXY.put("Y", y);
        view.showObjectIcon(viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType());
        viewModel.getGameMatrixHelper().setOldXY(oldXY);
    }

    private void selectCell() {
        view.enableButton(false);
        CellType cellType = viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getCellType();
        if(cellType.getDefault() == null){
            view.setImageCrystalText(String.valueOf(cellType.getOnVisible().getCrystals()));
        }
        view.showObjectIcon(viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getCellType());
        view.hideCellListItem();
        viewModel.getGameMatrixHelper().setOldXY(null);
        viewModel.getGameMatrixHelper().setPlayerName(null);
        viewModel.setDoubleClick(false);
    }

    private void tryMove() {
        view.enableButton(false);
        GameMatrixHelper newGameMatrix = new EntityMove(viewModel.getGameMatrixHelper()).canMove();
        if (newGameMatrix != null) {
            viewModel.setGameMatrixHelper(newGameMatrix);
            int count = 0;
            while (viewModel.getGameMatrixHelper().isEventMove()) {
                viewModel.getGameMatrixHelper().setEventMove(false);
                view.drawBattleGround(viewModel.getGameMatrixHelper().getGameMatrix());
                viewModel.setGameMatrixHelper(new EntityMove(viewModel.getGameMatrixHelper()).doEvent());
                count++;
                if (count > 20) {
                    break;
                }
            }
            view.drawBattleGround(viewModel.getGameMatrixHelper().getGameMatrix());
            view.showToast(App.getContext().getResources().getString(R.string.turnMessage) + " " + TurnHandler.getFraction().toString());
        }
        viewModel.getGameMatrixHelper().setOldXY(null);
        viewModel.getGameMatrixHelper().setPlayerName(null);
        viewModel.setDoubleClick(false);
    }

    private GameMatrixHelper getCrystal(GameMatrixHelper gameMatrixHelper){
        EntityType entityType = gameMatrixHelper.getGameMatrixCellByXY().getEntityType();
        CellType cellType = gameMatrixHelper.getGameMatrixCellByXY().getCellType();

        if( cellType.getOnVisible().getCrystals() > 0 ){
            entityType.getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()).setCrystals(entityType.getPlayersOnCell().getPlayerByName(gameMatrixHelper.getPlayerName()).getCrystals() + 1);
            cellType.getOnVisible().setCrystals(cellType.getOnVisible().getCrystals() - 1);
        };
        return gameMatrixHelper;
    }
}
