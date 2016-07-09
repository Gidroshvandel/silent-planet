package com.silentgames.silent_planet.utils;

import android.graphics.Bitmap;

import com.silentgames.silent_planet.logic.Constants;

/**
 * Created by gidroshvandel on 07.07.16.
 */
public class Calculator {

    public static float CellCenterNumeratorPoint(float cell, int viewSize){
        int lineCountOfCells = Constants.getHorizontalCountOfCells();
        return ((1f/(2* lineCountOfCells))*viewSize+(1f/ lineCountOfCells)*cell*viewSize);
    }

    public static float CellCenterNumeratorSquare(float cell, int viewSize, Bitmap bitmap){
        return CellCenterNumeratorPoint(cell, viewSize)- bitmap.getWidth()/2;
    }

}
