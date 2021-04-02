//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

public class TableCell {
    private int mines;
    private int combs = 1;
    private List<MinesCombs> minesCombsList;

    public TableCell(List<MinesCombs> minesCombsList) {
        this.minesCombsList = minesCombsList;

        MinesCombs minesCombs;
        for(Iterator i$ = minesCombsList.iterator(); i$.hasNext(); this.combs *= minesCombs.getCombs()) {
            minesCombs = (MinesCombs)i$.next();
            this.mines += minesCombs.getMines();
        }

    }

    public int getMines() {
        return this.mines;
    }

    public int getCombs() {
        return this.combs;
    }

    public void multiplyCombs(BigInteger value) {
        Iterator i$ = this.minesCombsList.iterator();

        while(i$.hasNext()) {
            MinesCombs minesCombs = (MinesCombs)i$.next();
            minesCombs.multiplyCombs(value);
        }

    }

    public void refreshCellCombs(BigInteger deepComb) {
        Iterator i$ = this.minesCombsList.iterator();

        while(i$.hasNext()) {
            MinesCombs minesCombs = (MinesCombs)i$.next();
            minesCombs.refreshCellCombs(this.combs, deepComb);
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.toShortString());
        sb.append(" (");
        Iterator i$ = this.minesCombsList.iterator();

        while(i$.hasNext()) {
            MinesCombs minesCombs = (MinesCombs)i$.next();
            sb.append(minesCombs.toShortString()).append(" ");
        }

        sb.append(')');
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mines).append("->").append(this.combs);
        return sb.toString();
    }
}
