package com.silentgames.silent_planet.mvp.main

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import com.silentgames.silent_planet.R
import com.silentgames.silent_planet.dialog.BottomSheetMenu
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.model.Cell
import com.silentgames.silent_planet.model.cells.CellType
import com.silentgames.silent_planet.model.entities.EntityType
import com.silentgames.silent_planet.model.fractions.FractionsType
import com.silentgames.silent_planet.utils.Calculator
import com.silentgames.silent_planet.view.GameView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), MainContract.View, GameView.Callback {

    lateinit var presenter: MainContract.Presenter

    private lateinit var mBitmap: Bitmap
    private lateinit var mCanvas: Canvas
    private lateinit var paint: Paint
    private lateinit var mBitmapPaint: Paint

    private var mScaleFactor: Float = 0.toFloat()
    private var canvasSize: Float = 0.toFloat()
    private var viewSize: Int = 0

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenter(this, MainViewModel(), MainModel(this))

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

        mScaleFactor = Constants.getmScaleFactor()
        canvasSize = Constants.getCanvasSize(this)
        viewSize = Constants.getViewSize(this)
        mBitmap = Bitmap.createBitmap(canvasSize.toInt(), canvasSize.toInt(), Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        mBitmapPaint = Paint(Paint.DITHER_FLAG)

        action_button.setOnClickListener { presenter.onActionButtonClick() }

    }

    override fun fillEntityName(text: String) {
        tv_entity_name.text = text
    }

    override fun fillDescription(text: String) {
        tv_description.text = text
    }


    override fun selectCurrentFraction(fractionType: FractionsType) {
        val white = ContextCompat.getColor(this, R.color.white)
        val red = ContextCompat.getColor(this, R.color.red)
        tv_humans.setTextColor(white)
        tv_aliens.setTextColor(white)
        tv_robots.setTextColor(white)
        tv_pirates.setTextColor(white)
        when (fractionType) {
            FractionsType.HUMAN -> tv_humans.setTextColor(red)
            FractionsType.ALIEN -> tv_aliens.setTextColor(red)
            FractionsType.ROBOT -> tv_robots.setTextColor(red)
            FractionsType.PIRATE -> tv_pirates.setTextColor(red)
        }
    }

    override fun changeAlienCristalCount(crystals: Int) {
        tv_aliens.text = getString(
                R.string.crystal_count,
                getString(R.string.aliens),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun changeHumanCristalCount(crystals: Int) {
        tv_humans.text = getString(
                R.string.crystal_count,
                getString(R.string.humans),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun changePirateCristalCount(crystals: Int) {
        tv_pirates.text = getString(
                R.string.crystal_count,
                getString(R.string.pirates),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun changeRobotCristalCount(crystals: Int) {
        tv_robots.text = getString(
                R.string.crystal_count,
                getString(R.string.robots),
                crystals,
                Constants.countCrystalsToWin
        )
    }

    override fun enableButton(isEnabled: Boolean) {
        action_button.isEnabled = isEnabled
    }

    override fun setImageCrystalText(text: String) {
        image_crystal_text.text = text
    }

    override fun drawGrid() {
        //рисуем сетку
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells + 1)
            mCanvas.drawLine(x.toFloat() * Constants.getCanvasSize(this) / horizontalCountOfCells, 0f, x.toFloat() * Constants.getCanvasSize(this) / horizontalCountOfCells, Constants.getCanvasSize(this), paint)
        for (y in 0 until verticalCountOfCells + 1)
            mCanvas.drawLine(0f, y.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells, Constants.getCanvasSize(this), y.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells, paint)
        game_view.invalidate()
    }

    override fun drawBattleGround(gameMatrix: Array<Array<Cell>>) {
        val horizontalCountOfCells = Constants.horizontalCountOfCells
        val verticalCountOfCells = Constants.verticalCountOfCells
        for (x in 0 until horizontalCountOfCells) {
            for (y in 0 until verticalCountOfCells) {
                mCanvas.drawBitmap(
                        gameMatrix[x][y].cellType.getCurrentBitmap(),
                        x.toFloat() * Constants.getCanvasSize(this) / horizontalCountOfCells,
                        y.toFloat() * Constants.getCanvasSize(this) / verticalCountOfCells,
                        paint
                )
                if (gameMatrix[x][y].entityType.isNotEmpty())
                    reDraw(x, y, gameMatrix[x][y].entityType.first().bitmap)
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
        select_object_icon.setImageBitmap(cellType.getCurrentBitmap())

    }

    override fun showObjectIcon(entityType: EntityType) {
        select_object_icon.setImageBitmap(entityType.bitmap)
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
        if (game_view.scrollX + distanceX < canvasSize - viewSize && game_view.scrollX + distanceX > 0) {
            game_view.scrollBy(distanceX.toInt(), 0)
        }
        //не даем канвасу показать края по вертикали
        if (game_view.scrollY + distanceY < canvasSize - viewSize && game_view.scrollY + distanceY > 0) {
            game_view.scrollBy(0, distanceY.toInt())
        }
    }

    override fun onSingleTapConfirmed(event: MotionEvent) {
        val eventX = (event.x + game_view.scrollX) / mScaleFactor
        val eventY = (event.y + game_view.scrollY) / mScaleFactor
        val x = (Constants.horizontalCountOfCells * eventX / viewSize).toInt()
        val y = (Constants.verticalCountOfCells * eventY / viewSize).toInt()

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
            var scrollX = ((game_view.scrollX + focusX) * scaleFactor - focusX).toInt()
            scrollX = Math.min(Math.max(scrollX, 0), canvasSize.toInt() - viewSize)
            var scrollY = ((game_view.scrollY + focusY) * scaleFactor - focusY).toInt()
            scrollY = Math.min(Math.max(scrollY, 0), canvasSize.toInt() - viewSize)
            game_view.scrollTo(scrollX, scrollY)
        }
        //вызываем перерисовку принудительно
        game_view.invalidate()
    }

    override fun showEntityMenuDialog(
            entityList: MutableList<EntityType>,
            currentCell: CellType
    ) {
        BottomSheetMenu(this, entityList, currentCell) { entityType ->
            presenter.onEntityDialogElementSelect(entityType)
        }.show()
    }
}
