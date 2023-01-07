package com.silentgames.silent_planet.engine.base

import android.graphics.Canvas
import android.view.SurfaceHolder
import java.util.*

class DrawerTask(
    private val holder: SurfaceHolder,
    private val scene: Scene,
    private val onSceneUpdated: ((Boolean) -> Unit)
) : TimerTask() {

    override fun run() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()

            canvas.translate(-scene.scrollAxis.x, -scene.scrollAxis.y)
            canvas.scale(
                scene.mScaleFactor,
                scene.mScaleFactor
            ) // зумируем канвас

            canvas.drawRGB(9, 9, 9)
            scene.getLayers().forEach { layer ->
                val mainPaint = layer.paint
                for (tmp in layer.data) {
                    tmp.draw(canvas, mainPaint)
                }
            }
            scene.update {
                onSceneUpdated.invoke(true)
            }
            onSceneUpdated.invoke(false)
        } catch (e: Exception) {
        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}
