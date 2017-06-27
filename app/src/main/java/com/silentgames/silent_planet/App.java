package com.silentgames.silent_planet;

import android.app.Application;
import android.content.Context;

/**
 * Created by gidroshvandel on 27.06.17.
 */
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }


}
