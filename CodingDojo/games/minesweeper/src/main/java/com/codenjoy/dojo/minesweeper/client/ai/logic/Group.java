package com.codenjoy.dojo.minesweeper.client.ai.logic;

import java.util.*;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.DETECTOR_VALUE;
import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.NONE_VALUE;

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

    public List<Action> actions() {
        List<Action> result = new LinkedList<>();
        if (value == NONE_VALUE || value == DETECTOR_VALUE) {
            for (Cell cell : list) {
                result.add(new Action(cell, false));
            }
        } else if (size() == value) {
            for (Cell cell : list) {
                result.add(new Action(cell, true));
            }
        } else {
            // indefinite
        }
        return result;
    }
}
