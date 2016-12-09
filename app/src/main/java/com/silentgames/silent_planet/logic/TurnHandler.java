package com.silentgames.silent_planet.logic;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.silentgames.silent_planet.model.fractions.Fractions;
import com.silentgames.silent_planet.model.fractions.FractionsEnum;

/**
 * Created by gidroshvandel on 26.09.16.
 */
public class TurnHandler {

    private static int turnCount;
    private static FractionsEnum fraction;

    public TurnHandler() {
    }

    public static void turnCount(){
        int turnCount = getTurnCount();
        setTurnCount(turnCount ++);
        nextPlayer();
    }

    public static void start(Fractions fraction){

        setFraction(fraction.getFractionsEnum());
    }

    public static void setPlayable(Fractions fraction){
        fraction.setPlayable(true);
    }

    private static void nextPlayer(){
        setFraction(getFraction().next());
    }

    public static int getTurnCount() {
        return turnCount;
    }

    public static void setTurnCount(int turnCount) {
        TurnHandler.turnCount = turnCount;
    }

    public static FractionsEnum getFraction() {
        return fraction;
    }

    public static void setFraction(FractionsEnum fraction) {
        TurnHandler.fraction = fraction;
    }
}
