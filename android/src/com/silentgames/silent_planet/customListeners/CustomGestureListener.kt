package com.silentgames.silent_planet.customListeners

import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent

/**
 * Created by gidroshvandel on 21.06.17.
 */
class CustomGestureListener(private val callback: Callback) : SimpleOnGestureListener() {

    interface Callback {
        fun onScroll(distanceX: Float, distanceY: Float)
        fun onSingleTapConfirmed(event: MotionEvent?)
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        callback.onScroll(distanceX, distanceY)
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        callback.onSingleTapConfirmed(event)
        return true
    }
}
