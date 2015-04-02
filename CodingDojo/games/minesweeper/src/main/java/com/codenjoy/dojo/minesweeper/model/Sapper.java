package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:39 PM
 */
public class Sapper extends PointImpl implements State<Elements, Object> {
    private boolean isDead = false;
    private MineDetector mineDetector;
    private Field board;

    public Sapper(int x, int y) {
        super(x, y);
    }

    public boolean isDead() {
        return isDead;
    }

    public void die() {
        isDead = true;
    }

    private void useMineDetector() {
        if (mineDetector.getCharge() > 0) {
            mineDetector.useMe();
        }
    }

    public void iWantToHaveMineDetectorWithChargeNumber(int charge) {
        this.mineDetector = new MineDetector(charge);
    }

    public boolean isEmptyCharge() {
        return mineDetector.getCharge() == 0;
    }

    public void tryToUseDetector(DetectorAction detectorAction) {
        if (isEmptyCharge()) {
            return;
        }

        useMineDetector();

        if (detectorAction != null) {
            detectorAction.used();
        }
    }

    public MineDetector getMineDetector() {
        return mineDetector;
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (board.isSapperOnMine()) {
            return Elements.BANG;
        } else {
            return Elements.DETECTOR;
        }
    }

    public void setBoard(Field board) {
        this.board = board;
    }
}
