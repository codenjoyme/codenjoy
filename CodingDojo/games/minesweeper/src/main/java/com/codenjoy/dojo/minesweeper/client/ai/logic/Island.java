package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.DETECTOR_VALUE;
import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.NONE_VALUE;

public class Island {

    private List<Group> list = new ArrayList();
    private List<Cell> open = new ArrayList();
    private List<Cell> mark = new ArrayList();
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
                if (first.value() < 0) {
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
                            Group overlap = first.overlap(second);
                            if (overlap == null) {
                                overlap = second.overlap(first);
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
            if (first.size() > 1 && first.value() == 0) {
                for (Cell cell : first.list()) {
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
            for (Cell cell : group.list()) {
                if (!indefiniteCells.contains(cell)) {
                    indefiniteCells.add(cell);
                }
            }
        }

        amountCells = indefiniteCells.size()
                + open.size()
                + mark.size();
    }

    public int size() {
        return amountCells;
    }

    private void determine() {
        for (Group group : list) {
            if (group.value() == NONE_VALUE || group.value() == DETECTOR_VALUE) {
                for (Cell cell : group.list()) {
                    open.add(cell);
                }
            } else if (group.size() == group.value()) {
                for (Cell cell : group.list()) {
                    mark.add(cell);
                }
            } else {
                indefinite.add(group);
            }
        }

        setIndefiniteCells();
        return;
    }

    public List<Cell> open() {
        return open;
    }

    public List<Cell> mark() {
        return mark;
    }

}
