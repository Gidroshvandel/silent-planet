package com.silentgames.silent_planet.model.cells.onVisible;

import com.silentgames.silent_planet.model.cells.DefaultCellClass;

/**
 * Created by gidroshvandel on 13.07.16.
 */
public class OnVisible<T> extends DefaultCellClass {

//    private DefaultCellClass<T> type;

        public OnVisible(DefaultCellClass<T> type){
            setBitmap(type.getBitmap());
            setCanFly(type.isCanFly());
            setCanMove(type.isCanMove());
            setDead(type.isDead());
        }

//    private EmptyCell emptyCell;
//    private DeadCell deadCell;
//
//    public OnVisible(DeadCell deadCell) {
//        this.deadCell = deadCell;
//        setDead(deadCell.isDead());
//        setBitmap(deadCell.getBitmap());
//    }
//
//    public OnVisible(EmptyCell emptyCell) {
//        this.emptyCell = emptyCell;
//        setBitmap(emptyCell.getBitmap());
//        setCanFly(emptyCell.isCanFly());
//        setCanMove(emptyCell.isCanMove());
//    }
//
//    public DeadCell getDeadCell() {
//        return deadCell;
//    }
//
//    public void setDeadCell(DeadCell deadCell) {
//        this.deadCell = deadCell;
//    }
//
//    public EmptyCell getEmptyCell() {
//        return emptyCell;
//    }
//
//    public void setEmptyCell(EmptyCell emptyCell) {
//        this.emptyCell = emptyCell;
//    }
}
