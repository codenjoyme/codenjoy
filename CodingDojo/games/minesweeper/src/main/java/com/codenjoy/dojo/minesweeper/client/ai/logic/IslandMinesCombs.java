package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IslandMinesCombs {

    private final List<MinesCombs> minesCombses = new ArrayList();
    private final List<Cell> indefinite;

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
    }

    private MinesCombs getByMines(int mines) {
        Iterator i$ = minesCombses.iterator();

        MinesCombs minesCombs;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            minesCombs = (MinesCombs) i$.next();
        } while (minesCombs.getMines() != mines);

        return minesCombs;
    }

    public void addArrayCombs(int mines, int[] array) {
        getByMines(mines).addArrayComb(array);
    }
}
