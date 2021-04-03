package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.model.Elements;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Group {

    private List<Cell> list;
    private Elements element;

    public Group(List<Cell> cells, Elements element) {
        this.element = element;

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
        if (element == Elements.NONE || element == Elements.DETECTOR) {
            return Action.GO;
        } else if (size() == element.value()) {
            return Action.MARK;
        } else {
            return Action.NOTHING;
        }
    }
}
