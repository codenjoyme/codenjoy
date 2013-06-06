package com.codenjoy.dojo.minesweeper.model.objects;

import com.codenjoy.dojo.minesweeper.model.DetectorAction;
import com.codenjoy.dojo.minesweeper.model.MineDetector;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:39 PM
 */
public class Sapper extends CellImpl {
    private boolean isDead = false;
    private MineDetector mineDetector;

    public Sapper(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    public boolean isDead() {
        return isDead;
    }

    public void die(boolean b) {
        this.isDead = true;
    }

    private void useMineDetector() {
        if (mineDetector.getCharge() > 0) {
            mineDetector.useMe();
        }
    }

    public void iWantToHaveMineDetectorWithChargeNumber(int charge) {
        this.mineDetector = new MineDetector(charge);
    }

    @Override
    public Cell clone() {
        return super.clone();
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
}
