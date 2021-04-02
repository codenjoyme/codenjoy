package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.minesweeper.model.Elements.BORDER;
import static com.codenjoy.dojo.minesweeper.model.Elements.HIDDEN;
import static com.codenjoy.dojo.services.Direction.*;

public class WaveField {

    private int size;
    private Cell2[][] field;
    private List<Cell2> cells = new ArrayList();
    private Board board;

    public WaveField(Board board) {
        this.board = board;
        size = board.size();
        createCells();
        setCellsNeighbours();
        initData();
    }

    public List<Direction> findWay(Point to) {
        Point from = board.getMe();
        Cell2 cell = getCell(from);
        cell.setWave(0);
        cell.makeWave();
        List<Cell2> way = getCellsWay(to);
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

    private List<Cell2> getCellsWay(Point to) {
        List<Cell2> way = new ArrayList();
        Cell2 target = getCell(to);

        do {
            way.add(target);
            target = target.getPrevWaveCell();
        } while (target != null);

        return way;
    }

    private Direction getDirection(Point from, Point to) {
        if (from.getX() == to.getX()) {
            if (to.getY() < from.getY()) {
                return UP;
            }

            if (to.getY() > from.getY()) {
                return DOWN;
            }
        }

        if (from.getY() == to.getY()) {
            if (to.getX() < from.getX()) {
                return LEFT;
            }

            if (to.getX() > from.getX()) {
                return RIGHT;
            }
        }

        return null;
    }

    private Cell2 getCell(Point point) {
        return field[point.getX()][point.getY()];
    }

    private void initData() {
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                field[i][j].setWalkable(!board.isAt(i, j, HIDDEN, BORDER));
            }
        }
    }

    private void createCells() {
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
