package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.math.BigInteger;
import java.util.List;

public class Indefinite {

    private BigInteger one = new BigInteger("1");
    private BigInteger zero = new BigInteger("0");
    private int[] mines;
    private BigInteger[] combs;
    private BigInteger summary;

    public Indefinite(List<Cell> cells) {
        mines = new int[cells.size()];
        combs = new BigInteger[cells.size()];
        summary = zero;
        init();
    }

    private void init() {
        for (int i = 0; i < combs.length; ++i) {
            combs[i] = zero;
        }
    }

    public void addArrayComb(int[] array) {
        summary = summary.add(one);
        for (int i = 0; i < mines.length; ++i) {
            mines[i] += array[i];
        }
    }

    public int[] getMines() {
        return mines;
    }
}
