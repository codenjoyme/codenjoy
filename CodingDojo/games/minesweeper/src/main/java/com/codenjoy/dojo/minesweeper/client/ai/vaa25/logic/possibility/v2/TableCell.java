package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class TableCell {
    private int mines;
    private int combs = 1;
    private List<MinesCombs> minesCombsList;

    public TableCell(List<MinesCombs> minesCombsList) {
        this.minesCombsList = minesCombsList;
        for (MinesCombs minesCombs : minesCombsList) {
            mines += minesCombs.getMines();
            combs *= minesCombs.getCombs();
        }
    }

    public int getMines() {
        return mines;
    }

    public int getCombs() {
        return combs;
    }

    public void multiplyCombs(BigInteger value) {
        for (MinesCombs minesCombs : minesCombsList) {
            minesCombs.multiplyCombs(value);
        }
    }

    public void refreshCellCombs(BigInteger deepComb) {
        for (MinesCombs minesCombs : minesCombsList) {
            minesCombs.refreshCellCombs(combs, deepComb);
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(toShortString());
        sb.append(" (");
        for (MinesCombs minesCombs : minesCombsList) {
            sb.append(minesCombs.toShortString()).append(" ");
        }
        sb.append(')');
        return sb.toString();
    }

    public String toShortString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(mines).append("->").append(combs);
        return sb.toString();
    }
}
