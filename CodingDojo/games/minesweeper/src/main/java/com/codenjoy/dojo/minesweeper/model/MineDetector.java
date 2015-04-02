package com.codenjoy.dojo.minesweeper.model;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 5:57 PM
 */
public class MineDetector {
    private int charge;

    public MineDetector(int charge) {
        this.charge = charge;
    }

    public int getCharge() {
        return charge;
    }

    public void useMe() {
        this.charge--;
    }

}