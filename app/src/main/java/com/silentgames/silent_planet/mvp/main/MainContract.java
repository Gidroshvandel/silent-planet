package com.silentgames.silent_planet.mvp.main;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.model.cells.CellType;
import com.silentgames.silent_planet.model.entities.EntityType;
import com.silentgames.silent_planet.mvp.BasePresenter;

import java.util.List;

public interface MainContract {
    interface View {

        void drawGrid();

        void drawBattleGround(Cell[][] gameMatrix);

        void reDraw(int eventX,int eventY, Bitmap entity);

        void showToast(String text);

        void showObjectIcon(CellType cellType);

        void showObjectIcon(EntityType entityType);

        void showCellListItem(int x, int y, List<String> playerList);

        void hideCellListItem();

        void update(Runnable runnable);

        void enableButton(boolean isEnabled);

        void setImageCrystalText(String text);

    }
    interface Presenter extends BasePresenter {

        void onSingleTapConfirmed(int x, int y);

        void onCellListItemSelectedClick(int x, int y, String text);

        void onActionButtonClick();

    }
}
