//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Archipelag {
    private List<IslandMinesCombs> list = new ArrayList();
    private static int minesLeft;
    private static int deepAmount;
    private Table table;

    public static boolean isValid(int mines) {
        return mines <= minesLeft;
    }

    public Archipelag(int minesLeft, int deepAmount) {
        Archipelag.minesLeft = minesLeft;
        Archipelag.deepAmount = deepAmount;
    }

    public void add(IslandMinesCombs islandMinesCombs) {
        this.list.add(islandMinesCombs);
    }

    public void calculate() {
        if (this.list.size() != 0) {
            Digits digits = new Digits(this.list);
            this.table = digits.getTable();
            this.table.setMinesLeft(minesLeft);
            this.table.setDeepAmount(deepAmount);
            this.table.createGroups();
            this.table.createDeepMap();
            this.table.calculate();
            this.table.refreshCellCombs();
            this.divideDeepCombSum();
        }
    }

    private void divideDeepCombSum() {
        BigInteger deepCombSum = this.table.getDeepCombSum();
        Iterator i$ = this.list.iterator();

        while(i$.hasNext()) {
            IslandMinesCombs islandMinesCombs = (IslandMinesCombs)i$.next();
            islandMinesCombs.divideDeepCombSum(deepCombSum);
        }

    }
}
