//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IslandMinesCombs {
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs> minesCombses = new ArrayList();
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> indefinite;

    public IslandMinesCombs(List<com.codenjoy.dojo.minesweeper.client.ai.logic.Cell> indefinite) {
        this.indefinite = indefinite;
    }

    private void add(com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs minesCombs) {
        minesCombs.setIndefinite(new Indefinite(this.indefinite));
        this.minesCombses.add(minesCombs);
    }

    public int size() {
        return this.minesCombses.size();
    }

    public com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs get(int i) {
        return (com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs)this.minesCombses.get(i);
    }

    public void incCombsByMines(int mines) {
        com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs minesCombs = this.getByMines(mines);
        if (minesCombs == null) {
            minesCombs = new com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs(mines);
            this.add(minesCombs);
        }

        minesCombs.addComb(1);
    }

    private com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs getByMines(int mines) {
        Iterator i$ = this.minesCombses.iterator();

        com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.MinesCombs minesCombs;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            minesCombs = (MinesCombs)i$.next();
        } while(minesCombs.getMines() != mines);

        return minesCombs;
    }

    public void divideDeepCombSum(BigInteger deepCombSum) {
        BigInteger handred = new BigInteger("100000");

        for(int i = 0; i < this.indefinite.size(); ++i) {
            BigInteger was = ((com.codenjoy.dojo.minesweeper.client.ai.logic.Cell)this.indefinite.get(i)).getBigInteger();
            BigInteger b1 = handred.multiply(was);
            BigInteger b3 = b1.divide(deepCombSum);
            ((Cell)this.indefinite.get(i)).setPossibility(b3.doubleValue() / 1000.0D);
        }

    }

    public void addArrayCombs(int mines, int[] array) {
        this.getByMines(mines).addArrayComb(array);
    }
}
