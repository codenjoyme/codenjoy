//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.wave;

import com.codenjoy.dojo.minesweeper.client.ai.utils.BoardImpl;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WaveField {
    private int size;
    private Cell[][] field;
    private List<Cell> cells = new ArrayList();
    private BoardImpl board;

    public WaveField(BoardImpl board) {
        this.board = board;
        this.size = board.size();
        this.createCells();
        this.setCellsNeighbours();
        this.initData();
        this.print();
    }

    public List<Direction> findWay(Point to) {
        Point from = this.board.getMe();
        Cell cell = this.getCell(from);
        cell.setWave(0);
        cell.makeWave();
        this.printWaves();
        List<Cell> way = this.getCellsWay(to);
        List<Direction> result = this.getDirectionsWay(from, way);
        return result;
    }

    private List<Direction> getDirectionsWay(Point from, List<Cell> way) {
        List<Direction> result = new ArrayList();

        for(int i = way.size() - 2; i >= 0; --i) {
            Point coord = ((Cell)way.get(i)).getPoint();
            result.add(this.getDirection(from, coord));
            from = coord;
        }

        return result;
    }

    private List<Cell> getCellsWay(Point to) {
        List<Cell> way = new ArrayList();
        Cell target = this.getCell(to);

        do {
            way.add(target);
            target = target.getPrevWaveCell();
        } while(target != null);

        return way;
    }

    private Direction getDirection(Point from, Point to) {
        if (from.getX() == to.getX()) {
            if (to.getY() < from.getY()) {
                return Direction.UP;
            }

            if (to.getY() > from.getY()) {
                return Direction.DOWN;
            }
        }

        if (from.getY() == to.getY()) {
            if (to.getX() < from.getX()) {
                return Direction.LEFT;
            }

            if (to.getX() > from.getX()) {
                return Direction.RIGHT;
            }
        }

        return null;
    }

    private Cell getCell(Point point) {
        return this.field[point.getX()][point.getY()];
    }

    private void initData() {
        for(int i = 0; i < this.size; ++i) {
            for(int j = 0; j < this.size; ++j) {
                this.field[i][j].setWalkable(!this.board.isAt(i, j, new Elements[]{Elements.HIDDEN, Elements.BORDER}));
            }
        }

    }

    private int getMaxValue(Map<Integer, Integer> map) {
        int maxKey = -1;
        int maxValue = -1;
        Iterator i$ = map.entrySet().iterator();

        while(i$.hasNext()) {
            Entry<Integer, Integer> entry = (Entry)i$.next();
            if ((Integer)entry.getValue() > maxValue) {
                maxValue = (Integer)entry.getValue();
                maxKey = (Integer)entry.getKey();
            }
        }

        return maxKey;
    }

    private void createCells() {
        this.field = new Cell[this.size][this.size];

        for(int x = 0; x < this.size; ++x) {
            for(int y = 0; y < this.size; ++y) {
                Cell cell = new Cell(x, y);
                cell.initWave();
                this.field[x][y] = cell;
                this.cells.add(cell);
            }
        }

    }

    private void setCellsNeighbours() {
        for(int x = 0; x < this.size; ++x) {
            for(int y = 0; y < this.size; ++y) {
                if (x > 0) {
                    this.field[x][y].addNeighbour(this.field[x - 1][y]);
                }

                if (y > 0) {
                    this.field[x][y].addNeighbour(this.field[x][y - 1]);
                }

                if (x < this.size - 1) {
                    this.field[x][y].addNeighbour(this.field[x + 1][y]);
                }

                if (y < this.size - 1) {
                    this.field[x][y].addNeighbour(this.field[x][y + 1]);
                }
            }
        }

    }

    public void print() {
        System.out.println(this.toString());
    }

    public void printWaves() {
        System.out.println(this.toStringWaves());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("0 1 2 3 4 5 6 7 8 9 0 1 2 3\n\n");

        for(int i = 0; i < this.size; ++i) {
            for(int j = 0; j < this.size; ++j) {
                sb.append(this.field[i][j]).append(' ');
            }

            sb.append(' ').append(i).append('\n');
        }

        sb.append("\n0 1 2 3 4 5 6 7 8 9 0 1 2 3");
        return sb.toString();
    }

    public String toStringWaves() {
        StringBuilder sb = new StringBuilder(" 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15\n\n");

        for(int i = 0; i < this.size; ++i) {
            for(int j = 0; j < this.size; ++j) {
                int wave = this.field[i][j].getWave();
                sb.append(wave < 10 ? ' ' : "").append(wave == 2147483646 ? "  " : wave).append(' ');
            }

            sb.append(' ').append(i).append('\n');
        }

        sb.append("\n 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15");
        return sb.toString();
    }
}
