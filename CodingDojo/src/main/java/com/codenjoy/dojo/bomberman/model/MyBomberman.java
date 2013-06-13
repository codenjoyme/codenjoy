package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman extends PointImpl implements Bomberman {
    private int newX;
    private int newY;
    private boolean moving;
    private Level level;
    private Dice dice;
    private Board board;
    private boolean alive;
    private boolean bomb;

    public MyBomberman(Level level, Dice dice) {
        super(-1, -1);
        this.level = level;
        this.dice = dice;
        moving = false;
        alive = true;
    }

    @Override
    public void init(Board board) {
        this.board = board;
        int count = 0;
        do {
            x = dice.next(board.size());
            y = dice.next(board.size());
            while (isBusy(x, y) && !isOutOfBoard(x, y)) {
                x++;
                if (isBusy(x, y)) {
                    y++;
                }
            }
        } while ((isBusy(x, y) || isOutOfBoard(x, y)) && count++ < 1000);

        if (count >= 1000) {
            throw new  RuntimeException("Dead loop at MyBomberman.init(Board)!");
        }
    }

    private boolean isBusy(int x, int y) {
        for (Bomberman bomberman : board.getBombermans()) {
            if (bomberman != null && bomberman.itsMe(this) && bomberman != this) {
                return true;
            }
        }

        return this.board.getWalls().itsMe(x, y);
    }

    private boolean isOutOfBoard(int x, int y) {
        return x >= board.size() || y >= board.size() || x < 0 || y < 0;
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
    public void act() {
        checkAlive();
        if (moving) {
            bomb = true;
        } else {
            setBomb(x, y);
        }
    }

    @Override
    public void apply() {
        if (!moving) {
            return;
        }
        moving = false;

        if (board.isBarrier(newX, newY)) {
            newX = x;
            newY = y;
        }

        if (bomb) {
            setBomb(newX, newY);
            bomb = false;
        }

        x = newX;
        y = newY;

        correct();
    }

    private void correct() {
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
        if (board.getBombs(this).size() < level.bombsCount()) {
            board.drop(new BombCopier(this, bombX, bombY, level.bombsPower()));
        }
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void kill() {
        alive = false;
    }
}

