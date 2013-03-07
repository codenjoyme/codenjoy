package com.codenjoy.bomberman;

import com.codenjoy.bomberman.Bomberman;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:41 AM
 */
public class MyBomberman implements Bomberman {
    private int curX;
    private int curY;

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
    }

    @Override
    public void down() {
        curY++;
    }
}

