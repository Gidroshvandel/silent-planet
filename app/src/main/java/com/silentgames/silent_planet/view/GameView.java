package com.silentgames.silent_planet.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.silentgames.silent_planet.customListeners.CustomGestureListener;
import com.silentgames.silent_planet.customListeners.CustomScaleGestureListener;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.Axis;

/**
 * Created by gidroshvandel on 21.06.17.
 */
public class GameView extends View implements CustomGestureListener.Callback, CustomScaleGestureListener.Callback {

    private Callback callback;

    private GestureDetector detector;
    private ScaleGestureDetector scaleGestureDetector;

    private Float mScaleFactor;
    private Float canvasSize;
    private int viewSize;

    private Paint mBitmapPaint;
    private Bitmap mBitmap;

    public interface Callback {

        void onSingleTapConfirmed(Axis axis);

    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.callback = (Callback) context;

        scaleGestureDetector = new ScaleGestureDetector(context, new CustomScaleGestureListener(this));
        detector = new GestureDetector(context, new CustomGestureListener(this));

        mScaleFactor = Constants.mScaleFactor;
        canvasSize = Constants.getCanvasSize(context);
        viewSize = Constants.getViewSize(context);

        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);//зумируем канвас
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint);
        canvas.restore();
    }

    @Override
    public void onScroll(float distanceX, float distanceY) {
        //не даем канвасу показать края по горизонтали
        if (getScrollX() + distanceX < canvasSize - viewSize
                && getScrollX() + distanceX > 0) {
            scrollBy((int) distanceX, 0);
        }
        //не даем канвасу показать края по вертикали
        if (getScrollY() + distanceY < canvasSize - viewSize
                && getScrollY() + distanceY > 0) {
            scrollBy(0, (int) distanceY);
        }
    }

    @Override
    public void onSingleTapConfirmed(MotionEvent event) {
        float eventX = (event.getX() + getScrollX()) / mScaleFactor;
        float eventY = (event.getY() + getScrollY()) / mScaleFactor;
        int x = (int) (Constants.horizontalCountOfCells * eventX / viewSize);
        int y = (int) (Constants.horizontalCountOfCells * eventY / viewSize);
        callback.onSingleTapConfirmed(new Axis(x, y));
    }

    @Override
    public void onScale() {
        float scaleFactor = scaleGestureDetector.getScaleFactor();//получаем значение зума относительно предыдущего состояния
        //получаем координаты фокальной точки - точки между пальцами
        float focusX = scaleGestureDetector.getFocusX();
        float focusY = scaleGestureDetector.getFocusY();
        //следим чтобы канвас не уменьшили меньше исходного размера и не допускаем увеличения больше чем в 2 раза
        if (mScaleFactor * scaleFactor > 1 && mScaleFactor * scaleFactor < 2) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            canvasSize = viewSize * mScaleFactor;//изменяем хранимое в памяти значение размера канваса
            //используется при расчетах
            //по умолчанию после зума канвас отскролит в левый верхний угол. Скролим канвас так, чтобы на экране оставалась обасть канваса, над которой был
            //жест зума
            //Для получения данной формулы достаточно школьных знаний математики (декартовы координаты).
            int scrollX = (int) ((getScrollX() + focusX) * scaleFactor - focusX);
            scrollX = (int) Math.min(Math.max(scrollX, 0), canvasSize - viewSize);
            int scrollY = (int) ((getScrollY() + focusY) * scaleFactor - focusY);
            scrollY = (int) Math.min(Math.max(scrollY, 0), canvasSize - viewSize);
            scrollTo(scrollX, scrollY);
        }
        //вызываем перерисовку принудительно
        invalidate();
    }
}
