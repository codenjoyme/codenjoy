package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Group {

    private List<Cell> list;
    private Value value;

    public Group(List<Cell> cells, Value value) {
        this.value = value;

        list = cells.stream()
                    .map(Cell::copy)
                    .collect(toList());

        Action action = action();
        list.forEach(cell -> cell.action(action));
    }

    public List<Cell> list() {
        return list;
    }

    public int size() {
        return list.size();
    }

    private Action action() {
        if (value == Value.NONE || value == Value.DETECTOR) {
            return Action.GO;
        } else if (size() == value.get()) {
            return Action.MARK;
        } else {
            return Action.NOTHING;
        }
    }
}
