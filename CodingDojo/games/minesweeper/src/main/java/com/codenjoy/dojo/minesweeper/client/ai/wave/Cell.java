//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.wave;

import com.codenjoy.dojo.minesweeper.client.ai.utils.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cell {
    private boolean walkable;
    private Point point;
    private int wave;
    private List<Cell> neighbours = new ArrayList();

    public Cell(int x, int y) {
        this.point = new Point(x, y);
    }

    public void addNeighbour(Cell cell) {
        this.neighbours.add(cell);
    }

    public void setWave(int wave) {
        this.wave = wave;
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

        while(i$.hasNext()) {
            Cell neighbour = (Cell)i$.next();
            if (neighbour.isWalkable() && neighbour.wave + 1 > this.wave) {
                neighbour.wave = this.wave + 1;
            }
        }

        this.makeNeighboursWaves();
    }

    private void makeNeighboursWaves() {
        Iterator i$ = this.neighbours.iterator();

        while(i$.hasNext()) {
            Cell neighbour = (Cell)i$.next();
            if (neighbour.isWalkable() && neighbour.wave == this.wave + 1) {
                neighbour.makeWave();
            }
        }

    }

    public Cell getPrevWaveCell() {
        Iterator i$ = this.neighbours.iterator();

        Cell neighbour;
        do {
            if (!i$.hasNext()) {
                return null;
            }

            neighbour = (Cell)i$.next();
        } while(!neighbour.isWalkable() || neighbour.wave >= this.wave);

        return neighbour;
    }

    public int getWave() {
        return this.wave;
    }

    public Point getPoint() {
        return this.point;
    }

    public String toString() {
        return this.walkable ? "." : "Ж";
    }
}
