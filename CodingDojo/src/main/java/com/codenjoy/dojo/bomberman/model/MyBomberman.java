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

    public MyBomberman(Level level) {
        super(0, 0);
        this.level = level;
        moving = false;
        alive = true;
    }

    @Override
    public void init(Board board) {
        this.board = board;
        while (isBusy(x, y)) {
            x++;
            if (isBusy(x, y)) {
                y++;
            }
        }
    }

    private boolean isBusy(int x, int y) {
        boolean busy = false;
        for (Bomberman bomberman : board.getBombermans()) {
            if (bomberman != null && bomberman.itsMe(this) && bomberman != this) {
                busy = true;
                break;
            }
        }

        busy |= this.board.getWalls().itsMe(x, y);

        return busy;
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

