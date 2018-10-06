package com.silentgames.silent_planet.engine.base

import android.graphics.Paint
import java.util.*


open class Layer {

    var data = LinkedList<Basic>()

    /**
     * Кисть, которой должен отрисовываться слой
     */
    var paint: Paint

    private var processing = false

    /**
     * Конструктор принимает в качестве параметра номер слоя
     *
     * @param lev
     * номер слоя на сцене
     */
    init {
        processing = true
        paint = Paint()
        processing = false
    }

    /**
     * добавление на слой объектов
     *
     * @param item
     * - графический объект
     */
    fun add(item: Basic) {
        processing = true
        data.add(item)
        processing = false
    }

    /**
     * @return возвращает количество объектов на слое
     */
    fun getSize(): Int {
        return data.size
    }

    /**
     * Возвращает объект **Basic** c номером **i**
     *
     * @param i
     * номер объекта
     * @return объект со слоя с номером **i**
     */
    operator fun get(i: Int): Basic {
        return data[i]
    }

    /**
     * Удаляет объект с номером i со слоя
     *
     * @param i
     */
    fun delete(i: Int) {
        processing = true
        data.removeAt(i)
        processing = false
    }

    /**
     * Очищает текущий солой
     */
    fun clear() {
        processing = true
        data.clear()
        processing = false
    }

    /**
     * обновляет все объекты на слое
     */
    fun update() {
        processing = true
        for (a in data) {
            a.update()
        }
        processing = false
    }

//     /**
//     * выбирает первый объект на слое, в который попадает точка с кооординатами
//     * (f,g)
//     *
//     * @param f
//     * координата по x
//     * @param g
//     * координата по y
//     * @return объект со слоя
//     */
//    fun select(f: Float, g: Float): Basic? {
//        var tmp: Basic? = null
//        for (i in 0 until data.size) {
//            if (data[i].isSelected(f, g)) {
//                tmp = data[i]
//                break
//            }
//        }
//        return tmp
//    }

    fun isProcessing(): Boolean {
        return processing
    }

}