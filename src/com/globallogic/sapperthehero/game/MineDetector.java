package com.globallogic.sapperthehero.game;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class MineDetector {
    public MineDetector(int charge) {
        this.charge = charge;
    }

    private int charge;

    public int getCharge() {
        return charge;
    }

    public void useMe() {
        this.charge--;
    }
}
