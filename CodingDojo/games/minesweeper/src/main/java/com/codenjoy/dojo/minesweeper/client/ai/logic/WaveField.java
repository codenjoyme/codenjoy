package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

public class WaveField {

    private int size;
    private Cell2[][] field;
    private List<Cell2> cells = new ArrayList();
    private Board board;

    public WaveField(Board board) {
        this.board = board;
        size = board.size();
        createCell2s();
        setCellsNeighbours();
        initData();
    }

    public List<Direction> findWay(Point to) {
        Point from = board.getMe();
        Cell2 cell = getCell2(from);
        cell.setWave(0);
        cell.makeWave();
        List<Cell2> way = getCell2sWay(to);
        List<Direction> result = getDirectionsWay(from, way);
        return result;
    }

    private List<Direction> getDirectionsWay(Point from, List<Cell2> way) {
        List<Direction> result = new ArrayList();

        for (int i = way.size() - 2; i >= 0; --i) {
            Point coord = (way.get(i)).getPoint();
            result.add(getDirection(from, coord));
            from = coord;
        }

        return result;
    }

    private List<Cell2> getCell2sWay(Point to) {
        List<Cell2> way = new ArrayList();
        Cell2 target = getCell2(to);

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
        return field[point.getX()][point.getY()];
    }

    private void initData() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                field[i][j].setWalkable(!board.isAt(i, j, Elements.HIDDEN, Elements.BORDER));
            }
        }
    }

    private void createCell2s() {
        field = new Cell2[size][size];

        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                Cell2 cell = new Cell2(x, y);
                cell.initWave();
                field[x][y] = cell;
                cells.add(cell);
            }
        }
    }

    private void setCellsNeighbours() {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                if (x > 0) {
                    field[x][y].addNeighbour(field[x - 1][y]);
                }

                if (y > 0) {
                    field[x][y].addNeighbour(field[x][y - 1]);
                }

                if (x < size - 1) {
                    field[x][y].addNeighbour(field[x + 1][y]);
                }

                if (y < size - 1) {
                    field[x][y].addNeighbour(field[x][y + 1]);
                }
            }
        }
    }
}
