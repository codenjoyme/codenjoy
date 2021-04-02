package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.*;

public class Island {

    private List<Group> list = new ArrayList();
    private List<Cell> toOpen = new ArrayList();
    private List<Cell> toMark = new ArrayList();
    private List<Group> indefinite = new ArrayList();
    private int amountCells;
    private Deque<StringBuilder> stack = new LinkedList();
    private List<Cell> indefiniteCells;
    private Integer[] countOfMines;
    private MinesAndCombinationAmountsOfIsland mx0;
    private IslandMinesCombs islandMinesCombs;

    public Island(Group group) {
        add(group);
    }

    public void add(Group group) {
        list.add(group);
    }

    public void add(Island island) {
        list.addAll(island.list);
    }

    public boolean isCross(Group group) {
        Iterator iterator = list.iterator();

        Group group1;
        do {
            if (!iterator.hasNext()) {
                return false;
            }

            group1 = (Group) iterator.next();
        } while (!group.isCross(group1));

        return true;
    }

    public void optimize() {
        doFractionZeroGroupsInList();

        boolean repeat;
        do {
            repeat = false;

            for (int i = 0; i < list.size() - 1; ++i) {
                Group first = list.get(i);
                if (first.getValue() < 0) {
                    list.remove(i--);
                }

                for (int j = i + 1; j < list.size(); ++j) {
                    Group second = list.get(j);
                    if (first != second && first.isCross(second)) {
                        if (first.equals(second)) {
                            list.remove(j--);
                        } else if (first.contains(second)) {
                            first.subtraction(second);
                            repeat = true;
                        } else if (second.contains(first)) {
                            second.subtraction(first);
                            repeat = true;
                        } else {
                            Group overlap = first.getOverlap(second);
                            if (overlap == null) {
                                overlap = second.getOverlap(first);
                                if (overlap != null) {
                                    second.subtraction(first);
                                    repeat = true;
                                    add(overlap);
                                }
                            } else {
                                first.subtraction(second);
                                repeat = true;
                                add(overlap);
                            }
                        }
                    }
                }
            }
        } while (repeat);

        determine();
        amountCells = size();
    }

    private void doFractionZeroGroupsInList() {
        for (int i = 0; i < list.size() - 1; ++i) {
            Group first = list.get(i);
            if (first.size() > 1 && first.getValue() == 0) {
                Iterator i$ = first.getList().iterator();

                while (i$.hasNext()) {
                    Cell cell = (Cell) i$.next();
                    List<Cell> cells = new ArrayList(1);
                    cells.add(cell);
                    list.add(new Group(cells, 0));
                }

                list.remove(i--);
            }
        }
    }

    private void setIndefiniteCells() {
        indefiniteCells = new ArrayList();
        Iterator iterator = indefinite.iterator();

        while (iterator.hasNext()) {
            Group group = (Group) iterator.next();
            Iterator iterator1 = group.getList().iterator();

            while (iterator1.hasNext()) {
                Cell cell = (Cell) iterator1.next();
                if (!indefiniteCells.contains(cell)) {
                    indefiniteCells.add(cell);
                }
            }
        }

        amountCells = indefiniteCells.size() + toOpen.size() + toMark.size();
    }

    public int size() {
        return amountCells;
    }

    private void determine() {
        Iterator iterator = list.iterator();

        while (true) {
            while (iterator.hasNext()) {
                Group group = (Group) iterator.next();
                Cell cell;
                if (group.getValue() == 0) {
                    Iterator iterator1 = group.getList().iterator();

                    while (iterator1.hasNext()) {
                        cell = (Cell) iterator1.next();
                        toOpen.add(cell);
                    }
                } else if (group.size() == group.getValue()) {
                    Iterator iterator1 = group.getList().iterator();

                    while (iterator1.hasNext()) {
                        cell = (Cell) iterator1.next();
                        toMark.add(cell);
                    }
                } else {
                    indefinite.add(group);
                }
            }

            setIndefiniteCells();
            return;
        }
    }

    public void resolve() {
        Iterator iterator = indefinite.iterator();

        while (iterator.hasNext()) {
            Group group = (Group) iterator.next();
            group.setComb();
        }

        makeTree(indefinite.listIterator());
        computePossibilities();
    }

    private void setPossibilities(int amountCombs) {
        for (int i = 0; i < countOfMines.length; ++i) {
            indefiniteCells.get(i).setPossibility(100.0D * (double) countOfMines[i] / (double) amountCombs);
        }

    }

    private void makeTree(ListIterator<Group> iterator) {
        if (iterator.hasNext()) {
            Group group = iterator.next();
            int combAmount = group.getCombSize();

            for (int i = 0; i < combAmount; ++i) {
                if (group.checkCombination(i)) {
                    group.storeCells();
                    group.setCellsComb(i);
                    if (checkIslandComb()) {
                        makeTree(iterator);
                    }

                    group.restoreCells();
                    if (iterator.hasPrevious()) {
                        iterator.previous();
                    }
                }
            }
        } else {
            storeComb();
        }

    }

    private boolean checkIslandComb() {
        Iterator iterator = list.iterator();

        Group group;
        do {
            if (!iterator.hasNext()) {
                return true;
            }

            group = (Group) iterator.next();
        } while (group.checkCells());

        return false;
    }

    private void storeComb() {
        StringBuilder combSB = new StringBuilder(list.size());
        Iterator i$ = indefiniteCells.iterator();

        while (i$.hasNext()) {
            Cell cell = (Cell) i$.next();
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

        stack.push(combSB);
    }

    private void computePossibilities() {
        int amountOfComb = stack.size();
        countOfMines = new Integer[indefiniteCells.size()];
        mx0 = new MinesAndCombinationAmountsOfIsland();
        islandMinesCombs = new IslandMinesCombs(indefiniteCells);

        int i;
        for (i = 0; i < countOfMines.length; ++i) {
            countOfMines[i] = 0;
        }

        for (i = 0; i < amountOfComb; ++i) {
            StringBuilder combSB = stack.pop();
            int[] mxCountOfMines = new int[indefiniteCells.size()];
            int mines = 0;

            for (int j = 0; j < indefiniteCells.size(); ++j) {
                if (combSB.charAt(j) == '1') {
                    Integer[] var7 = countOfMines;
                    var7[j] = var7[j] + 1;
                    mxCountOfMines[j]++;
                    ++mines;
                }
            }

            mx0.addMxCountOfMines(mines, mxCountOfMines);
            islandMinesCombs.incCombsByMines(mines);
            islandMinesCombs.addArrayCombs(mines, mxCountOfMines);
        }

        setPossibilities(amountOfComb);
    }

    public List<Cell> getIndefiniteCells() {
        return indefiniteCells;
    }

    public List<Cell> getToOpen() {
        return toOpen;
    }

    public List<Cell> getToMark() {
        return toMark;
    }

}
