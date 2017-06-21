package com.silentgames.silent_planet.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.silentgames.silent_planet.customListeners.CustomGestureListener;
import com.silentgames.silent_planet.customListeners.CustomScaleGestureListener;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class GameView extends View implements CustomGestureListener.Callback, CustomScaleGestureListener.Callback {

    private Callback callback;

    private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;

    public interface Callback{

        void onDraw(Canvas canvas);

        void onScroll(float distanceX, float distanceY);

        void onSingleTapConfirmed(MotionEvent event);

        void onScale(ScaleGestureDetector scaleGestureDetector);

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.callback = (Callback) context;

        scaleGestureDetector = new ScaleGestureDetector(context, new CustomScaleGestureListener(this));
        detector = new GestureDetector(context, new CustomGestureListener(this));

    }

    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        callback.onDraw(canvas);
    }

    @Override
    public void onScroll(float distanceX, float distanceY) {
        callback.onScroll(distanceX, distanceY);
    }

    @Override
    public void onSingleTapConfirmed(MotionEvent event) {
        callback.onSingleTapConfirmed(event);
    }

    @Override
    public void onScale() {
        callback.onScale(scaleGestureDetector);
    }
}
