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

    public MyBomberman(int boardSize) {
        this.boardSize = boardSize;
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
        newX = x + 1;
    }

    @Override
    public void down() {
        newY = y + 1;
    }

    @Override
    public void up() {
        newY = y - 1;
    }

    @Override
    public void left() {
        newX = x - 1;
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
    }
}

