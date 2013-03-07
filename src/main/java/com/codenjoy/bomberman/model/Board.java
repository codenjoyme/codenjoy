package com.codenjoy.bomberman.model;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board {
    private Walls walls;
    private Level level;
    private int size;
    private MyBomberman bomberman;
    private List<Bomb> bombs;
    private List<Point> blasts;

    public Board(Walls walls, Level level, int size) {
        this.walls = walls;
        this.level = level;
        this.size = size;
        bomberman = new MyBomberman(level, this);
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Point>();
    }

    public int size() {
        return size;
    }

    public Bomberman getBomberman() {
        return bomberman;
    }

    public void tact() {
        blasts.clear();
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

    public List<Point> getBlasts() {
        return blasts;
    }

    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bomb.setAffect(new Boom() {
                @Override
                public void boom(Bomb bomb) {
                    bombs.remove(bomb);
                    makeBlast(bomb.getX(), bomb.getY(), bomb.getPower());
                    killAllNear(bomb.getX(), bomb.getY(), bomb.getPower());
                }
            });
            bombs.add(bomb);
        }
    }

    private void makeBlast(int cx, int cy, int blastWave) {
        blasts.addAll(new BoomEngineBad().boom(walls.asList(), size, new Point(cx, cy), blastWave));
    }

    private boolean isOutOfBoard(int x, int y) {
        return x < 0 || x >= size || y < 0 || y >= size;
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

    public Walls getWalls() {
        return walls;
    }

    public boolean isBarrier(int x, int y) {
        for (Bomb bomb : getBombs()) {
            if (bomb.itsMe(x, y)) {
                return true;
            }
        }
        if (getWalls().itsMe(x, y)) {
            return true;
        }
        return false;
    }
}
