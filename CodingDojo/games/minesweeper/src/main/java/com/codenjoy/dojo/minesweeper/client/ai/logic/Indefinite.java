package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class Indefinite {

    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private List<Cell> cells;
    private int[] mines;
    private BigInteger[] combs;
    private BigInteger summaryCombs;

    public Indefinite(List<Cell> cells) {
        this.cells = cells;
        mines = new int[cells.size()];
        combs = new BigInteger[cells.size()];
        summaryCombs = zero;
        init();
    }

    private void init() {
        for (int i = 0; i < combs.length; ++i) {
            combs[i] = zero;
            (cells.get(i)).setBigInteger(zero);
        }
    }

    public void addArrayComb(int[] array) {
        summaryCombs = summaryCombs.add(one);

        for (int i = 0; i < mines.length; ++i) {
            int[] var10000 = mines;
            var10000[i] += array[i];
        }

    }

    public int[] getMines() {
        return mines;
    }
}
