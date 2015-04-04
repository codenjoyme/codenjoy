package com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.possibility.v2;


import com.codenjoy.dojo.minesweeper.client.ai.vaa25.logic.Sequence6;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Vlasov
 */
public class Table {
    private Map<Integer, TableCellGroup> groups;
    private List<TableCell> tableCells = new ArrayList<TableCell>();
    private Map<Integer, DeepIsland> deepMap;
    private int minesLeft;
    private int deepAmount;


    public void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
    }

    public void setDeepAmount(int deepAmount) {
        this.deepAmount = deepAmount;
    }

    public void add(TableCell tableCell) {
        tableCells.add(tableCell);

    }

    public void createGroups() {
        groups = new HashMap<Integer, TableCellGroup>();
        for (TableCell tableCell : tableCells) {
            int mines = tableCell.getMines();
            TableCellGroup group = groups.get(mines);
            if (group == null) {
                group = new TableCellGroup(mines);
                groups.put(mines, group);
            }
            group.add(tableCell);
        }
    }

    public void createDeepMap() {
        deepMap = new HashMap<Integer, DeepIsland>();
        for (Map.Entry<Integer, TableCellGroup> entry : groups.entrySet()) {
            int groupMines = entry.getKey();
            int deepMines = minesLeft - groupMines;
            BigInteger deepCombs = (Sequence6.getBigIntegerAmount(deepMines, deepAmount));
            DeepIsland deepIsland = new DeepIsland(deepMines, deepCombs);
            deepMap.put(groupMines, deepIsland);
        }
    }

    public void calculate() {
        for (TableCell tableCell : tableCells) {
            int mines = tableCell.getMines();
            DeepIsland deepIsland = deepMap.get(mines);
            BigInteger deepCombs = deepIsland.getDeepCombs();
            tableCell.multiplyCombs(deepCombs);
        }
    }

    public BigInteger getDeepCombSum() {
        BigInteger deepCombSum = new BigInteger("0");
        for (Map.Entry<Integer, DeepIsland> entry : deepMap.entrySet()) {
            int mines = entry.getKey();
            DeepIsland deepIsland = entry.getValue();
            BigInteger deepCombs = deepIsland.getDeepCombs();
            BigInteger groupCombs = new BigInteger(String.valueOf(groups.get(mines).getCombs()));
            deepCombSum = deepCombSum.add(deepCombs.multiply(groupCombs));
        }
        return deepCombSum;
    }

    public void refreshCellCombs() {
        for (TableCell tableCell : tableCells) {
            int mines = tableCell.getMines();
            BigInteger deepComb = deepMap.get(mines).getDeepCombs();
            tableCell.refreshCellCombs(deepComb);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (TableCell tableCell : tableCells) {
            sb.append(tableCell.toShortString()).append(", ");
        }
        return sb.toString();
    }
}
