package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Field {

    public int amount;
    public int width;
    public int height;
    private Cell[][] field;
    private List<Cell> cells;
    private PlayField playField;
    private List<Group> groups;
    private List<Island> islands;
    private List<Cell> toOpen;
    private List<Cell> toMark;

    public Field(PlayField field) {
        this(field.width(), field.height(), field.amount());
        this.playField = field;
        scanPlayField();
    }

    public Field(int width, int height, int amount1) {
        groups = new ArrayList();
        toOpen = new ArrayList();
        toMark = new ArrayList();
        amount = amount1;
        this.width = width;
        this.height = height;
        cells = new LinkedList();
        islands = new ArrayList();
        field = new Cell[width][height];
        createCells();
        setCellsNeighbours();
    }

    private void createCells() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                field[x][y] = new Cell(x, y);
                cells.add(field[x][y]);
            }
        }
    }

    private void setCellsNeighbours() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                Point point = pt(x, y);
                QDirection.getValues().stream()
                        .map(direction -> direction.change(point))
                        .filter(pt -> !pt.isOutOf(1, 1, width))
                        .forEach(pt -> field[point.getX()][point.getY()]
                                        .addNeighbour(field[pt.getX()][pt.getY()]));
            }
        }
    }

    private void scanPlayField() {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int value = playField.get(x, y);
                if (value == BORDER_VALUE || value == BANG_VALUE) {
                    continue;
                }

                if (value == HIDDEN_VALUE) {
                    field[x][y].setUnknown();
                } else if (value == FLAG_VALUE) {
                    field[x][y].setMine();
                } else if (value == DETECTOR_VALUE) {
                    field[x][y].setValue(DETECTOR_VALUE);
                } else {
                    field[x][y].setValue(value);
                }
            }
        }
    }

    private void setGroups() {
        groups.clear();
        for (Cell cell : cells) {
            if (cell.isValued() && cell.hasUnknownAround()) {
                groups.add(new Group(cell.getUnknownCells(), cell.getValue()));
            }
        }
    }

    private void optimizeIslands() {
        islands.forEach(island -> island.optimize());
    }

    private void divideGroupsToIslands(List<Group> groups) {
        islands.clear();
        for (Group group : groups) {
            boolean added = false;
            Island addedTo = null;

            for (int i = 0; i < islands.size(); ++i) {
                Island currentIsland = islands.get(i);
                if (currentIsland.isCross(group)) {
                    if (!added) {
                        currentIsland.add(group);
                        added = true;
                        addedTo = currentIsland;
                    } else {
                        addedTo.add(currentIsland);
                        islands.remove(i);
                    }
                }
            }

            if (!added) {
                islands.add(new Island(group));
            }
        }
    }

    public void play() {
        islands.clear();
        setGroups();
        divideGroupsToIslands(groups);
        optimizeIslands();
        determineMarkOpenIndefinite();
        filterReachableCells(toOpen);
    }

    private void filterReachableCells(List<Cell> cells) {
        for (int i = 0; i < cells.size(); ++i) {
            if (!isReachableCell(cells.get(i))) {
                cells.remove(i--);
            }
        }
    }

    private boolean isReachableCell(Cell cell) {
        if (cell.isOutOf(1, 1, width)) { // TODO с учетом границ
            return false;
        }

        return cell.neighbours().stream()
                .anyMatch(it -> !it.isUnknown()
                        && (it.getValue() == NONE_VALUE));
    }

    private void determineMarkOpenIndefinite() {
        islands.forEach(island -> {
            toOpen.addAll(island.getToOpen());
            toMark.addAll(island.getToMark());
        });
    }

    public List<Action> actions() {
        List<Action> result = new LinkedList<>();
        for (Cell cell : toMark) {
            result.add(new Action(cell, true));
        }
        for (Cell cell : toOpen) {
            result.add(new Action(cell, false));
        }
        return result;
    }
}
