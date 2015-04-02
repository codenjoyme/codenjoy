package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.collapse.services.CollapseEvents;
import com.codenjoy.dojo.services.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Collapse implements Tickable, Field {

    private final Dice dice;
    private Container<Point, Cell> cells;
    private Player player;

    private final int size;
    private Container<Point, Wall> walls;

    private Point act;
    private Direction direction;

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

        if (act != null && direction != null) {
            Cell cell = cells.get(act);
            if (cell == null) return;

            Point to = direction.change(act);
            Cell cellTo = cells.get(to);
            if (cellTo == null) return;

            cell.exchange(cellTo);

            checkClear(cell, cellTo);
        }

        act = null;
        direction = null;
    }

    private void fall() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = PointImpl.pt(x, y);
                if (walls.contains(pt)) continue;

                Cell cell = cells.get(pt);
                if (cell == null) {
                    Cell cell2 = null;
                    for (int y2 = y + 1; y2 < size; y2++) {
                        Point pt2 = PointImpl.pt(x, y2);
                        if (walls.contains(pt2)) break;

                        cell2 = cells.get(pt2);
                        if (cell2 != null) break;
                    }

                    if (cell2 == null) break;

                    cells.remove(cell2);
                    Cell newCell = new Cell(PointImpl.pt(x, y), cell2.getNumber());
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

            for (Direction dir : Direction.values()) {
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

            CollapseEvents success = CollapseEvents.SUCCESS;
            success.setCount(count);
            player.event(success);
        }
    }

    private void fillNew() {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point pt = PointImpl.pt(x, y);
                if (walls.contains(pt)) continue;

                Cell cell = cells.get(pt);
                if (cell == null) {
                    Cell newCell = new Cell(pt, dice.next(8) + 1);
                    cells.add(newCell);
                }
            }
        }
    }

    public int getSize() {
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

    public boolean isGameOver() {
        return gameOver;
    }

    public Collection<Wall> getWalls() {
        return walls.values();
    }

    @Override
    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                direction = Direction.DOWN;
            }

            @Override
            public void up() {
                direction = Direction.UP;
            }

            @Override
            public void left() {
                direction = Direction.LEFT;
            }

            @Override
            public void right() {
                direction = Direction.RIGHT;
            }

            @Override
            public void act(int... p) {
                if (gameOver) return;

                if (p.length != 2) {
                    return;
                }

                if (check(p[0])) return;
                if (check(p[1])) return;

                int x = p[0];
                int y = p[1];
                act = PointImpl.pt(x, y);
            }
        };
    }

    private boolean check(int i) {
        return (i >= size || i < 0);
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
                List<Point> result = new LinkedList<Point>();
                result.addAll(Collapse.this.walls.values());
                result.addAll(Collapse.this.cells.values());
                return result;
            }
        };
    }
}
