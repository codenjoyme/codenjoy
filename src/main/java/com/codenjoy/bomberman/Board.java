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
        tactAllBombs();
    }

    private void tactAllBombs() {
        for (Bomb bomb : bombs) {
            bomb.tick();
        }
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bomb.setAffect(new Boom() {
                @Override
                public void boom(Bomb bomb) {
                    bombs.remove(bomb);
                    killAllNear(bomb.getX(), bomb.getY(), 1);
                }
            });
            bombs.add(bomb);
        }
    }

    private void killAllNear(int x, int y, int blastWave) {
        int dx = Math.abs(bomberman.getX() - x);
        int dy = Math.abs(bomberman.getY() - y);
        if (dx <= blastWave && dy <= blastWave) {
            bomberman.kill();
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

    public boolean isGameOver() {
        return !bomberman.isAlive();
    }
}
