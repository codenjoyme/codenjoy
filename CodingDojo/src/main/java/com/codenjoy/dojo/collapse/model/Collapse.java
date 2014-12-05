package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.collapse.services.CollapseEvents;
import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

public class Collapse implements Tickable, Field {

    private List<Cell> cells;
    private Player player;

    private final int size;
    private List<Wall> walls;

    private Point act;
    private Direction direction;

    private boolean gameOver;

    public Collapse(Level level) {
        cells = level.getCells();
        walls = level.getWalls();
        size = level.getSize();
        gameOver = false;
    }

    @Override
    public void tick() {
        if (gameOver) return;
        if (act == null || direction == null) return;

        int index = cells.indexOf(act);
        if (index == -1) throw new IllegalArgumentException("Такой координаты нет: " + act);
        Cell cell = cells.get(index);

        Point to = direction.change(act);
        int indexTo = cells.indexOf(to);
        if (indexTo == -1) throw new IllegalArgumentException("Такой координаты нет: " + to);
        Cell cellTo = cells.get(indexTo);

        cell.exchange(cellTo);

        checkClear(cellTo, direction);
        fillNew();

        act = null;
        direction = null;
    }

    private void checkClear(Cell cell, Direction direction) {
        // TODO implement me
        // player.event(CollapseEvents.SUCCESS);
    }

    private void fillNew() {
        // TODO implement me
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

    public List<Cell> getCells() {
        return cells;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<Wall> getWalls() {
        return walls;
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

                if (p.length == 1 && p[0] == 0) {
                    gameOver = true;
                    player.event(CollapseEvents.NEW_GAME);
                    return;
                }

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
                result.addAll(Collapse.this.walls);
                result.addAll(Collapse.this.cells);
                return result;
            }
        };
    }
}
