package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Alexander Vlasov
 */
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
        combs += value;
    }

    public int getMines() {
        return mines;
    }

    public void addArrayComb(int[] array) {
        indefinite.addArrayComb(array);
    }

    public int getCombs() {
        return combs;
    }

    public void refreshCellCombs(int combs, BigInteger deepComb) {
        indefinite.refreshCellCombs(combs / this.combs, deepComb);
    }

    public void multiplyCombs(BigInteger value) {
        indefinite.multiplyCombs(value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(toShortString());
        sb.append(" (");
        sb.append(Arrays.toString(indefinite.getMines()));
        sb.append(')');
        return sb.toString();
    }

    public String toShortString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(mines).append("->").append(combs);
        return sb.toString();
    }
}
