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
