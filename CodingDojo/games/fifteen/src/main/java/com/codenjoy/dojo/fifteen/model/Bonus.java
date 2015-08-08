package com.codenjoy.dojo.fifteen.model;

/**
 * Created by Administrator on 02.08.2015.
 */
public class Bonus {
    private int moveCount;
    private int number;

    public Bonus(int moveCount, int number) {
        this.moveCount = moveCount;
        this.number = number;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public int getNumber() {
        return number;
    }
}
