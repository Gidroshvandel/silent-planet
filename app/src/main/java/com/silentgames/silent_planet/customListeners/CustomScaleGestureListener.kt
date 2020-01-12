package com.silentgames.silent_planet.customListeners

import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener

/**
 * Created by gidroshvandel on 21.06.17.
 */
class CustomScaleGestureListener(private val callback: Callback) : SimpleOnScaleGestureListener() {

    interface Callback {
        fun onScale()
    }

    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        callback.onScale()
        return true
    }

}