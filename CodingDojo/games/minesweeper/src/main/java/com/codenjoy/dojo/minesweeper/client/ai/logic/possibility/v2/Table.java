

package com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2;

import com.codenjoy.dojo.minesweeper.client.ai.logic.Sequence6;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Table {
    private Map<Integer, TableCellGroup> groups;
    private List<TableCell> tableCells = new ArrayList();
    private Map<Integer, DeepIsland> deepMap;
    private int minesLeft;
    private int deepAmount;

    public Table() {
    }

    public void setMinesLeft(int minesLeft) {
        this.minesLeft = minesLeft;
    }

    public void setDeepAmount(int deepAmount) {
        this.deepAmount = deepAmount;
    }

    public void add(TableCell tableCell) {
        this.tableCells.add(tableCell);
    }

    public void createGroups() {
        this.groups = new HashMap();

        TableCell tableCell;
        TableCellGroup group;
        for(Iterator i$ = this.tableCells.iterator(); i$.hasNext(); group.add(tableCell)) {
            tableCell = (TableCell)i$.next();
            int mines = tableCell.getMines();
            group = this.groups.get(mines);
            if (group == null) {
                group = new TableCellGroup(mines);
                this.groups.put(mines, group);
            }
        }

    }

    public void createDeepMap() {
        this.deepMap = new HashMap();
        Iterator i$ = this.groups.entrySet().iterator();

        while(i$.hasNext()) {
            Entry<Integer, TableCellGroup> entry = (Entry)i$.next();
            int groupMines = entry.getKey();
            int deepMines = this.minesLeft - groupMines;
            BigInteger deepCombs = Sequence6.getBigIntegerAmount(deepMines, this.deepAmount);
            DeepIsland deepIsland = new DeepIsland(deepMines, deepCombs);
            this.deepMap.put(groupMines, deepIsland);
        }

    }

    public void calculate() {
        Iterator i$ = this.tableCells.iterator();

        while(i$.hasNext()) {
            TableCell tableCell = (TableCell)i$.next();
            int mines = tableCell.getMines();
            DeepIsland deepIsland = this.deepMap.get(mines);
            BigInteger deepCombs = deepIsland.getDeepCombs();
            tableCell.multiplyCombs(deepCombs);
        }

    }

    public BigInteger getDeepCombSum() {
        BigInteger deepCombSum = new BigInteger("0");

        BigInteger deepCombs;
        BigInteger groupCombs;
        for(Iterator i$ = this.deepMap.entrySet().iterator(); i$.hasNext(); deepCombSum = deepCombSum.add(deepCombs.multiply(groupCombs))) {
            Entry<Integer, DeepIsland> entry = (Entry)i$.next();
            int mines = entry.getKey();
            DeepIsland deepIsland = entry.getValue();
            deepCombs = deepIsland.getDeepCombs();
            groupCombs = new BigInteger(String.valueOf((this.groups.get(mines)).getCombs()));
        }

        return deepCombSum;
    }

    public void refreshCellCombs() {
        Iterator i$ = this.tableCells.iterator();

        while(i$.hasNext()) {
            TableCell tableCell = (TableCell)i$.next();
            int mines = tableCell.getMines();
            BigInteger deepComb = this.deepMap.get(mines).getDeepCombs();
            tableCell.refreshCellCombs(deepComb);
        }

    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator i$ = this.tableCells.iterator();

        while(i$.hasNext()) {
            TableCell tableCell = (TableCell)i$.next();
            sb.append(tableCell.toShortString()).append(", ");
        }

        return sb.toString();
    }
}
