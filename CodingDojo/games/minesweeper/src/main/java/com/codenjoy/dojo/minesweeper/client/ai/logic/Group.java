package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.*;

public class Group {

    private List<Cell> list;
    private int value;

    public Group(List<Cell> cells, int value) {
        list = new ArrayList(cells);
        this.value = value;
    }

    public List<Cell> list() {
        return list;
    }

    public int size() {
        return list.size();
    }

    public int value() {
        return value;
    }

    public boolean isCross(Group group) {
        return group.list.stream()
                .anyMatch(list::contains);
    }

    public void subtraction(Group group) {
        list.removeAll(group.list);
        value -= group.value;
    }

    public boolean contains(Group group) {
        return list.containsAll(group.list);
    }

    public Group overlap(Group group) {
        List<Cell> overlap = new ArrayList();

        for (Cell cell : group.list) {
            if (list.contains(cell)) {
                overlap.add(cell);
            }
        }

        int mine = value - (list.size() - overlap.size());
        if (mine != group.value) {
            return null;
        } else {
            return new Group(overlap, mine);
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o != null && getClass() == o.getClass()) {
            Group group = (Group) o;
            if (value != group.value) {
                return false;
            }

            if (list.size() != group.list.size()) {
                return false;
            }

            return group.list.containsAll(list);
        }

        return false;
    }

    public int hashCode() {
        return 31 * list.hashCode() + value;
    }
}
