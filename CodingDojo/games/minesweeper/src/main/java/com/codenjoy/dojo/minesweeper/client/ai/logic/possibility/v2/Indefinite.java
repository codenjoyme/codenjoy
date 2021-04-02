

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class Indefinite {
    private List<Cell> cells;
    private int[] mines;
    private BigInteger[] combs;
    private BigInteger summaryCombs;
    private final BigInteger one = new BigInteger("1");
    private final BigInteger zero = new BigInteger("0");

    public Indefinite(List<Cell> cells) {
        this.cells = cells;
        this.mines = new int[cells.size()];
        this.combs = new BigInteger[cells.size()];
        this.summaryCombs = this.zero;
        this.init();
    }

    private void init() {
        for(int i = 0; i < this.combs.length; ++i) {
            this.combs[i] = this.zero;
            ((Cell)this.cells.get(i)).setBigInteger(this.zero);
        }

    }

    public void addArrayComb(int[] array) {
        this.summaryCombs = this.summaryCombs.add(this.one);

        for(int i = 0; i < this.mines.length; ++i) {
            int[] var10000 = this.mines;
            var10000[i] += array[i];
        }

    }

    public void multiplyCombs(BigInteger value) {
        this.summaryCombs = this.summaryCombs.multiply(value);
    }

    public void refreshCellCombs(int combsInt, BigInteger deepComb) {
        BigInteger combs = (new BigInteger(String.valueOf(combsInt))).multiply(deepComb);

        for(int i = 0; i < this.mines.length; ++i) {
            Cell cell = (Cell)this.cells.get(i);
            BigInteger cellComb = cell.getBigInteger();
            BigInteger minesI = new BigInteger(String.valueOf(this.mines[i]));
            BigInteger newComb = minesI.multiply(combs);
            cell.setBigInteger(cellComb.add(newComb));
        }

    }

    public int[] getMines() {
        return this.mines;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator i$ = this.cells.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            sb.append(cell.getBigInteger()).append(' ');
        }

        return sb.toString();
    }
}
