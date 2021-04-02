

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IslandMinesCombs {
    private List<MinesCombs> minesCombses = new ArrayList();
    private List<Cell> indefinite;

    public IslandMinesCombs(List<Cell> indefinite) {
        this.indefinite = indefinite;
    }

    private void add(MinesCombs minesCombs) {
        minesCombs.setIndefinite(new Indefinite(this.indefinite));
        this.minesCombses.add(minesCombs);
    }

    public int size() {
        return this.minesCombses.size();
    }

    public MinesCombs get(int i) {
        return this.minesCombses.get(i);
    }

    public void incCombsByMines(int mines) {
        MinesCombs minesCombs = this.getByMines(mines);
        if (minesCombs == null) {
            minesCombs = new MinesCombs(mines);
            this.add(minesCombs);
        }

        minesCombs.addComb(1);
    }

    private MinesCombs getByMines(int mines) {
        Iterator i$ = this.minesCombses.iterator();

        MinesCombs minesCombs;
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
            BigInteger was = (this.indefinite.get(i)).getBigInteger();
            BigInteger b1 = handred.multiply(was);
            BigInteger b3 = b1.divide(deepCombSum);
            this.indefinite.get(i).setPossibility(b3.doubleValue() / 1000.0D);
        }

    }

    public void addArrayCombs(int mines, int[] array) {
        this.getByMines(mines).addArrayComb(array);
    }
}
