package com.silentgames.silent_planet.logic;

import android.content.Context;

import com.silentgames.silent_planet.utils.Converter;

/**
 * Created by gidroshvandel on 09.07.16.
 */
public class Constants {

    public static final int horizontalCountOfCells = 12;
    public static final int verticalCountOfCells = 12;
    public static final float mScaleFactor = 1f;
    public static final float cellImageSize = 31;
    public static final float entityImageSize = 20;
    public static final int countArrowCells = 2;
    public static final int countCrystalsToWin = 20;

    public static float getmScaleFactor() {
        return mScaleFactor;
    }

    public static int getViewSize(Context context) {
        return (int) Converter.convertDpToPixel(350, context.getResources());
    }

    public static float getCanvasSize(Context context) {
        return (int)(getViewSize(context)*mScaleFactor);
    }

}
