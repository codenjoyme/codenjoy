package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Vlasov
 */
public class Archipelag {
    private List<IslandMinesCombs> list = new ArrayList<IslandMinesCombs>();
    private static int minesLeft, deepAmount;
    private Table table;

    public static boolean isValid(int mines) {
        return mines <= minesLeft;
    }

    public Archipelag(int minesLeft, int deepAmount) {
        Archipelag.minesLeft = minesLeft;
        Archipelag.deepAmount = deepAmount;
    }

    public void add(IslandMinesCombs islandMinesCombs) {
        list.add(islandMinesCombs);
    }

    public void calculate() {
        if (list.size() == 0) return;
        Digits digits = new Digits(list);
        table = digits.getTable();
        table.setMinesLeft(minesLeft);
        table.setDeepAmount(deepAmount);
        table.createGroups();
        table.createDeepMap();
        table.calculate();
        table.refreshCellCombs();
        divideDeepCombSum();
    }

    private void divideDeepCombSum() {
        BigInteger deepCombSum = table.getDeepCombSum();
        for (IslandMinesCombs islandMinesCombs : list) {
            islandMinesCombs.divideDeepCombSum(deepCombSum);
        }
    }

}
