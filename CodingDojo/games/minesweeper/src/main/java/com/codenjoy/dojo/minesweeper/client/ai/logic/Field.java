package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Field {

    public int amount;
    public int size;
    private Cell[][] field;
    private List<Cell> cells;
    private List<Group> groups;
    private List<Island> islands;
    private List<Cell> toOpen;
    private List<Cell> toMark;

    public Field(int size) {
        groups = new ArrayList();
        toOpen = new ArrayList();
        toMark = new ArrayList();
        this.size = size;
        cells = new LinkedList();
        islands = new ArrayList();
        this.field = new Cell[size][size];
        createCells();
        setCellsNeighbours();
    }

    private void createCells() {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                field[x][y] = new Cell(x, y);
                cells.add(field[x][y]);
            }
        }
    }

    private void setCellsNeighbours() {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                Point point = pt(x, y);
                QDirection.getValues().stream()
                        .map(direction -> direction.change(point))
                        .filter(pt -> !pt.isOutOf(1, 1, size))
                        .forEach(pt -> field[point.getX()][point.getY()]
                                        .addNeighbour(field[pt.getX()][pt.getY()]));
            }
        }
    }

    public void scan(Function<Point, Integer> get) {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                int value = get.apply(pt(x, y));
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
        amount = getAmount();
    }

    private int getAmount() {
        int result = 0;
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                if (field[x][y].isMine()) {
                    ++result;
                }
            }
        }

        return result;
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
        if (cell.isOutOf(1, 1, size)) { // TODO с учетом границ
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
