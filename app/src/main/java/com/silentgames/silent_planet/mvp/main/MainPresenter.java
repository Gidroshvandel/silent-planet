package com.silentgames.silent_planet.mvp.main;

import com.silentgames.silent_planet.App;
import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.logic.TurnHandler;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.GameMatrixHelper;
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
        select(x,y, viewModel.getGameMatrixHelper().getOldXY(), null);
    }

    @Override
    public void onCellListItemSelectedClick(int x, int y, String text) {
        view.showToast(App.getContext().getResources().getString(R.string.selectPlayer) + " " + text);
        select(x,y, null, text);
    }

    @Override
    public void onCreate() {
        GameMatrixHelper gameMatrixHelper = new GameMatrixHelper();
        gameMatrixHelper.setEventMove(true);
        viewModel.setGameMatrixHelper(gameMatrixHelper);
        viewModel.getGameMatrixHelper().setGameMatrix(model.fillBattleGround());
        view.drawBattleGround(viewModel.getGameMatrixHelper().getGameMatrix());
    }

    private void select(int x, int y, Map<String,Integer> oldXY, String name){
        viewModel.getGameMatrixHelper().setX(x);
        viewModel.getGameMatrixHelper().setY(y);

        EntityType en = viewModel.getGameMatrixHelper().getGameMatrixCellByXY().getEntityType();

        if(oldXY == null) {
            if (en != null) {
                oldXY = new HashMap<>();
                if(name == null){
                    view.showCellListItem(x, y, model.findPlayerOnCell(viewModel.getGameMatrixHelper().getGameMatrixCellByXY()));
                }
                if(name != null){
                    viewModel.getGameMatrixHelper().setPlayerName(name);
                }
                oldXY.put("X",x);
                oldXY.put("Y",y);
                view.showObjectIcon(viewModel.getGameMatrixHelper().getGameMatrixCellByXY());
                viewModel.getGameMatrixHelper().setOldXY(oldXY);
            }
            else {
                view.showObjectIcon(viewModel.getGameMatrixHelper().getGameMatrixCellByXY());
                view.hideCellListItem();
                viewModel.getGameMatrixHelper().setOldXY(null);
                viewModel.getGameMatrixHelper().setPlayerName(null);
            }
        }
        else {
            GameMatrixHelper newGameMatrix = new EntityMove(viewModel.getGameMatrixHelper()).canMove();
            if(newGameMatrix != null){
                viewModel.setGameMatrixHelper(newGameMatrix);
                view.drawBattleGround(viewModel.getGameMatrixHelper().getGameMatrix());
                view.showToast(App.getContext().getResources().getString(R.string.turnMessage) + " " + TurnHandler.getFraction().toString());
            }
            viewModel.getGameMatrixHelper().setOldXY(null);
            viewModel.getGameMatrixHelper().setPlayerName(null);
        }
    }
}
