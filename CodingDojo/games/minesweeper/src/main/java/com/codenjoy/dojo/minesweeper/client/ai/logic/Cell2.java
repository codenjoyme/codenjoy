package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cell2 {

    private boolean walkable;
    private final Point point;
    private int wave;
    private final List<Cell2> neighbours = new ArrayList();

    public Cell2(int x, int y) {
        this.point = new PointImpl(x, y);
    }

    public void addNeighbour(Cell2 cell) {
        this.neighbours.add(cell);
    }

    public boolean isWalkable() {
        return this.walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void initWave() {
        this.wave = 2147483646;
    }

    public void makeWave() {
        Iterator i$ = this.neighbours.iterator();

        while (i$.hasNext()) {
            Cell2 neighbour = (Cell2) i$.next();
            if (neighbour.isWalkable() && neighbour.wave + 1 > this.wave) {
                neighbour.wave = this.wave + 1;
            }
        }

        this.makeNeighboursWaves();
    }

    private void makeNeighboursWaves() {
        Iterator i$ = this.neighbours.iterator();

        while (i$.hasNext()) {
            Cell2 neighbour = (Cell2) i$.next();
            if (neighbour.isWalkable() && neighbour.wave == this.wave + 1) {
                neighbour.makeWave();
            }
        }

    }

    public Cell2 getPrevWaveCell() {
        Iterator i$ = this.neighbours.iterator();

        Cell2 neighbour;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            neighbour = (Cell2) i$.next();
        } while (!neighbour.isWalkable() || neighbour.wave >= this.wave);

        return neighbour;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public Point getPoint() {
        return this.point;
    }

    public String toString() {
        return this.walkable ? "." : "Ж";
    }
}
