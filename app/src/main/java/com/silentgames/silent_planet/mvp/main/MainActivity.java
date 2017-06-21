package com.silentgames.silent_planet.mvp.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.utils.Calculator;
import com.silentgames.silent_planet.view.GameView;

public class MainActivity extends Activity implements MainContract.View, GameView.Callback {

    MainContract.Presenter presenter;

    private GameView game_view;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint paint, mBitmapPaint;
    private  float mScaleFactor;
    private  float canvasSize;
    private  int viewSize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        game_view=(GameView)findViewById(R.id.game_view);

        presenter = new MainPresenter(this, new MainViewModel());

        initUi();
        paintSettings();

        presenter.onCreate();

    }

    private void paintSettings(){
        //определяем параметры кисти
        paint =new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xffffffff);
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    private void initUi(){

        mScaleFactor = Constants.getmScaleFactor();
        canvasSize = Constants.getCanvasSize(this);
        viewSize = Constants.getViewSize(this);
        mBitmap = Bitmap.createBitmap((int) canvasSize, (int) canvasSize, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public void drawGrid() {
        //рисуем сетку
        int horizontalCountOfCells = Constants.getHorizontalCountOfCells();
        int verticalCountOfCells = Constants.getVerticalCountOfCells();
        for(int x=0;x< horizontalCountOfCells +1;x++)
            mCanvas.drawLine((float)x* Constants.getCanvasSize(this) / horizontalCountOfCells, 0, (float)x* Constants.getCanvasSize(this) / horizontalCountOfCells, Constants.getCanvasSize(this), paint);
        for(int y=0;y< verticalCountOfCells +1;y++)
            mCanvas.drawLine(0, (float)y* Constants.getCanvasSize(this) / verticalCountOfCells, Constants.getCanvasSize(this), (float)y* Constants.getCanvasSize(this) / verticalCountOfCells, paint);
        game_view.invalidate();
    }

    @Override
    public void drawBattleGround(Cell[][] gameMatrix) {

        int horizontalCountOfCells = Constants.getHorizontalCountOfCells();
        int verticalCountOfCells = Constants.getVerticalCountOfCells();
        for (int x = 0; x < horizontalCountOfCells + 1; x++) {
            for (int y = 0; y < verticalCountOfCells + 1; y++) {
                mCanvas.drawBitmap(gameMatrix[x][y].getCellType().getBitmap(),(float)x* Constants.getCanvasSize(this) / verticalCountOfCells,(float)y* Constants.getCanvasSize(this) / verticalCountOfCells,paint);
                if(gameMatrix[x][y].getEntityType() != null)
                    reDraw(x,y,gameMatrix[x][y].getEntityType().getBitmap());
            }
        }
        drawGrid();
    }

    @Override
    public void reDraw(int eventX, int eventY, Bitmap entity) {
        float x = Calculator.CellCenterNumeratorSquare(eventX, viewSize, entity);
        float y = Calculator.CellCenterNumeratorSquare(eventY, viewSize, entity);
        mCanvas.drawBitmap(entity, x, y, null);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);//зумируем канвас
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    @Override
    public void onScroll(float distanceX, float distanceY) {
        //не даем канвасу показать края по горизонтали
        if(game_view.getScrollX()+distanceX< canvasSize -viewSize && game_view.getScrollX()+distanceX>0){
            game_view.scrollBy((int)distanceX, 0);
        }
        //не даем канвасу показать края по вертикали
        if(game_view.getScrollY()+distanceY< canvasSize -viewSize && game_view.getScrollY()+distanceY>0){
            game_view.scrollBy(0, (int)distanceY);
        }
    }

    @Override
    public void onSingleTapConfirmed(MotionEvent event) {
        float eventX=(event.getX()+game_view.getScrollX())/mScaleFactor;
        float eventY=(event.getY()+game_view.getScrollY())/mScaleFactor;
        int x = (int)(Constants.getHorizontalCountOfCells() *eventX/viewSize);
        int y = (int)(Constants.getVerticalCountOfCells() *eventY/viewSize);

        presenter.onSingleTapConfirmed(x, y);
    }

    @Override
    public void onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor=scaleGestureDetector.getScaleFactor();//получаем значение зума относительно предыдущего состояния
            //получаем координаты фокальной точки - точки между пальцами
            float focusX=scaleGestureDetector.getFocusX();
            float focusY=scaleGestureDetector.getFocusY();
            //следим чтобы канвас не уменьшили меньше исходного размера и не допускаем увеличения больше чем в 2 раза
            if(mScaleFactor*scaleFactor>1 && mScaleFactor*scaleFactor<2){
                mScaleFactor *= scaleGestureDetector.getScaleFactor();
                canvasSize =viewSize*mScaleFactor;//изменяем хранимое в памяти значение размера канваса
                //используется при расчетах
                //по умолчанию после зума канвас отскролит в левый верхний угол. Скролим канвас так, чтобы на экране оставалась обасть канваса, над которой был
                //жест зума
                //Для получения данной формулы достаточно школьных знаний математики (декартовы координаты).
                int scrollX=(int)((game_view.getScrollX()+focusX)*scaleFactor-focusX);
                scrollX=Math.min( Math.max(scrollX, 0), (int) canvasSize -viewSize);
                int scrollY=(int)((game_view.getScrollY()+focusY)*scaleFactor-focusY);
                scrollY=Math.min( Math.max(scrollY, 0), (int) canvasSize -viewSize);
                game_view.scrollTo(scrollX, scrollY);
            }
            //вызываем перерисовку принудительно
            game_view.invalidate();
    }
}
