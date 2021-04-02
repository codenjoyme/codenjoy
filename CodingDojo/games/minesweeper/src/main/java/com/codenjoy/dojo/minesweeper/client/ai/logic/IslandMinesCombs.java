package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IslandMinesCombs {

    private List<MinesCombs> combses;
    private List<Cell> indefinite;

    public IslandMinesCombs(List<Cell> indefinite) {
        this.indefinite = indefinite;
        combses = new ArrayList();
    }

    private void add(MinesCombs input) {
        input.setIndefinite(new Indefinite(indefinite));
        combses.add(input);
    }

    public int size() {
        return combses.size();
    }

    public MinesCombs get(int i) {
        return combses.get(i);
    }

    public void incCombsByMines(int mines) {
        MinesCombs minesCombs = getByMines(mines);
        if (minesCombs == null) {
            minesCombs = new MinesCombs(mines);
            add(minesCombs);
        }
    }

    private MinesCombs getByMines(int mines) {
        Iterator iterator = combses.iterator();

        MinesCombs minesCombs;
        do {
            if (!iterator.hasNext()) {
                return null;
            }

            minesCombs = (MinesCombs) iterator.next();
        } while (minesCombs.getMines() != mines);

        return minesCombs;
    }

    public void addArrayCombs(int mines, int[] array) {
        getByMines(mines).addArrayComb(array);
    }
}
