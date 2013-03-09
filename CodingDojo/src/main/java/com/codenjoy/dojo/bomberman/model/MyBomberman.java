package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman extends Point implements Bomberman {
    private int newX;
    private int newY;
    private boolean moving;
    private Level level;
    private Board board;
    private boolean alive;
    private boolean bomb;

    public MyBomberman(Level level, Board board) {
        super(0, 0);
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
    public void right() {
        checkAlive();
        if (!moving) {
            moving = true;
            newX = x + 1;
            newY = y;
        }
    }

    @Override
    public void down() {
        checkAlive();
        if (!moving) {
            moving = true;
            newX = x;
            newY = y + 1;
        }
    }

    @Override
    public void up() {
        checkAlive();
        if (!moving) {
            moving = true;
            newX = x;
            newY = y - 1;
        }
    }

    @Override
    public void left() {
        checkAlive();
        if (!moving) {
            moving = true;
            newX = x - 1;
            newY = y;
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
        if (moving) {
            bomb = true;
        } else {
            setBomb(x, y);
        }
    }

    public void apply() {
        moving = false;

        if (board.isBarrier(newX, newY)) {
            return;
        }

        if (bomb) {
            setBomb(newX, newY);
            bomb = false;
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

    private void setBomb(int bombX, int bombY) {
        if (board.getBombs().size() < level.bombsCount()) {
            board.drop(new BombCopier(bombX, bombY, level.bombsPower()));
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }
}

