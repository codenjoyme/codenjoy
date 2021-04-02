

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import java.math.BigInteger;
import java.util.Arrays;

public class MinesCombs {
    private int mines;
    private int combs;
    private Indefinite indefinite;

    public MinesCombs(int mines) {
        this.mines = mines;
    }

    public void setIndefinite(Indefinite indefinite) {
        this.indefinite = indefinite;
    }

    public void addComb(int value) {
        this.combs += value;
    }

    public int getMines() {
        return this.mines;
    }

    public void addArrayComb(int[] array) {
        this.indefinite.addArrayComb(array);
    }

    public int getCombs() {
        return this.combs;
    }

    public void refreshCellCombs(int combs, BigInteger deepComb) {
        this.indefinite.refreshCellCombs(combs / this.combs, deepComb);
    }

    public void multiplyCombs(BigInteger value) {
        this.indefinite.multiplyCombs(value);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.toShortString());
        sb.append(" (");
        sb.append(Arrays.toString(this.indefinite.getMines()));
        sb.append(')');
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mines).append("->").append(this.combs);
        return sb.toString();
    }
}
