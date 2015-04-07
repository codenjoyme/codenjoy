package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.sudoku.services.Events;

import java.util.LinkedList;
import java.util.List;

public class Sudoku implements Tickable, Field {

    public static final int SIZE = 9;
    private List<Cell> cells;
    private Player player;

    private final int size;
    private List<Wall> walls;

    private List<Cell> acts;
    private Cell act;
    private boolean gameOver;

    public Sudoku(Level level) {
        cells = level.getCells();
        walls = level.getWalls();
        size = level.getSize();
        acts = new LinkedList<Cell>();
        gameOver = false;
    }

    @Override
    public void tick() {
        if (gameOver) return;
        if (act == null) return;

        int index = cells.indexOf(act);
        if (index == -1) throw new IllegalArgumentException("Такой координаты нет: " + act);

        Cell cell = cells.get(index);
        if (cell.isHidden()) {
            if (acts.contains(act)) {
                acts.remove(act);
            }
            acts.add(act);
            if (cell.getNumber() == act.getNumber()) {
                player.event(Events.SUCCESS);
            } else {
                player.event(Events.FAIL);
            }
        }
        act = null;

        checkIsWin();
    }

    private void checkIsWin() {
        for (Cell cell : cells) {
            if (!cell.isHidden()) continue;

            if (!acts.contains(cell)) return;

            Cell act = acts.get(acts.indexOf(cell));
            if (act.getNumber() != cell.getNumber()) {
                return;
            }
        }

        gameOver = true;
        player.event(Events.WIN);
    }

    public int getSize() {
        return size;
    }

    public void newGame(Player player) {
        this.player = player;
        this.acts.clear();
        gameOver = false;
        player.newHero(this);
    }

    public void remove(Player player) {
        this.player = null;
    }

    public List<Cell> getCells() {
        List<Cell> result = new LinkedList<Cell>();

        for (Cell cell : cells) {
            if (acts.contains(cell)) {
                result.add(acts.get(acts.indexOf(cell)));
            } else {
                result.add(cell);
            }
        }

        return result;
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
                if (gameOver) return;

                if (p.length == 1 && p[0] == 0) {
                    gameOver = true;
                    player.event(Events.LOOSE);
                    return;
                }

                if (p.length != 3) {
                    return;
                }

                if (check(p[0])) return;
                if (check(p[1])) return;
                if (check(p[2])) return;

                int x = fix(p[0]);
                int y = fix(SIZE + 1 - p[1]);
                Point pt = PointImpl.pt(x, y);

                set(pt, p[2]);
            }
        };
    }

    private boolean check(int i) {
        if (i > SIZE || i < 1) return true;
        return false;
    }

    private void set(Point pt, int n) {
        this.act = new Cell(pt, n, true);
    }

    public static int fix(int x) {
        return x + Math.abs((x - 1) / 3);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Sudoku.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Sudoku.this.walls);
                result.addAll(Sudoku.this.cells);
                result.addAll(Sudoku.this.acts);
                return result;
            }
        };
    }
}
