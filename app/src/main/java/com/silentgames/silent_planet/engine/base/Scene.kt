package com.silentgames.silent_planet.engine.base


class Scene(
        /**
         * Массив слоев на сцене
         */
        private var layers: MutableList<Layer>) {

    companion object {
        const val ORIENTATION_VERT = 0
        const val ORIENTATION_HOR = 1
    }

    /**
     * высота и ширина сцены
     */
    private var width: Int = 0
    private var height: Int = 0


    /**
     * номер текущего слоя
     */
    private var curLay: Int = 0


    private var orient: Int = ORIENTATION_VERT

    /**
     * Конструктор Создание новой сцены
     *
     * @param width
     * - ширина
     * @param height
     * - высота
     * @param layerCount
     * - количество слоев
     */
    init {
        curLay = 0
    }

    fun setOrientation() {
        orient = if (this.width > this.height) {
            ORIENTATION_HOR
        } else {
            ORIENTATION_VERT
        }
    }

    /**
     * Установить новую ширину и высоту сцены
     *
     * @param w
     * - новая ширина
     * @param h
     * - новая высота
     */
    fun setWH(w: Int, h: Int) {
        this.width = w
        this.height = h
    }

    /**
     * Установить текущий слой
     *
     * @param i
     */
    fun setCurLay(i: Int) {
        curLay = if (i <= getLayerCount() - 1)
            i
        else
            getLayerCount() - 1
    }

    /**
     * Добавить объект на текущий слой сцены
     *
     * @param item
     */
    fun addItem(item: Basic) {
        layers[curLay].add(item)
    }

    /**
     * @return Возвращает номер текущего слоя
     */
    fun getCurlayNum(): Int {
        return this.curLay
    }

    /**
     * @return возвращает текущий слой
     */
    fun getCurLay(): Layer {
        return layers[this.curLay]
    }

    /**
     * Очищает текущий слой
     */
    fun clear() {
        this.layers[this.curLay].clear()
    }

    /**
     * @return возвращает количество слоев на сцене
     */
    fun getLayerCount(): Int {
        return layers.size
    }

    /**
     * удаляет с текущего слоя объект с номером i
     *
     * @param i
     */
    fun delete(i: Int) {
        this.layers[this.curLay].delete(i)
    }

    fun getLayerByNum(num: Int): Layer? {
        return if (num < getLayerCount()) {
            layers[num]
        } else null
    }

    /**
     * @return Возвращает ориентацию экрана
     * 1 - если горизонтальная
     * 0 - если вертикальная
     * ориетация расчитывается исходя из того
     * что больше высота или ширина сцены
     */
    fun getOrient(): Int {
        return orient
    }

    /**
     * Обновляет все соержимое сцены
     */
    fun update() {
        for (l in layers) {
            l.update()
        }
    }

}