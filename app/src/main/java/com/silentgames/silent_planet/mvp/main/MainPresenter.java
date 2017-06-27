package com.silentgames.silent_planet.mvp.main;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.EntityMove;
import com.silentgames.silent_planet.logic.TurnHandler;
import com.silentgames.silent_planet.model.Cell;
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
        viewModel.setOldXY(select(x,y, viewModel.getOldXY(), null));
    }

    @Override
    public void onCellListItemSelectedClick(int x, int y, String text) {
        view.showToast(view.getResources().getString(R.string.selectPlayer) + " " + text);
        viewModel.setOldXY(select(x,y, null, text));
    }

    @Override
    public void onCreate() {
        viewModel.setBlock(false);
        viewModel.setGameMatrix(model.fillBattleGround());
        view.drawBattleGround(viewModel.getGameMatrix());
    }

    private Map<String,String> select(int x, int y, Map<String,String> oldXY, String name){
        EntityType en = viewModel.getGameMatrix()[x][y].getEntityType();

        if(oldXY == null && !viewModel.isBlock()) {
            if (en != null) {
                oldXY = new HashMap<>();
                if(name == null){
                    view.showCellListItem(x, y, model.findPlayerOnCell(viewModel.getGameMatrix()[x][y]));
                }
                if(name != null){
                    oldXY.put("name",name);
                }
                oldXY.put("X",String.valueOf(x));
                oldXY.put("Y",String.valueOf(y));
                view.showObjectIcon(viewModel.getGameMatrix()[x][y]);
                return oldXY;
            }
            else {
                view.showObjectIcon(viewModel.getGameMatrix()[x][y]);
                view.hideCellListItem();
                return null;
            }
        }
        else {
            Cell[][] newGameMatrix = new EntityMove(viewModel.getGameMatrix()).canMove(viewModel.isBlock(), x,y,oldXY);
            if(newGameMatrix != null){
                view.drawBattleGround(newGameMatrix);
                view.showToast(view.getResources().getString(R.string.turnMessage) + " " + TurnHandler.getFraction().toString());
            }
            if(viewModel.isBlock()){
                return oldXY;
            }else {
                return null;
            }
        }

    }
}
