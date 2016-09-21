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

import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.logic.GameLogic;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.utils.Calculator;

/**
 * Created by gidroshvandel on 05.07.16.
 */
public class GameView extends View{

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint paint, mBitmapPaint;
    private final GestureDetector detector;
    private  GameLogic gameLogic;
    private  float mScaleFactor;
    private  float canvasSize;
    private  int viewSize;


    private ScaleGestureDetector scaleGestureDetector;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Constants constant = new Constants(context);
        mScaleFactor = Constants.getmScaleFactor();
        canvasSize = constant.getCanvasSize();
        viewSize = constant.getViewSize();
        mBitmap = Bitmap.createBitmap((int) canvasSize, (int) canvasSize, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        paintSettings();

        scaleGestureDetector=new ScaleGestureDetector(context, new MyScaleGestureListener());

        detector=new GestureDetector(context, new MyGestureListener());

    }

    public void paintSettings(){
        //определяем параметры кисти, которой будем рисовать сетку и атомы
        paint =new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xffffffff);
        paint.setStrokeWidth(2f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void drawGrid(){

        Constants constant = new Constants(this.getContext());
        //рисуем сетку
        int horizontalCountOfCells = Constants.getHorizontalCountOfCells();
        int verticalCountOfCells = Constants.getVerticalCountOfCells();
        for(int x=0;x< horizontalCountOfCells +1;x++)
            mCanvas.drawLine((float)x* constant.getCanvasSize() / horizontalCountOfCells, 0, (float)x* constant.getCanvasSize() / horizontalCountOfCells, constant.getCanvasSize(), paint);
        for(int y=0;y< verticalCountOfCells +1;y++)
            mCanvas.drawLine(0, (float)y* constant.getCanvasSize() / verticalCountOfCells, constant.getCanvasSize(), (float)y* constant.getCanvasSize() / verticalCountOfCells, paint);
        invalidate();
    }

    public void drawBattleGround(Cell[][] gameMatrix) {
        Constants constant = new Constants(this.getContext());

        int horizontalCountOfCells = Constants.getHorizontalCountOfCells();
        int verticalCountOfCells = Constants.getVerticalCountOfCells();
        for (int x = 0; x < horizontalCountOfCells + 1; x++) {
            for (int y = 0; y < verticalCountOfCells + 1; y++) {
                mCanvas.drawBitmap(gameMatrix[x][y].getCellType().getBitmap(),(float)x* constant.getCanvasSize() / verticalCountOfCells,(float)y* constant.getCanvasSize() / verticalCountOfCells,paint);
                if(gameMatrix[x][y].getEntityType() != null)
                reDraw(x,y,gameMatrix[x][y].getEntityType().getBitmap());
            }
        }
        drawGrid();
    }


    public void reDraw(int eventX,int eventY, Bitmap entity){
        float x = Calculator.CellCenterNumeratorSquare(eventX, viewSize, entity);
        float y = Calculator.CellCenterNumeratorSquare(eventY, viewSize, entity);
        mCanvas.drawBitmap(entity, x, y, null);
    }

    //в случае касания пальем передаем обработку Motion Event'а MyGestureListener'у и MyScaleGestureListener'у

    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);//зумируем канвас
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();
    }

    //унаследовались от ScaleGestureDetector.SimpleOnScaleGestureListener, чтобы не писать пустую реализацию ненужных методов интерфейса OnScaleGestureListener

    public class MyScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        public MyScaleGestureListener() {
        }

        //обрабатываем "щипок" пальцами
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
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
                int scrollX=(int)((getScrollX()+focusX)*scaleFactor-focusX);
                scrollX=Math.min( Math.max(scrollX, 0), (int) canvasSize -viewSize);
                int scrollY=(int)((getScrollY()+focusY)*scaleFactor-focusY);
                scrollY=Math.min( Math.max(scrollY, 0), (int) canvasSize -viewSize);
                scrollTo(scrollX, scrollY);
            }
            //вызываем перерисовку принудительно
            invalidate();
            return true;
        }
    }

    //унаследовались от GestureDetector.SimpleOnGestureListener, чтобы не писать пустую
//реализацию ненужных методов интерфейса OnGestureListener
    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener
    {

        public MyGestureListener() {
        }

        //обрабатываем скролл (перемещение пальца по экрану)
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY)
        {
            //не даем канвасу показать края по горизонтали
            if(getScrollX()+distanceX< canvasSize -viewSize && getScrollX()+distanceX>0){
                scrollBy((int)distanceX, 0);
            }
            //не даем канвасу показать края по вертикали
            if(getScrollY()+distanceY< canvasSize -viewSize && getScrollY()+distanceY>0){
                scrollBy(0, (int)distanceY);
            }
            return true;
        }

        //обрабатываем одиночный тап
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event){
//        if(isLock)return true;
            //получаем координаты ячейки, по которой тапнули
            float eventX=(event.getX()+getScrollX())/mScaleFactor;
            float eventY=(event.getY()+getScrollY())/mScaleFactor;
            int x = (int)(Constants.getHorizontalCountOfCells() *eventX/viewSize);
            int y = (int)(Constants.getVerticalCountOfCells() *eventY/viewSize);
            Constants.oldXY = gameLogic.select(x,y, Constants.oldXY, null);
            return true;
        }

//        //обрабатываем двойной тап
//        @Override
//        public boolean onDoubleTapEvent(MotionEvent event){
//            //зумируем канвас к первоначальному виду
//            mScaleFactor=1f;
//            canvasSize =viewSize;
//            scrollTo(0, 0);//скролим, чтобы не было видно краев канваса.
//            invalidate();//перерисовываем канвас
//            return true;
//        }
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }
}
