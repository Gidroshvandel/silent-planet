package com.silentgames.silent_planet.customListeners;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.silentgames.silent_planet.logic.Constants;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {

    private Callback callback;

    public interface Callback {

        void onScroll(float distanceX, float distanceY);

        void onSingleTapConfirmed(MotionEvent event);

    }

    public CustomGestureListener(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        callback.onScroll(distanceX, distanceY);
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        callback.onSingleTapConfirmed(event);
        return true;
    }
}
