package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.*;

public class Island {

    private List<Group> list = new ArrayList();
    private List<Cell> toOpen = new ArrayList();
    private List<Cell> toMark = new ArrayList();
    private List<Group> indefinite = new ArrayList();
    private int amountCells;
    private List<Cell> indefiniteCells;

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
        return list.stream()
                .anyMatch(group::isCross);
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
        for (Group group : indefinite) {
            for (Cell cell : group.getList()) {
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
        for (Group group : list) {
            if (group.getValue() == 0) {
                for (Cell cell : group.getList()) {
                    toOpen.add(cell);
                }
            } else if (group.size() == group.getValue()) {
                for (Cell cell : group.getList()) {
                    toMark.add(cell);
                }
            } else {
                indefinite.add(group);
            }
        }

        setIndefiniteCells();
        return;
    }

    public List<Cell> getToOpen() {
        return toOpen;
    }

    public List<Cell> getToMark() {
        return toMark;
    }

}
