package com.codenjoy.bomberman;

import java.util.Arrays;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private int size;
    private MyBomberman bomberman;

    public Board(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public Bomberman getBomberman() {
        bomberman = new MyBomberman(size);
        return bomberman;
    }

    public void tact() {
        bomberman.apply();
    }

    public List<Bomb> getBombs() {
        return Arrays.asList(new Bomb());
    }
}
