//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.MinesAndCombinationAmountsOfIsland;
import com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.v2.IslandMinesCombs;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Island {
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Group> list = new ArrayList();
    private List<Cell> toOpen = new ArrayList();
    private List<Cell> toMark = new ArrayList();
    private List<com.codenjoy.dojo.minesweeper.client.ai.logic.Group> indefinite = new ArrayList();
    private int amountCells;
    private Deque<StringBuilder> stack = new LinkedList();
    private List<Cell> indefiniteCells;
    private double[] possibilities;
    private Integer[] countOfMines;
    private List<Cell> minPossibilities;
    private com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.MinesAndCombinationAmountsOfIsland mx0;
    private IslandMinesCombs islandMinesCombs;
    private double minPossibility;

    public Island(com.codenjoy.dojo.minesweeper.client.ai.logic.Group group) {
        this.add(group);
    }

    public void add(com.codenjoy.dojo.minesweeper.client.ai.logic.Group group) {
        this.list.add(group);
    }

    public void add(Island island) {
        this.list.addAll(island.list);
    }

    public boolean isCross(com.codenjoy.dojo.minesweeper.client.ai.logic.Group group) {
        Iterator i$ = this.list.iterator();

        com.codenjoy.dojo.minesweeper.client.ai.logic.Group group1;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            group1 = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)i$.next();
        } while(!group.isCross(group1));

        return true;
    }

    public void optimize() {
        this.doFractionZeroGroupsInList();

        boolean repeat;
        do {
            repeat = false;

            for(int i = 0; i < this.list.size() - 1; ++i) {
                com.codenjoy.dojo.minesweeper.client.ai.logic.Group first = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)this.list.get(i);
                if (first.getValue() < 0) {
                    this.list.remove(i--);
                }

                for(int j = i + 1; j < this.list.size(); ++j) {
                    com.codenjoy.dojo.minesweeper.client.ai.logic.Group second = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)this.list.get(j);
                    if (first != second && first.isCross(second)) {
                        if (first.equals(second)) {
                            this.list.remove(j--);
                        } else if (first.contains(second)) {
                            first.subtraction(second);
                            repeat = true;
                        } else if (second.contains(first)) {
                            second.subtraction(first);
                            repeat = true;
                        } else {
                            com.codenjoy.dojo.minesweeper.client.ai.logic.Group overlap = first.getOverlap(second);
                            if (overlap == null) {
                                overlap = second.getOverlap(first);
                                if (overlap != null) {
                                    second.subtraction(first);
                                    repeat = true;
                                    this.add(overlap);
                                }
                            } else {
                                first.subtraction(second);
                                repeat = true;
                                this.add(overlap);
                            }
                        }
                    }
                }
            }
        } while(repeat);

        this.determine();
        this.amountCells = this.size();
    }

    private void doFractionZeroGroupsInList() {
        for(int i = 0; i < this.list.size() - 1; ++i) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Group first = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)this.list.get(i);
            if (first.size() > 1 && first.getValue() == 0) {
                Iterator i$ = first.getList().iterator();

                while(i$.hasNext()) {
                    Cell cell = (Cell)i$.next();
                    List<Cell> cells = new ArrayList(1);
                    cells.add(cell);
                    this.list.add(new com.codenjoy.dojo.minesweeper.client.ai.logic.Group(cells, 0));
                }

                this.list.remove(i--);
            }
        }

    }

    private void setIndefiniteCells() {
        this.indefiniteCells = new ArrayList();
        Iterator i$ = this.indefinite.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Group group = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)i$.next();
            Iterator i$$ = group.getList().iterator();

            while(i$$.hasNext()) {
                Cell cell = (Cell)i$$.next();
                if (!this.indefiniteCells.contains(cell)) {
                    this.indefiniteCells.add(cell);
                }
            }
        }

        this.amountCells = this.indefiniteCells.size() + this.toOpen.size() + this.toMark.size();
    }

    public int size() {
        return this.amountCells;
    }

    private void determine() {
        Iterator i$ = this.list.iterator();

        while(true) {
            while(i$.hasNext()) {
                com.codenjoy.dojo.minesweeper.client.ai.logic.Group group = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)i$.next();
                Cell cell;
                if (group.getValue() == 0) {
                    Iterator i$$ = group.getList().iterator();

                    while(i$$.hasNext()) {
                        cell = (Cell)i$$.next();
                        this.toOpen.add(cell);
                    }
                } else if (group.size() == group.getValue()) {
                    Iterator i$$ = group.getList().iterator();

                    while(i$$.hasNext()) {
                        cell = (Cell)i$$.next();
                        this.toMark.add(cell);
                    }
                } else {
                    this.indefinite.add(group);
                }
            }

            this.setIndefiniteCells();
            return;
        }
    }

    public void resolve() {
        Iterator i$ = this.indefinite.iterator();

        while(i$.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Group group = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)i$.next();
            group.setComb();
        }

        ListIterator<com.codenjoy.dojo.minesweeper.client.ai.logic.Group> iterator = this.indefinite.listIterator();
        this.makeTree(iterator);
        this.computePossibilities();
    }

    private void setPossibilities(int amountCombs) {
        for(int i = 0; i < this.countOfMines.length; ++i) {
            ((Cell)this.indefiniteCells.get(i)).setPossibility(100.0D * (double)this.countOfMines[i] / (double)amountCombs);
        }

    }

    private void makeTree(ListIterator<com.codenjoy.dojo.minesweeper.client.ai.logic.Group> iterator) {
        if (iterator.hasNext()) {
            com.codenjoy.dojo.minesweeper.client.ai.logic.Group group = (com.codenjoy.dojo.minesweeper.client.ai.logic.Group)iterator.next();
            int combAmount = group.getCombSize();

            for(int i = 0; i < combAmount; ++i) {
                if (group.checkCombination(i)) {
                    group.storeCells();
                    group.setCellsComb(i);
                    if (this.checkIslandComb()) {
                        this.makeTree(iterator);
                    }

                    group.restoreCells();
                    if (iterator.hasPrevious()) {
                        iterator.previous();
                    }
                }
            }
        } else {
            this.storeComb();
        }

    }

    private boolean checkIslandComb() {
        Iterator i$ = this.list.iterator();

        com.codenjoy.dojo.minesweeper.client.ai.logic.Group group;
        do {
            if (!i$.hasNext()) {
                return true;
            }

            group = (Group)i$.next();
        } while(group.checkCells());

        return false;
    }

    private void storeComb() {
        StringBuilder combSB = new StringBuilder(this.list.size());
        Iterator i$ = this.indefiniteCells.iterator();

        while(i$.hasNext()) {
            Cell cell = (Cell)i$.next();
            if (cell.isMine()) {
                combSB.append('1');
            }

            if (cell.isValued()) {
                combSB.append('0');
            }

            if (cell.isUnknown()) {
                combSB.append('2');
            }
        }

        this.stack.push(combSB);
    }

    private void computePossibilities() {
        int amountOfComb = this.stack.size();
        this.countOfMines = new Integer[this.indefiniteCells.size()];
        this.possibilities = new double[this.indefiniteCells.size()];
        this.mx0 = new com.codenjoy.dojo.minesweeper.client.ai.logic.possibility.MinesAndCombinationAmountsOfIsland();
        this.mx0.setIndefiniteCells(this.indefiniteCells);
        this.islandMinesCombs = new IslandMinesCombs(this.indefiniteCells);

        int i;
        for(i = 0; i < this.countOfMines.length; ++i) {
            this.countOfMines[i] = 0;
        }

        for(i = 0; i < amountOfComb; ++i) {
            StringBuilder combSB = (StringBuilder)this.stack.pop();
            int[] mxCountOfMines = new int[this.indefiniteCells.size()];
            int mines = 0;

            for(int j = 0; j < this.indefiniteCells.size(); ++j) {
                if (combSB.charAt(j) == '1') {
                    Integer[] var7 = this.countOfMines;
                    Integer var9 = var7[j];
                    Integer var10 = var7[j] = var7[j] + 1;
                    int var10002 = mxCountOfMines[j]++;
                    ++mines;
                }
            }

            this.mx0.inc(mines);
            this.mx0.addMxCountOfMines(mines, mxCountOfMines);
            this.islandMinesCombs.incCombsByMines(mines);
            this.islandMinesCombs.addArrayCombs(mines, mxCountOfMines);
        }

        this.setPossibilities(amountOfComb);
    }

    public List<Cell> getIndefiniteCells() {
        return this.indefiniteCells;
    }

    public MinesAndCombinationAmountsOfIsland getMx0() {
        return this.mx0;
    }

    public IslandMinesCombs getIslandMinesCombs() {
        return this.islandMinesCombs;
    }

    public List<Cell> getToOpen() {
        return this.toOpen;
    }

    public List<Cell> getToMark() {
        return this.toMark;
    }

    public String toString() {
        return "size=" + this.list.size();
    }
}
