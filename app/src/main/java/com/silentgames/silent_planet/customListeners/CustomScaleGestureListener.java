package com.silentgames.silent_planet.customListeners;

import android.view.ActionMode;
import android.view.ScaleGestureDetector;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class CustomScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

    private Callback callback;

    public interface Callback {

        void onScale();

    }

    public CustomScaleGestureListener(Callback callback) {
        this.callback = callback;
    }

    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

        callback.onScale();

        return true;
    }
}
