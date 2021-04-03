package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cell2 {

    private boolean walkable;
    private Point point;
    private int wave;
    private List<Cell2> neighbours = new ArrayList();

    public Cell2(int x, int y) {
        point = new PointImpl(x, y);
    }

    public void addNeighbour(Cell2 cell) {
        neighbours.add(cell);
    }

    public boolean isWalkable() {
        return walkable;
    }

    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    public void initWave() {
        wave = 2147483646;
    }

    public void makeWave() {
        for (Cell2 neighbour : neighbours) {
            if (neighbour.isWalkable() && neighbour.wave + 1 > wave) {
                neighbour.wave = wave + 1;
            }
        }

        makeNeighboursWaves();
    }

    private void makeNeighboursWaves() {
        for (Cell2 neighbour : neighbours) {
            if (neighbour.isWalkable() && neighbour.wave == wave + 1) {
                neighbour.makeWave();
            }
        }

    }

    public Cell2 getPrevWaveCell() {
        return neighbours.stream()
                .filter(cell -> cell.isWalkable() && cell.wave < wave)
                .findFirst()
                .orElse(null);
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public Point getPoint() {
        return point;
    }

}
