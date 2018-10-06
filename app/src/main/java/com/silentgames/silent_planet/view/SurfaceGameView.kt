package com.silentgames.silent_planet.view

import android.content.Context
import android.util.AttributeSet
import android.view.*
import com.silentgames.silent_planet.customListeners.CustomGestureListener
import com.silentgames.silent_planet.customListeners.CustomScaleGestureListener
import com.silentgames.silent_planet.engine.base.DrawerTask
import com.silentgames.silent_planet.engine.base.Scene
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.model.Axis
import java.util.*

class SurfaceGameView(
        context: Context,
        attrs: AttributeSet?
) : SurfaceView(context, attrs),
        SurfaceHolder.Callback,
        CustomGestureListener.Callback,
        CustomScaleGestureListener.Callback {

    private var mScaleFactor = 1f

    private val detector: GestureDetector
    private val scaleGestureDetector: ScaleGestureDetector

    private var drawer: DrawerTask? = null

    private var callback: Callback

    private var scene: Scene? = null

    private var timer: Timer = Timer()

    init {
        holder.addCallback(this)
        callback = context as Callback
        scaleGestureDetector = ScaleGestureDetector(
                context,
                CustomScaleGestureListener(this)
        )
        detector = GestureDetector(context, CustomGestureListener(this))
    }

    fun setScene(scene: Scene) {
        this.scene = scene
        drawer = DrawerTask(holder, scene)
    }

    override fun onSingleTapConfirmed(event: MotionEvent?) {
        event?.let {
            val eventX = (event.x + scrollX) / mScaleFactor
            val eventY = (event.y + scrollY) / mScaleFactor
            val x = (Constants.horizontalCountOfCells * eventX / (scene?.width ?: 1)).toInt()
            val y = (Constants.verticalCountOfCells * eventY / (scene?.height ?: 1)).toInt()
            callback.onSingleTapConfirmed(Axis(x, y))
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        detector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        timer.scheduleAtFixedRate(drawer, 0, 40)
        val canvas = holder.lockCanvas()
        scene?.setWH(canvas.width, canvas.height)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        //Прерываем поток при уничтожении surface
        drawer?.cancel()
        timer.cancel()
    }

    override fun onScroll(distanceX: Float, distanceY: Float) {
        scene?.let {
            //не даем канвасу показать края по горизонтали
            if (scrollX + distanceX < it.width && scrollX + distanceX > 0) {
                scrollBy(distanceX.toInt(), 0)
                scene?.scrollAxis = Axis((scrollX + distanceX).toInt(), scrollY)
            }
            //не даем канвасу показать края по вертикали
            if (scrollY + distanceY < it.height && scrollY + distanceY > 0) {
                scrollBy(0, distanceY.toInt())
                scene?.scrollAxis = Axis(scrollX, (scrollY + distanceY).toInt())
            }
        }
    }

    override fun onScale() {
        val scaleFactor = scaleGestureDetector.scaleFactor//получаем значение зума относительно предыдущего состояния
        //получаем координаты фокальной точки - точки между пальцами
        val focusX = scaleGestureDetector.focusX
        val focusY = scaleGestureDetector.focusY
        scene?.let {
            //следим чтобы канвас не уменьшили меньше исходного размера и не допускаем увеличения больше чем в 2 раза
            if (mScaleFactor * scaleFactor > 1 && mScaleFactor * scaleFactor < 2) {
                mScaleFactor *= scaleGestureDetector.scaleFactor
                scene?.mScaleFactor = mScaleFactor
                //используется при расчетах
                //по умолчанию после зума канвас отскролит в левый верхний угол. Скролим канвас так, чтобы на экране оставалась обасть канваса, над которой был
                //жест зума
                //Для получения данной формулы достаточно школьных знаний математики (декартовы координаты).
                var scrollX = ((scrollX + focusX) * scaleFactor - focusX).toInt()
                scrollX = Math.min(
                        Math.max(scrollX, 0),
                        it.width)
                var scrollY = ((scrollY + focusY) * scaleFactor - focusY).toInt()
                scrollY = Math.min(
                        Math.max(scrollY, 0),
                        it.height
                )
                scrollTo(scrollX, scrollY)
            }
        }
    }
}

interface Callback {

    fun onSingleTapConfirmed(axis: Axis)

}