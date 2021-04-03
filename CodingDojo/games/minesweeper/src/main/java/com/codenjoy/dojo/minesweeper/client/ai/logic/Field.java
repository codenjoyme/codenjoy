package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toCollection;

public class Field {

    public int size;
    private List<Cell> cells;
    private List<Group> groups;

    public Field(int size) {
        groups = new ArrayList();
        this.size = size;
        cells = new LinkedList();
        createCells();
        setCellsNeighbours();
    }

    private void createCells() {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                cells.add(new Cell(x, y));
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
                        .forEach(pt -> cell(point).addNeighbour(cell(pt)));
            }
        }
    }

    private Cell cell(Point point) {
        return cells.get(cells.indexOf(point));
    }

    public void scan(Function<Point, Value> get) {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                Point pt = pt(x, y);
                Value value = get.apply(pt);
                if (value == Value.BORDER || value == Value.BANG) {
                    continue;
                }

                cell(pt).set(value);
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
                        && (it.value() == Value.NONE));
    }

    public Collection<Cell> actions() {
        return groups.stream()
                // все группы клеток разбиваем в плоскую коллекцию
                .flatMap(group -> group.list().stream())
                // пропускаем клеточки в отношении которых ничего не поделать
                .filter(cell -> cell.action() != Action.NOTHING)
                // активные действия MARK совершаются в направлении '*' а значит туда мы не зайдем
                // а вот GO надо бы проверить на доступность клеточки
                .filter(cell -> cell.action() == Action.MARK || isReachableCell(cell))
                // сперва нас интересуют активные действия в устранении мин
                .sorted((cell1, cell2) -> Boolean.compare(cell1.action() != Action.MARK, cell2.action() != Action.MARK))
                // мы исключаем все дубликаты
                .collect(toCollection(LinkedHashSet::new));
    }
}
