package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;

import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.minesweeper.client.ai.logic.Action.*;
import static com.codenjoy.dojo.minesweeper.model.Elements.*;
import static java.util.stream.Collectors.toCollection;

public class Field {

    private int size;
    private List<Cell> cells;
    private List<Group> groups;

    public Field(int size) {
        this.size = size;
        groups = new ArrayList();
        cells = new LinkedList();
        createCells();
    }

    private void createCells() {
        for (int x = 0; x < size; ++x) {
            for (int y = 0; y < size; ++y) {
                cells.add(new Cell(x, y));
            }
        }
    }

    private void setCellsNeighbours() {
        for (Cell cell : cells) {
            QDirection.getValues().stream()
                    .map(direction -> direction.change(cell))
                    .filter(pt -> !pt.isOutOf(size))
                    .map(pt -> cell(pt))
                    .filter(neighbour -> neighbour.element() != BORDER)
                    .forEach(neighbour -> cell.add(neighbour));
        }
    }

    private Cell cell(Point point) {
        return cells.get(cells.indexOf(point));
    }

    public void scan(Function<Point, Elements> get) {
        for (Cell cell : cells) {
            cell.set(get.apply(cell));
        }
        setCellsNeighbours();
        setGroups();
    }

    private void setGroups() {
        for (Cell cell : cells) {
            if (cell.isValued() && cell.hasUnknownAround()) {
                groups.add(new Group(cell.unknownCells(), cell.element()));
            }
        }
    }

    private boolean isReachable(Cell cell) {
        return cell.neighbours().stream()
                .anyMatch(it -> it.isValued() && (it.element() == NONE));
    }

    public Collection<Cell> actions() {
        return groups.stream()
                // все группы клеток разбиваем в плоскую коллекцию
                .flatMap(group -> group.list().stream())
                // пропускаем клеточки в отношении которых ничего не поделать
                .filter(cell -> cell.action() != NOTHING)
                // активные действия MARK совершаются в направлении '*' а значит туда мы не зайдем
                // а вот GO надо бы проверить на доступность клеточки
                .filter(cell -> cell.action() == MARK || isReachable(cell))
                // сперва нас интересуют активные действия в устранении мин
                .sorted((cell1, cell2) -> Boolean.compare(cell1.action() != MARK, cell2.action() != MARK))
                // мы исключаем все дубликаты
                .collect(toCollection(LinkedHashSet::new));
    }
}
