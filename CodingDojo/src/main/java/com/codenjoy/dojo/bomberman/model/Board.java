package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board implements Game {

    protected final Player player = new Player();

    private Walls walls;
    private int size;
    private List<Bomb> bombs;
    private List<Point> blasts;
    private BombermanPrinter printer;
    private Level level;
    private GameSettings settings;
    private List<Point> destoyed;

    public Board(GameSettings settings, EventListener listener) {
        this.player.maxScore = 0;
        this.player.score = 0;

        this.settings = settings;
        this.player.listener = listener;
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Point>();
        destoyed = new LinkedList<Point>();
        printer = new BombermanPrinter(this);
    }

    public int size() {
        return size;
    }

    public Joystick getJoystick() {
        return player.bomberman;
    }

    @Override
    public int getMaxScore() {
        return player.maxScore;
    }

    @Override
    public int getCurrentScore() {
        return player.score;
    }

    @Override
    public void tick() {
        removeBlasts();
        player.bomberman.apply();
        tactAllMeatChoppers();
        tactAllBombs();
    }

    private void removeBlasts() {
        blasts.clear();
        for (Point pt : destoyed) {
            walls.destroy(pt.x, pt.y);
        }
        destoyed.clear();
    }

    private void eventWallDestroyed(Wall wall) {
        if (wall instanceof MeatChopper) {
            player.listener.event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        } else if (wall instanceof DestroyWall) {
            player.listener.event(BombermanEvents.KILL_DESTROY_WALL.name());
        }
    }

    private void tactAllMeatChoppers() {
        if (walls instanceof MeatChoppers) {
            ((MeatChoppers) walls).tick();
            for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
                if (chopper.itsMe(player.bomberman.getX(), player.bomberman.getY())) {
                    player.bomberman.kill();
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
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : bombs) {
            result.add(new BombCopier(bomb));
        }
        return result;
    }

    public List<Point> getBlasts() {
        List<Point> result = new LinkedList<Point>();
        for (Point blast : blasts) {
            result.add(new Point(blast));
        }
        return result;
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
            if (walls.itsMe(blast.getX(), blast.getY())) {
                destoyed.add(blast);

                Wall wall = walls.get(blast.getX(), blast.getY());
                eventWallDestroyed(wall);
                increaseScore();
            }
        }
        for (Point blast: blasts) {
            if (player.bomberman.itsMe(blast)) {
                player.bomberman.kill();
                player.score = 0;
            }
        }
    }

    private void increaseScore() {
        player.score = player.score + 1;
        player.maxScore = Math.max(player.maxScore, player.score);
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
        return !player.bomberman.isAlive();
    }

    @Override
    public void newGame() {
        this.size = settings.getBoardSize();
        this.level = settings.getLevel();
        this.walls = settings.getWalls();
        this.player.score = 0;
        this.player.bomberman = settings.getBomberman(level);
        this.player.bomberman.init(this);
//        bombs = new LinkedList<Bomb>();  // TODO implement me
        blasts = new LinkedList<Point>();
//        destoyed = new LinkedList<Point>();
    }

    @Override
    public String getBoardAsString() {
        return this.toString();
    }

    @Override
    public String toString() {
        return printer.print();
    }

    public Walls getWalls() {
        return new WallsImpl(walls);
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

    public Bomberman getBomberman() {
        return player.bomberman;
    }
}
