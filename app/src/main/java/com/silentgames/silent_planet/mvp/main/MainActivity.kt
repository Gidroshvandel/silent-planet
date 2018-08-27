package com.silentgames.silent_planet.mvp.main

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.*
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.utils.Calculator
import com.silentgames.silent_planet.view.GameView

class MainActivity : Activity(), MainContract.View, GameView.Callback {

    lateinit var presenter: MainContract.Presenter

    private lateinit var gameView: GameView
    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private lateinit var paint: Paint
    private lateinit var mBitmapPaint: Paint
    private lateinit var selectObjectIcon: ImageView
    private lateinit var objectListOnCell: Spinner
    private lateinit var adapter: ArrayAdapter<String>

    private var mScaleFactor: Float = 0.toFloat()
    private var canvasSize: Float = 0.toFloat()
    private var viewSize: Int = 0

    private var actionButton: Button? = null
    private var imageCrystalText: TextView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, MainViewModel(), MainModel())

        initUi()
        paintSettings()

        presenter.onCreate()

    }

    private fun paintSettings() {
        //определяем параметры кисти
        paint = Paint()
        paint.isAntiAlias = true
        paint.isDither = true
        paint.color = -0x1
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeCap = Paint.Cap.ROUND
    }

    private fun initUi() {

        gameView = findViewById<View>(R.id.game_view) as GameView

        selectObjectIcon = findViewById<View>(R.id.imageView) as ImageView

        objectListOnCell = findViewById<View>(R.id.spinner) as Spinner

        mScaleFactor = Constants.getmScaleFactor()
        canvasSize = Constants.getCanvasSize(this)
        viewSize = Constants.getViewSize(this)
        mBitmap = Bitmap.createBitmap(canvasSize.toInt(), canvasSize.toInt(), Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        mBitmapPaint = Paint(Paint.DITHER_FLAG)

        imageCrystalText = findViewById<View>(R.id.imageCrystalText) as TextView

        actionButton = findViewById<View>(R.id.actionButton) as Button

        actionButton!!.setOnClickListener { presenter.onActionButtonClick() }
    }

    override fun enableButton(isEnabled: Boolean) {
        actionButton!!.isEnabled = isEnabled
        //        if (isEnabled){
        ////            actionButton.setVisibility(View.VISIBLE);
        //        }else {
        ////            actionButton.setVisibility(View.INVISIBLE);
        //        }
    }

    override fun setImageCrystalText(text: String) {
        imageCrystalText!!.text = text
    }

    override fun drawGrid() {
        //рисуем сетку
        val horizontalCountOfCells = Constants.getHorizontalCountOfCells()
        val verticalCountOfCells = Constants.getVerticalCountOfCells()
        for (x in 0 until horizontalCountOfCells + 1)
            mCanvas.drawLine(x.toFloat() * Constants.getCanvasSize(this) / horizontalCountOfCells, 0f, x.toFloat() * Constants.getCanvasSize(this) / horizontalCountOfCells, Constants.getCanvasSize(this), paint)
        for (y in 0 until verticalCountOfCells + 1)
            mCanvas.drawLine(0f, y.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells, Constants.getCanvasSize(this), y.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells, paint)
        gameView.invalidate()
    }

    override fun drawBattleGround(gameMatrix: Array<Array<Cell>>) {

        val horizontalCountOfCells = Constants.getHorizontalCountOfCells()
        val verticalCountOfCells = Constants.getVerticalCountOfCells()
        for (x in 0 until horizontalCountOfCells + 1) {
            for (y in 0 until verticalCountOfCells + 1) {
                mCanvas.drawBitmap(gameMatrix[x][y].cellType.bitmap, x.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells, y.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells, paint)
                if (gameMatrix[x][y].entityType != null)
                    reDraw(x, y, gameMatrix[x][y].entityType!!.bitmap!!)
            }
        }
        drawGrid()
    }

    override fun reDraw(eventX: Int, eventY: Int, entity: Bitmap) {
        val x = Calculator.CellCenterNumeratorSquare(eventX.toFloat(), viewSize, entity)
        val y = Calculator.CellCenterNumeratorSquare(eventY.toFloat(), viewSize, entity)
        mCanvas.drawBitmap(entity, x, y, null)
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showObjectIcon(cellType: CellType) {
        selectObjectIcon.setImageBitmap(cellType.bitmap)

    }

    override fun showObjectIcon(entityType: EntityType) {
        selectObjectIcon.setImageBitmap(entityType.bitmap)
    }

    override fun showCellListItem(x: Int, y: Int, playerList: List<String>) {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, playerList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        objectListOnCell.visibility = View.VISIBLE
        objectListOnCell.adapter = adapter
        objectListOnCell.prompt = "Title"
        objectListOnCell.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View,
                                        position: Int, id: Long) {
                presenter.onCellListItemSelectedClick(x, y, adapter.getItem(position))
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {}
        }
    }

    override fun hideCellListItem() {
        objectListOnCell.visibility = View.INVISIBLE
    }

    override fun update(runnable: Runnable) {
        this.runOnUiThread(runnable)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        canvas.scale(mScaleFactor, mScaleFactor)//зумируем канвас
        canvas.drawBitmap(mBitmap, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    override fun onScroll(distanceX: Float, distanceY: Float) {
        //не даем канвасу показать края по горизонтали
        if (gameView.scrollX + distanceX < canvasSize - viewSize && gameView.scrollX + distanceX > 0) {
            gameView.scrollBy(distanceX.toInt(), 0)
        }
        //не даем канвасу показать края по вертикали
        if (gameView.scrollY + distanceY < canvasSize - viewSize && gameView.scrollY + distanceY > 0) {
            gameView.scrollBy(0, distanceY.toInt())
        }
    }

    override fun onSingleTapConfirmed(event: MotionEvent) {
        val eventX = (event.x + gameView.scrollX) / mScaleFactor
        val eventY = (event.y + gameView.scrollY) / mScaleFactor
        val x = (Constants.getHorizontalCountOfCells() * eventX / viewSize).toInt()
        val y = (Constants.getVerticalCountOfCells() * eventY / viewSize).toInt()

        presenter.onSingleTapConfirmed(x, y)
    }

    override fun onScale(scaleGestureDetector: ScaleGestureDetector) {
        val scaleFactor = scaleGestureDetector.scaleFactor//получаем значение зума относительно предыдущего состояния
        //получаем координаты фокальной точки - точки между пальцами
        val focusX = scaleGestureDetector.focusX
        val focusY = scaleGestureDetector.focusY
        //следим чтобы канвас не уменьшили меньше исходного размера и не допускаем увеличения больше чем в 2 раза
        if (mScaleFactor * scaleFactor > 1 && mScaleFactor * scaleFactor < 2) {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            canvasSize = viewSize * mScaleFactor//изменяем хранимое в памяти значение размера канваса
            //используется при расчетах
            //по умолчанию после зума канвас отскролит в левый верхний угол. Скролим канвас так, чтобы на экране оставалась обасть канваса, над которой был
            //жест зума
            //Для получения данной формулы достаточно школьных знаний математики (декартовы координаты).
            var scrollX = ((gameView.scrollX + focusX) * scaleFactor - focusX).toInt()
            scrollX = Math.min(Math.max(scrollX, 0), canvasSize.toInt() - viewSize)
            var scrollY = ((gameView.scrollY + focusY) * scaleFactor - focusY).toInt()
            scrollY = Math.min(Math.max(scrollY, 0), canvasSize.toInt() - viewSize)
            gameView.scrollTo(scrollX, scrollY)
        }
        //вызываем перерисовку принудительно
        gameView.invalidate()
    }
}
