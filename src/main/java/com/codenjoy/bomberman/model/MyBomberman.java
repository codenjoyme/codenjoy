package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman implements Bomberman {
    private int x;
    private int y;
    private int newX;
    private int newY;
    private boolean moving;
    private Level level;
    private Board board;
    private boolean alive;

    public MyBomberman(Level level, Board board) {
        this.level = level;
        this.board = board;
        moving = false;
        alive = true;
        initPosition();
    }

    private void initPosition() {
        while (board.getWalls().itsMe(x, y)) {
            x++;
            if (board.getWalls().itsMe(x, y)) {
                y++;
            }
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void right() {
        checkAlive();
        if (!moving) {
            moving = true;
            newX = x + 1;
        }
    }

    @Override
    public void down() {
        checkAlive();
        if (!moving) {
            moving = true;
            newY = y + 1;
        }
    }

    @Override
    public void up() {
        checkAlive();
        if (!moving) {
            moving = true;
            newY = y - 1;
        }
    }

    @Override
    public void left() {
        checkAlive();
        if (!moving) {
            moving = true;
            newX = x - 1;
        }
    }

    private void checkAlive() {
        if (!alive) {
            throw new IllegalStateException("Your bomberman is dead!");
        }
    }

    @Override
    public void bomb() {
        checkAlive();
        if (board.getBombs().size() < level.bombsCount()) {
            board.drop(new Bomb(x, y));
        }
    }

    public void apply() {
        moving = false;

        if (board.getWalls().itsMe(newX, newY)) {
            return;
        }

        x = newX;
        y = newY;
        if (x >= board.size()) {
            x = board.size() - 1;
        }
        if (y >= board.size()) {
            y = board.size() - 1;
        }
        if (y < 0) {
            y = 0;
        }
        if (x < 0) {
            x = 0;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }
}

