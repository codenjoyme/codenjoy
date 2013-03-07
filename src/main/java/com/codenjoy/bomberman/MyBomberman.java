package com.codenjoy.bomberman;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman implements Bomberman {
    private int x;
    private int y;
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
        x++;
        if (x >= boardSize) {
            x = boardSize - 1;
        }
    }

    @Override
    public void down() {
        y++;
        if (y >= boardSize) {
            y = boardSize - 1;
        }
    }

    @Override
    public void up() {
        y--;
        if (y < 0) {
            y = 0;
        }
    }

    @Override
    public void left() {
        x--;
        if (x < 0) {
            x = 0;
        }
    }
}

