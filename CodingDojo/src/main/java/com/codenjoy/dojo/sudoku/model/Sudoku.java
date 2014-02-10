package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.*;

import java.util.List;

public class Sudoku implements Tickable, Field {

    private List<Cell> cells;
    private Player player;

    private final int size;
    private List<Point> walls;

    private Cell act;

    public Sudoku(Level level) {
        cells = level.getCells();
        walls = level.getWalls();
        size = level.getSize();
    }

    @Override
    public void tick() {
        if (act != null) {
            cells.add(act);
        }
        act = null;
    }

    public int getSize() {
        return size;
    }

    public void newGame(Player player) {
        this.player = player;
        player.newHero(this);
    }

    public void remove(Player player) {
        this.player = null;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public boolean isGameOver() {
        return false; // TODO
    }

    public List<Point> getWalls() {
        return walls;
    }

    @Override
    public Joystick getJoystick() {
        return new Joystick() {
            @Override
            public void down() {
                // do nothing
            }

            @Override
            public void up() {
                // do nothing
            }

            @Override
            public void left() {
                // do nothing
            }

            @Override
            public void right() {
                // do nothing
            }

            @Override
            public void act(int... p) {
                if (p.length != 3) {
                    return;
                }

                int x = fix(p[0]);
                int y = fix(p[1]);
                Point pt = PointImpl.pt(x, y);

                set(pt, p[2]);
            }
        };
    }

    private void set(Point pt, int n) {
        this.act = new Cell(pt, n, true);
    }

    private int fix(int x) {
        return x + Math.abs(x / 3);
    }
}
