package com.silentgames.silent_planet.mvp.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.silentgames.silent_planet.R;
import com.silentgames.silent_planet.logic.Constants;
import com.silentgames.silent_planet.model.Cell;
import com.silentgames.silent_planet.utils.Calculator;
import com.silentgames.silent_planet.view.GameView;

import java.util.List;

public class MainActivity extends Activity implements MainContract.View, GameView.Callback {

    MainContract.Presenter presenter;

    private GameView game_view;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint paint, mBitmapPaint;
    private ImageView selectObjectIcon;
    private Spinner objectListOnCell;
    ArrayAdapter<String> adapter;

    private  float mScaleFactor;
    private  float canvasSize;
    private  int viewSize;

    private Button actionButton;
    private TextView imageCrystalText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this, new MainViewModel(), new MainModel());

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

        game_view=(GameView)findViewById(R.id.game_view);

        selectObjectIcon = (ImageView) findViewById(R.id.imageView);

        objectListOnCell = (Spinner) findViewById(R.id.spinner);

        mScaleFactor = Constants.getmScaleFactor();
        canvasSize = Constants.getCanvasSize(this);
        viewSize = Constants.getViewSize(this);
        mBitmap = Bitmap.createBitmap((int) canvasSize, (int) canvasSize, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        imageCrystalText =(TextView) findViewById(R.id.imageCrystalText);

        actionButton = (Button) findViewById(R.id.actionButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onActionButtonClick();
            }
        });
    }

    @Override
    public void enableButton(boolean isEnabled) {
        actionButton.setEnabled(isEnabled);
//        if (isEnabled){
////            actionButton.setVisibility(View.VISIBLE);
//        }else {
////            actionButton.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public void setImageCrystalText(String text) {
        imageCrystalText.setText(text);
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
    public void showToast(String text) {
        Toast.makeText(this, text , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showObjectIcon(Cell gameCell) {
        if (gameCell.getEntityType() != null ) {
            selectObjectIcon.setImageBitmap(gameCell.getEntityType().getBitmap());
        }else {
            selectObjectIcon.setImageBitmap(gameCell.getCellType().getBitmap());
        }
    }

    @Override
    public void showCellListItem(final int x, final int y, List<String> playerList) {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, playerList );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        objectListOnCell.setVisibility(View.VISIBLE);
        objectListOnCell.setAdapter(adapter);
        objectListOnCell.setPrompt("Title");
        objectListOnCell.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                presenter.onCellListItemSelectedClick(x, y, adapter.getItem(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    @Override
    public void hideCellListItem() {
        objectListOnCell.setVisibility(View.INVISIBLE);
    }

    @Override
    public void update(Runnable runnable) {
        this.runOnUiThread(runnable);
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
