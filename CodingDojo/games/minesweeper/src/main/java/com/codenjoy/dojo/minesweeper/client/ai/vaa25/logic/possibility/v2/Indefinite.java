package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Cell;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class Indefinite {
    private List<Cell> cells;
    private int[] mines;
    private BigInteger[] combs;
    private BigInteger summaryCombs;
    private final BigInteger one = new BigInteger("1");
    private final BigInteger zero = new BigInteger("0");

    public Indefinite(List<Cell> cells) {
        this.cells = cells;
        mines = new int[cells.size()];
        combs = new BigInteger[cells.size()];
        summaryCombs = zero;
        init();
    }

    private void init() {

        for (int i = 0; i < combs.length; i++) {
            combs[i] = zero;
            cells.get(i).setBigInteger(zero);
        }
    }

    public void addArrayComb(int[] array) {
        summaryCombs = summaryCombs.add(one);
        for (int i = 0; i < mines.length; i++) {
            mines[i] += array[i];
        }
    }

    public void multiplyCombs(BigInteger value) {
        summaryCombs = summaryCombs.multiply(value);
    }

    public void refreshCellCombs(int combsInt, BigInteger deepComb) {
        BigInteger combs = new BigInteger(String.valueOf(combsInt)).multiply(deepComb);
        for (int i = 0; i < mines.length; i++) {
            Cell cell = cells.get(i);
            BigInteger cellComb = cell.getBigInteger();
            BigInteger minesI = new BigInteger(String.valueOf(mines[i]));
            BigInteger newComb = (minesI).multiply(combs);
            cell.setBigInteger(cellComb.add(newComb));
        }
    }


    public int[] getMines() {
        return mines;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (Cell cell : cells) {
            sb.append(cell.getBigInteger()).append(' ');
        }
        return sb.toString();
    }

}
