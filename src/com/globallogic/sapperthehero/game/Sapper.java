package com.globallogic.sapperthehero.game;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sapper extends Cell {
    private boolean isDead = false;
    private MineDetector mineDetector;

    public Sapper(int xPosition, int yPosition) {
        super(xPosition, yPosition);
    }

    public void displaceMeByDelta(Cell deltaCell) {
        super.changeMyCoordinate(deltaCell);
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
}
