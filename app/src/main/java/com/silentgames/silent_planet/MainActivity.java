package com.silentgames.silent_planet;

import android.app.Activity;
import android.os.Bundle;

import com.silentgames.silent_planet.logic.GameLogic;
import com.silentgames.silent_planet.view.GameView;
import com.silentgames.silent_planet.view.MySurfaceView;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new MySurfaceView(this));
        setContentView(R.layout.activity_main);
        GameView view=(GameView)findViewById(R.id.game_view);
        GameLogic logic=new GameLogic(view, this);
        view.setGameLogic(logic);
    }
}
