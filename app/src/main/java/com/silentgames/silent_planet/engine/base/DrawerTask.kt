package com.silentgames.silent_planet.engine.base

import android.graphics.Canvas
import android.view.SurfaceHolder
import java.util.*


class DrawerTask(
        private val holder: SurfaceHolder,
        private val scene: Scene
) : TimerTask() {

    override fun run() {
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            canvas.drawRGB(9, 9, 9)
            for (l in 0 until scene.getLayerCount()) {
                val layer = scene.getLayerByNum(l)
                if (layer != null) {
                    val mainPaint = layer.paint
                    for (tmp in layer.data) {
                        tmp.draw(canvas, mainPaint)
                    }

                }
            }
            scene.update()
        } catch (e: Exception) {

        } finally {
            if (canvas != null) {
                holder.unlockCanvasAndPost(canvas)
            }
        }
    }
}