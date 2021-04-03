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

    public void determine() {
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
        amountCells = size();
    }

    public List<Cell> open() {
        return open;
    }

    public List<Cell> mark() {
        return mark;
    }

}
