package com.silentgames.silent_planet.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.*
import com.silentgames.silent_planet.customListeners.CustomGestureListener
import com.silentgames.silent_planet.customListeners.CustomScaleGestureListener
import com.silentgames.silent_planet.engine.GridLayer
import com.silentgames.silent_planet.engine.LayerEntityMover
import com.silentgames.silent_planet.engine.base.DrawerTask
import com.silentgames.silent_planet.engine.base.Layer
import com.silentgames.silent_planet.engine.base.Scene
import com.silentgames.silent_planet.logic.Constants
import com.silentgames.silent_planet.model.Axis
import java.util.*
import kotlin.math.max
import kotlin.math.min

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

    private var timer: Timer? = null

    private var toDrawLayerList = mutableListOf<Pair<LayerType, Layer>>()

    private var onSceneChanged: (() -> Unit)? = null

    init {
        holder.addCallback(this)
        callback = context as Callback
        scaleGestureDetector = ScaleGestureDetector(
                context,
                CustomScaleGestureListener(this)
        )
        detector = GestureDetector(context, CustomGestureListener(this))
    }

    fun updateLayer(layerType: LayerType, layer: Layer) {
        val scene = this.scene
        if (scene != null) {
            LayerEntityMover(layerType.id, layer).attach(scene)
        } else {
            toDrawLayerList.add(Pair(layerType, layer))
        }
    }

    fun updateLayer(layerType: LayerType, layer: Layer, onUpdateComplete: () -> Unit) {
        val scene = this.scene
        if (scene != null) {
            LayerEntityMover(layerType.id, layer).attach(scene)
            drawer?.onSceneChanged = onUpdateComplete
        } else {
            onSceneChanged = onUpdateComplete
            toDrawLayerList.add(Pair(layerType, layer))
        }
    }

    override fun onSingleTapConfirmed(event: MotionEvent?) {
        event?.let {
            scene?.let {
                val eventX = (event.x + it.scrollAxis.x) / mScaleFactor
                val eventY = (event.y + it.scrollAxis.y) / mScaleFactor
                val x = ((Constants.horizontalCountOfCells) * eventX / (it.width)).toInt()
                val y = ((Constants.verticalCountOfCells) * eventY / (it.height)).toInt()
                callback.onSingleTapConfirmed(Axis(x, y))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        detector.onTouchEvent(event)
        scaleGestureDetector.onTouchEvent(event)
        return true
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        if (scene == null) {
            val canvas = holder.lockCanvas()
            scene = Scene(mutableListOf(Layer(), GridLayer(), Layer()), canvas.width, canvas.height).apply {
                toDrawLayerList.forEach {
                    setLayer(it.first.id, it.second)
                }
            }
            holder.unlockCanvasAndPost(canvas)
        }
        scene?.let {
            drawer = DrawerTask(holder, it).also {
                it.onSceneChanged = this.onSceneChanged
            }
        }
        timer = Timer().apply {
            scheduleAtFixedRate(drawer, 0, 40)
        }
    }

    override fun surfaceChanged(p0: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {}

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        //Прерываем поток при уничтожении surface
        drawer?.cancel()
        drawer = null
        timer?.cancel()
        timer = null
    }

    override fun onScroll(distanceX: Float, distanceY: Float) {
        scene?.let {
            //не даем канвасу показать края по горизонтали
            if (it.scrollAxis.x + distanceX < it.width.toScale() && it.scrollAxis.x + distanceX > 0) {
                scene?.scrollAxis = Axis((it.scrollAxis.x + distanceX).toInt(), it.scrollAxis.y)
            }
            //не даем канвасу показать края по вертикали
            if (it.scrollAxis.y + distanceY < it.height.toScale() && it.scrollAxis.y + distanceY > 0) {
                scene?.scrollAxis = Axis(it.scrollAxis.x, (it.scrollAxis.y + distanceY).toInt())
            }
        }
    }

    private fun Int.toScale(): Int = (this * (mScaleFactor - 1)).toInt()

    override fun onScale() {
        val scaleFactor = scaleGestureDetector.scaleFactor//получаем значение зума относительно предыдущего состояния
        //получаем координаты фокальной точки - точки между пальцами
        val focusX = scaleGestureDetector.focusX
        val focusY = scaleGestureDetector.focusY
        scene?.let {
            //следим чтобы канвас не уменьшили меньше исходного размера и не допускаем увеличения больше чем в 2 раза
            if (mScaleFactor * scaleFactor > 1 && mScaleFactor * scaleFactor < 2) {
                mScaleFactor *= scaleGestureDetector.scaleFactor
                it.mScaleFactor = mScaleFactor
                //используется при расчетах
                //по умолчанию после зума канвас отскролит в левый верхний угол. Скролим канвас так, чтобы на экране оставалась обасть канваса, над которой был
                //жест зума
                //Для получения данной формулы достаточно школьных знаний математики (декартовы координаты).
                var scrollX = ((scrollX + focusX) * scaleFactor - focusX).toInt()
                scrollX = min(
                        it.scrollAxis.x + max(scrollX, 0),
                        it.width.toScale()
                )
                var scrollY = ((scrollY + focusY) * scaleFactor - focusY).toInt()
                scrollY = min(
                        it.scrollAxis.y + max(scrollY, 0),
                        it.height.toScale()
                )
                it.scrollAxis = Axis(scrollX, scrollY)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    enum class LayerType(val id: Int) {
        BACKGROUND(0),
        ENTITY(2)
    }
}

interface Callback {

    fun onSingleTapConfirmed(axis: Axis)

}