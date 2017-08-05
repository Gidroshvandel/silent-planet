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
        viewModel.setOldXY(select(x,y, viewModel.getOldXY(), null));
    }

    @Override
    public void onCellListItemSelectedClick(int x, int y, String text) {
        view.showToast(App.getContext().getResources().getString(R.string.selectPlayer) + " " + text);
        viewModel.setOldXY(select(x,y, null, text));
    }

    @Override
    public void onCreate() {
        viewModel.setGameMatrix(model.fillBattleGround());
        view.drawBattleGround(viewModel.getGameMatrix());
    }

    private Map<String,String> select(int x, int y, Map<String,String> oldXY, String name){
        EntityType en = viewModel.getGameMatrix()[x][y].getEntityType();

        if(oldXY == null) {
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
            Cell[][] newGameMatrix = new EntityMove(viewModel.getGameMatrix()).canMove(x,y,oldXY);
            if(newGameMatrix != null){
                viewModel.setGameMatrix(newGameMatrix);
                int count = 0;
                while (Constants.eventMove){
                    Constants.eventMove = false;
                    view.drawBattleGround(viewModel.getGameMatrix());
                    viewModel.setGameMatrix(new EntityMove(viewModel.getGameMatrix()).doEvent(Constants.x,Constants.y,oldXY));
                    count ++;
                    if(count > 20){
                        break;
                    }
                }
                view.drawBattleGround(viewModel.getGameMatrix());
                view.showToast(App.getContext().getResources().getString(R.string.turnMessage) + " " + TurnHandler.getFraction().toString());
            }
            return null;
        }
    }
}
