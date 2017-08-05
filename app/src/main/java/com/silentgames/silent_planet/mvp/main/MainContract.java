package com.silentgames.silent_planet.mvp.main;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.mvp.BasePresenter;

import java.util.List;

public interface MainContract {
    interface View {

        void drawGrid();

        void drawBattleGround(Cell[][] gameMatrix);

        void reDraw(int eventX,int eventY, Bitmap entity);

        void showToast(String text);

        void showObjectIcon(Cell gameCell);

        void showCellListItem(int x, int y, List<String> playerList);

        void hideCellListItem();

    }
    interface Presenter extends BasePresenter {

        void onSingleTapConfirmed(int x, int y);

        void onCellListItemSelectedClick(int x, int y, String text);

    }
}
