package com.codenjoy.bomberman;

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
    private int boardSize;
    private boolean moving;

    public MyBomberman(int boardSize) {
        this.boardSize = boardSize;
        moving = false;
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
        if (!moving) {
            moving = true;
            newX = x + 1;
        }
    }

    @Override
    public void down() {
        if (!moving) {
            moving = true;
            newY = y + 1;
        }
    }

    @Override
    public void up() {
        if (!moving) {
            moving = true;
            newY = y - 1;
        }
    }

    @Override
    public void left() {
        if (!moving) {
            moving = true;
            newX = x - 1;
        }
    }

    @Override
    public void bomb() {
        ///board.drop(new Bomb());
    }

    public void apply() {
        x = newX;
        y = newY;
        if (x >= boardSize) {
            x = boardSize - 1;
        }
        if (y >= boardSize) {
            y = boardSize - 1;
        }
        if (y < 0) {
            y = 0;
        }
        if (x < 0) {
            x = 0;
        }
        moving = false;
    }
}

