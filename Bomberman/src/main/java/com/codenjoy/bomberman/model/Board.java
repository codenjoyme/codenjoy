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
        tactAllMeatChoppers();
        tactAllBombs();
    }

    private void tactAllMeatChoppers() {
        if (walls instanceof MeatChoppers) {
            ((MeatChoppers) walls).tick();
            for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
                if (chopper.itsMe(bomberman)) {
                    bomberman.kill();
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : bombs.toArray(new Bomb[0])) {
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
                    killAllNear();
                }
            });
            bombs.add(bomb);
        }
    }

    private void makeBlast(int cx, int cy, int blastWave) {
        blasts.addAll(new BoomEngineOriginal().boom((List) walls.subList(Wall.class), size, new Point(cx, cy), blastWave));
    }

    private void killAllNear() {
        for (Point blast: blasts) {
            if (bomberman.itsMe(blast)) {
                bomberman.kill();
            }
            if (walls.itsMe(blast.getX(), blast.getY())) {
                walls.destroy(blast.getX(), blast.getY());
            }
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
        for (Bomb bomb : bombs) {
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
