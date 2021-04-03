package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.minesweeper.model.Elements.*;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

public class Cell extends PointImpl {

    private Elements element;
    private boolean valued;
    private List<Cell> neighbours;
    private Action action;

    public Cell(int x, int y) {
        super(x, y);
        neighbours = new ArrayList();
    }

    public void add(Cell cell) {
        neighbours.add(cell);
    }

    public void set(Elements element) {
        valued = (element != HIDDEN);
        if (valued) {
            this.element = element;
        }
    }

    public List<Cell> unknownCells() {
        return neighbours.stream()
                .filter(not(Cell::isValued))
                .collect(toList());
    }

    public boolean hasUnknownAround() {
        return neighbours.stream()
                .anyMatch(not(Cell::isValued));
    }

    @Override
    public Cell copy() {
        Cell cell = new Cell(x, y);
        cell.neighbours = neighbours;
        cell.element = element;
        cell.valued = valued;
        return cell;
    }

    @Override
    public String toString() {
        return String.format("%s:value=%s,valued=%s,action=%s",
                super.toString(),
                element,
                valued,
                action);
    }

    public boolean isValued() {
        return valued;
    }

    public Elements element() {
        return element;
    }

    public List<Cell> neighbours() {
        return neighbours;
    }

    public void action(Action action) {
        this.action = action;
    }

    public Action action() {
        return action;
    }

}