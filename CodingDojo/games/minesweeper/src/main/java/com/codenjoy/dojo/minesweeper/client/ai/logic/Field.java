package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.minesweeper.client.ai.AISolver.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class Field {

    public int size;
    private Cell[][] field;
    private List<Cell> cells;
    private List<Group> groups;

    public Field(int size) {
        groups = new ArrayList();
        this.size = size;
        cells = new LinkedList();
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
                    field[x][y].value(DETECTOR_VALUE);
                } else {
                    field[x][y].value(value);
                }
            }
        }
        setGroups();
    }

    private void setGroups() {
        for (Cell cell : cells) {
            if (cell.isValued() && cell.hasUnknownAround()) {
                groups.add(new Group(cell.unknownCells(), cell.value()));
            }
        }
    }

    private boolean isReachableCell(Cell cell) {
        if (cell.isOutOf(1, 1, size)) { // TODO с учетом границ
            return false;
        }

        return cell.neighbours().stream()
                .anyMatch(it -> !it.isUnknown()
                        && (it.value() == NONE_VALUE));
    }

    public List<Action> actions() {
        List<Action> result = new LinkedList<>();
        Set<Cell> cells = new HashSet<>();
        groups.stream()
                .flatMap(group -> group.actions().stream())
                .filter(action -> action.willMark() || isReachableCell(action.cell()))
                .sorted((action1, action2) -> Boolean.compare(!action1.willMark(), !action2.willMark()))
                .forEach(action -> {
                    if (!cells.contains(action.cell())) {
                        cells.add(action.cell());
                        result.add(action);
                    }
                });
        return result;
    }
}
