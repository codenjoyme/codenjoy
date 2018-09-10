package com.codenjoy.dojo.collapse.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.collapse.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.Collection;
import java.util.LinkedList;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Collapse implements Field {

    private final Dice dice;
    private Container<Point, Cell> cells;
    private Player player;

    private final int size;
    private Container<Point, Wall> walls;

    private boolean gameOver;

    public Collapse(Level level, Dice dice) {
        this.dice = dice;
        cells = new Container(level.getCells());
        walls = new Container(level.getWalls());
        size = level.getSize();
        gameOver = false;
    }

    @Override
    public void tick() {
        if (gameOver) return;
        fall();
        fillNew();

        Hero hero = hero();
        Point act = hero.getAct();
        Direction direction = hero.getDirection();
        if (hero.getAct() != null && direction != null) {
            Cell cell = cells.get(act);
            if (cell == null) return;

            Point to = direction.change(act);
            Cell cellTo = cells.get(to);
            if (cellTo == null) return;

            cell.exchange(cellTo);

            checkClear(cell, cellTo);
        }

        hero.tick();
    }

    private Hero hero() {
        return player.getHero();
    }

    private void fall() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = pt(x, y);
                if (walls.contains(pt)) continue;

                Cell cell = cells.get(pt);
                if (cell == null) {
                    Cell cell2 = null;
                    for (int y2 = y + 1; y2 < size; y2++) {
                        Point pt2 = pt(x, y2);
                        if (walls.contains(pt2)) break;

                        cell2 = cells.get(pt2);
                        if (cell2 != null) break;
                    }

                    if (cell2 == null) break;

                    cells.remove(cell2);
                    Cell newCell = new Cell(pt(x, y), cell2.getNumber());
                    cells.add(newCell);
                }
            }
        }
    }

    private void checkClear(Cell cell1, Cell cell2) {
        Container<Point, Cell> toCheck = new Container();
        Container<Point, Cell> forRemove = new Container();
        toCheck.add(cell1);
        toCheck.add(cell2);

        while (!toCheck.isEmpty()) {
            Cell current = toCheck.removeLast();

            for (Direction dir : Direction.getValues()) {
                Point pt = dir.change(current);
                Cell next = cells.get(pt);
                if (next == null) continue;

                if (current.getNumber() == next.getNumber()) {
                    if (!forRemove.contains(next)) {
                        toCheck.add(next);
                    }
                    forRemove.add(current);
                    forRemove.add(next);
                }
            }
        }

        if (!forRemove.isEmpty()) {
            int count = forRemove.size();

            for (Cell remove : forRemove) {
                cells.remove(remove);
            }

            Events success = Events.SUCCESS;
            success.setCount(count);
            player.event(success);
        }
    }

    private void fillNew() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = pt(x, y);
                if (walls.contains(pt)) continue;

                Cell cell = cells.get(pt);
                if (cell == null) {
                    Cell newCell = new Cell(pt, dice.next(8) + 1);
                    cells.add(newCell);
                }
            }
        }
    }

    @Override
    public int size() {
        return size;
    }

    public void newGame(Player player) {
        this.player = player;
        gameOver = false;
        player.newHero(this);
    }

    public void remove(Player player) {
        this.player = null;
    }

    public Collection<Cell> getCells() {
        return cells.values();
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    public Collection<Wall> getWalls() {
        return walls.values();
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Collapse.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>(){{
                    addAll(Collapse.this.walls.values());
                    addAll(Collapse.this.cells.values());
                }};
            }
        };
    }
}
