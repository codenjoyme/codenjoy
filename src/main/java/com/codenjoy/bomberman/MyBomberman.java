package com.codenjoy.bomberman;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman implements Bomberman {
    private int curX;
    private int curY;
    private int boardSize;

    public MyBomberman(int boardSize) {
        this.boardSize = boardSize;
    }

    @Override
    public int getX() {
        return curX;
    }

    @Override
    public int getY() {
        return curY;
    }

    @Override
    public void right() {
        curX++;
        if (curX >= boardSize) {
            curX = boardSize - 1;
        }
    }

    @Override
    public void down() {
        curY++;
    }

    @Override
    public void up() {
        curY--;
        if (curY < 0) {
            curY = 0;
        }
    }

    @Override
    public void left() {
        curX--;
        if (curX < 0) {
            curX = 0;
        }
    }
}

