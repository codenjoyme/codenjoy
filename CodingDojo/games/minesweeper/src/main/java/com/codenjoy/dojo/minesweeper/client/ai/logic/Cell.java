package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cell extends PointImpl {

    private int value;
    private boolean valued = false;
    private boolean mine = false;
    private boolean unknown = true;
    private double possibility = -1.0D;
    private final List<Cell> neighbours = new ArrayList();

    public Cell(int x, int y) {
        super(x, y);
    }

    public void addNeighbour(Cell cell) {
        neighbours.add(cell);
    }

    public double getPossibility() {
        return possibility;
    }

    public void setPossibility(double possibility) {
        this.possibility = possibility;
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
        mine = false;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine() {
        mine = true;
        unknown = false;
        valued = false;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public void setValued() {
        unknown = false;
        mine = false;
        valued = true;
    }

    public void setUnknown() {
        unknown = true;
        mine = false;
        valued = false;
    }

    public ArrayList<Cell> getUnknownCells() {
        ArrayList<Cell> cells = new ArrayList();
        Iterator i$ = neighbours.iterator();

        while (i$.hasNext()) {
            Cell neighbour = (Cell) i$.next();
            if (neighbour.isUnknown()) {
                cells.add(neighbour);
            }
        }

        return cells;
    }

    public String toString() {
        String string = super.toString() + "="
                + (mine ? "mine" : (unknown ? "unknown, " + getPossibility() + "%" : value));
        return string;
    }

    public boolean hasUnknownAround() {
        Iterator iterator = neighbours.iterator();

        Cell cell;
        do {
            if (!iterator.hasNext()) {
                return false;
            }
            cell = (Cell) iterator.next();
        } while (!cell.isUnknown());

        return true;
    }
}
