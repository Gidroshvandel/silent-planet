package com.silentgames.silent_planet.engine.base

import android.graphics.Canvas
import android.graphics.Paint

abstract class Basic(
//        /**
//         * Указывает на тип объекта-наследника класса mBasic присваивается в
//         * конструкторах классов потомков. Для каждого класса потомка свой.
//         */
//        var type: Int
) {

//    companion object {
//         /**
//         * Тип простой спрайт
//         */
//         const val TYPE_SPRITE = 0
//    }

    internal abstract fun update()

    /**
     * Метод отрисовки
     * @param canvas - канва для отрисовки
     * @param paint - кисть для отрисовки
     */
    internal abstract fun draw(canvas: Canvas, paint: Paint)

}