package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

public class WaveField {

    private final int size;
    private Cell2[][] field;
    private final List<Cell2> cells = new ArrayList();
    private final BoardImpl board;

    public WaveField(BoardImpl board) {
        this.board = board;
        this.size = board.size();
        this.createCell2s();
        this.setCellsNeighbours();
        this.initData();
        this.print();
    }

    public List<Direction> findWay(Point to) {
        Point from = this.board.getMe();
        Cell2 cell = this.getCell2(from);
        cell.setWave(0);
        cell.makeWave();
        this.printWaves();
        List<Cell2> way = this.getCell2sWay(to);
        List<Direction> result = this.getDirectionsWay(from, way);
        return result;
    }

    private List<Direction> getDirectionsWay(Point from, List<Cell2> way) {
        List<Direction> result = new ArrayList();

        for (int i = way.size() - 2; i >= 0; --i) {
            Point coord = (way.get(i)).getPoint();
            result.add(this.getDirection(from, coord));
            from = coord;
        }

        return result;
    }

    private List<Cell2> getCell2sWay(Point to) {
        List<Cell2> way = new ArrayList();
        Cell2 target = this.getCell2(to);

        do {
            way.add(target);
            target = target.getPrevWaveCell();
        } while (target != null);

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

    private Cell2 getCell2(Point point) {
        return this.field[point.getX()][point.getY()];
    }

    private void initData() {
        for (int i = 0; i < this.size; ++i) {
            for (int j = 0; j < this.size; ++j) {
                this.field[i][j].setWalkable(!this.board.isAt(i, j, Elements.HIDDEN, Elements.BORDER));
            }
        }

    }

    private void createCell2s() {
        this.field = new Cell2[this.size][this.size];

        for (int x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
                Cell2 cell = new Cell2(x, y);
                cell.initWave();
                this.field[x][y] = cell;
                this.cells.add(cell);
            }
        }
    }

    private void setCellsNeighbours() {
        for (int x = 0; x < this.size; ++x) {
            for (int y = 0; y < this.size; ++y) {
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

        for (int i = 0; i < this.size; ++i) {
            for (int j = 0; j < this.size; ++j) {
                sb.append(this.field[i][j]).append(' ');
            }

            sb.append(' ').append(i).append('\n');
        }

        sb.append("\n0 1 2 3 4 5 6 7 8 9 0 1 2 3");
        return sb.toString();
    }

    public String toStringWaves() {
        StringBuilder sb = new StringBuilder(" 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15\n\n");

        for (int i = 0; i < this.size; ++i) {
            for (int j = 0; j < this.size; ++j) {
                int wave = this.field[i][j].getWave();
                sb.append(wave < 10 ? ' ' : "").append(wave == 2147483646 ? "  " : wave).append(' ');
            }

            sb.append(' ').append(i).append('\n');
        }

        sb.append("\n 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15");
        return sb.toString();
    }
}
