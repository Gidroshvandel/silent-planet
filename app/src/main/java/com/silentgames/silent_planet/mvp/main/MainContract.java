package com.silentgames.silent_planet.mvp.main;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.mvp.BasePresenter;

public interface MainContract {
    interface View {

        void drawGrid();

        void drawBattleGround(Cell[][] gameMatrix);

        void reDraw(int eventX,int eventY, Bitmap entity);

        Resources getResources();

        Activity getActivity();

    }
    interface Presenter extends BasePresenter {

        void onSingleTapConfirmed(int x, int y);

    }
}
