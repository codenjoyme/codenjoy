package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Cell;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class IslandMinesCombs {
    private List<MinesCombs> minesCombses = new ArrayList<MinesCombs>();
    private List<Cell> indefinite;

    public IslandMinesCombs(List<Cell> indefinite) {
        this.indefinite = indefinite;
    }

    private void add(MinesCombs minesCombs) {
        minesCombs.setIndefinite(new Indefinite(indefinite));
        minesCombses.add(minesCombs);
    }

    public int size() {
        return minesCombses.size();
    }

    public MinesCombs get(int i) {
        return minesCombses.get(i);
    }

    public void incCombsByMines(int mines) {
        MinesCombs minesCombs = getByMines(mines);
        if (minesCombs == null) {
            minesCombs = new MinesCombs(mines);
            add(minesCombs);
        }
        minesCombs.addComb(1);

    }

    private MinesCombs getByMines(int mines) {
        for (MinesCombs minesCombs : minesCombses) {
            if (minesCombs.getMines() == mines) {
                return minesCombs;
            }
        }
        return null;
    }

    public void divideDeepCombSum(BigInteger deepCombSum) {
        BigInteger handred = new BigInteger("100000");
        for (int i = 0; i < indefinite.size(); i++) {
            BigInteger was = indefinite.get(i).getBigInteger();
            BigInteger b1 = handred.multiply(was);
            BigInteger b3 = b1.divide(deepCombSum);

            indefinite.get(i).setPossibility(b3.doubleValue() / 1000);
        }
    }

    public void addArrayCombs(int mines, int[] array) {
        getByMines(mines).addArrayComb(array);
    }
}
