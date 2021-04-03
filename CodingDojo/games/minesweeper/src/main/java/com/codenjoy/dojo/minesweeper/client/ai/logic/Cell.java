package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Cell extends PointImpl {

    private int value;
    private boolean valued = false;
    private boolean unknown = true;
    private List<Cell> neighbours = new ArrayList();
    private Action action;

    public Cell(int x, int y) {
        super(x, y);
    }

    public void addNeighbour(Cell cell) {
        neighbours.add(cell);
    }

    public boolean isValued() {
        return valued;
    }

    public int value() {
        return value;
    }

    public List<Cell> neighbours() {
        return neighbours;
    }

    public void action(Action action) {
        this.action = action;
    }

    public Action action() {
        return action;
    }

    public void value(int value) {
        this.value = value;
        valued = true;
        unknown = false;
    }

    public void setMine() {
        value = Value.FLAG;
        unknown = false;
        valued = true;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public void setUnknown() {
        unknown = true;
        valued = false;
    }

    public List<Cell> unknownCells() {
        return neighbours.stream()
                .filter(Cell::isUnknown)
                .collect(toList());
    }

    public boolean hasUnknownAround() {
        return neighbours.stream()
                .anyMatch(Cell::isUnknown);
    }

    @Override
    public Cell copy() {
        Cell cell = new Cell(x, y);
        cell.neighbours = neighbours;
        cell.value = value;
        cell.valued = valued;
        cell.unknown = unknown;
        return cell;
    }

    @Override
    public String toString() {
        return String.format("%s:value=%s,unknown=%s,valued=%s,action=%s",
                super.toString(),
                value,
                unknown,
                valued,
                action);
    }

}