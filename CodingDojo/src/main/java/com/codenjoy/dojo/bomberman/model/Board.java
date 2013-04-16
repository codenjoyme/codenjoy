package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board implements Game {

    private List<Player> players = Arrays.asList(new Player());

    private Walls walls;
    private int size;
    private List<Bomb> bombs;
    private List<Point> blasts;
    private BombermanPrinter printer;
    private Level level;
    private GameSettings settings;
    private List<Point> destoyed;

    public Board(GameSettings settings, EventListener listener) {
        this.players.get(0).maxScore = 0;
        this.players.get(0).score = 0;

        this.settings = settings;
        this.players.get(0).listener = listener;
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Point>();
        destoyed = new LinkedList<Point>();
        printer = new BombermanPrinter(this);
    }

    public int size() {
        return size;
    }

    public Joystick getJoystick() {
        return players.get(0).bomberman;
    }

    @Override
    public int getMaxScore() {
        return players.get(0).maxScore;
    }

    @Override
    public int getCurrentScore() {
        return players.get(0).score;
    }

    @Override
    public void tick() {
        removeBlasts();
        players.get(0).bomberman.apply();
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
            players.get(0).listener.event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        } else if (wall instanceof DestroyWall) {
            players.get(0).listener.event(BombermanEvents.KILL_DESTROY_WALL.name());
        }
    }

    private void tactAllMeatChoppers() {
        if (walls instanceof MeatChoppers) {
            ((MeatChoppers) walls).tick();
            for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
                if (chopper.itsMe(players.get(0).bomberman.getX(), players.get(0).bomberman.getY())) {
                    players.get(0).bomberman.kill();
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
            if (players.get(0).bomberman.itsMe(blast)) {
                players.get(0).bomberman.kill();
                players.get(0).score = 0;
            }
        }
    }

    private void increaseScore() {
        players.get(0).score = players.get(0).score + 1;
        players.get(0).maxScore = Math.max(players.get(0).maxScore, players.get(0).score);
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
        return !players.get(0).bomberman.isAlive();
    }

    @Override
    public void newGame() {
        this.size = settings.getBoardSize();
        this.level = settings.getLevel();
        this.walls = settings.getWalls();
        this.players.get(0).score = 0;
        this.players.get(0).bomberman = settings.getBomberman(level);
        this.players.get(0).bomberman.init(this);
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
        return players.get(0).bomberman;
    }
}
