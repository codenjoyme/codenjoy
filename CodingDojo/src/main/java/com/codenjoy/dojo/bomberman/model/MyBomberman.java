package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman extends PointImpl implements Bomberman {
    private static final boolean WITHOUT_MEAT_CHOPPER = false;
    private Level level;
    private Dice dice;
    private IBoard board;
    private boolean alive;
    private boolean bomb;
    private Direction direction;

    public MyBomberman(Level level, Dice dice) {
        super(-1, -1);
        this.level = level;
        this.dice = dice;
        alive = true;
        direction = null;
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
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;

        if (direction != null) {
            bomb = true;
        } else {
            setBomb(x, y);
        }
    }

    @Override
    public void apply() {
        if (!alive) return;

        if (direction == null) {
            return;
        }

        int newX = direction.changeX(x);
        int newY = direction.changeY(y);

        if (!board.isBarrier(newX, newY, WITHOUT_MEAT_CHOPPER)) {
            move(newX, newY);
        }
        direction = null;

        if (bomb) {
            setBomb(x, y);
            bomb = false;
        }
    }

    private void setBomb(int bombX, int bombY) {
        if (board.getBombs(this).size() < level.bombsCount()) {
            board.drop(new Bomb(this, bombX, bombY, level.bombsPower(), board));
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

