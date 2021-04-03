package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Cell extends PointImpl {

    private int value;
    private boolean valued = false;
    private boolean unknown = true;
    private final List<Cell> neighbours = new ArrayList();

    public Cell(int x, int y) {
        super(x, y);
    }

    public void addNeighbour(Cell cell) {
        neighbours.add(cell);
    }

    public boolean isValued() {
        return valued;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
        valued = true;
        unknown = false;
    }

    public void setMine() {
        unknown = false;
        valued = false;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public void setUnknown() {
        unknown = true;
        valued = false;
    }

    public List<Cell> getUnknownCells() {
        return neighbours.stream()
                .filter(Cell::isUnknown)
                .collect(toList());
    }

    public boolean hasUnknownAround() {
        return neighbours.stream()
                .anyMatch(Cell::isUnknown);
    }
}
