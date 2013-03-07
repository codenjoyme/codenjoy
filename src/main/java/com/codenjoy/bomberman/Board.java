package com.codenjoy.bomberman;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private Level level;
    private int size;
    private MyBomberman bomberman;
    private List<Bomb> bombs;

    public Board(Level level, int size) {
        this.level = level;
        this.size = size;
        bombs = new LinkedList<Bomb>();
    }

    public int size() {
        return size;
    }

    public Bomberman getBomberman() {
        bomberman = new MyBomberman(level, this);
        return bomberman;
    }

    public void tact() {
        bomberman.apply();
    }

    public List<Bomb> getBombs() {
        return bombs; // TODO return clone
    }

    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bombs.add(bomb);
        }
    }

    private boolean existAtPlace(int x, int y) {
        for (Bomb bomb : bombs) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }
        return false;
    }
}
