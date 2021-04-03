package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.model.Elements;

import java.util.List;

import static com.codenjoy.dojo.minesweeper.client.ai.logic.Action.*;
import static com.codenjoy.dojo.minesweeper.model.Elements.*;
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
        if (element == NONE || element == DETECTOR) {
            return GO;
        } else if (size() == element.value()) {
            return MARK;
        } else {
            return NOTHING;
        }
    }
}
