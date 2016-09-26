package com.silentgames.silent_planet.logic;

/**
 * Created by gidroshvandel on 26.09.16.
 */
public class TurnHandler {

    public TurnHandler() {
    }

    public static void turnCount(){
        int turnCount = Constants.getTurnCount();
        Constants.setTurnCount(turnCount ++);
        nextPlayer();
    }

    public static void start(Fractions fraction){
        Constants.setFraction(fraction);
    }

    private static void nextPlayer(){
        Constants.setFraction(Constants.getFraction().next());
    }
}
