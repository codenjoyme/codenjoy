package com.globallogic.training.oleksii.morozov.sapperthehero.game.objects;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.items.MineDetector;

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

    public void displaceMeByDelta(Cell deltaCell) {
        super.changeTo(deltaCell);
    }

    public boolean isDead() {
        return isDead;
    }

    public void die(boolean b) {
        this.isDead = true;
    }

    public void useMineDetector() {
        mineDetector.useMe();
    }

    public MineDetector getMineDetector() {
        return mineDetector;
    }

    public int getMineDetectorCharge() {
        return mineDetector.getCharge();
    }

    public void iWantToHaveMineDetectorWithChargeNumber(int charge) {
        this.mineDetector = new MineDetector(charge);
    }

    @Override
    public Cell clone() {
        return super.clone();
    }

}
