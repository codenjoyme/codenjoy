package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.bomberman.services.BombermanPlotsBuilder;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.services.playerdata.PlotsBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board implements Game {

    private Walls walls;
    private int size;
    protected Bomberman bomberman;
    private List<Bomb> bombs;
    private List<Point> blasts;
    private BombermanPrinter printer;
    private PlotsBuilder plots;
    private Level level;
    private GameSettings settings;
    private EventListener listener;
    private List<Point> destoyed;

    public Board(GameSettings settings, EventListener listener) {
        this.settings = settings;
        this.listener = listener;
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Point>();
        destoyed = new LinkedList<Point>();
        printer = new BombermanPrinter(this);
        plots = new BombermanPlotsBuilder(this);
    }

    public int size() {
        return size;
    }

    public Joystick getJoystick() {
        return bomberman;
    }

    @Override
    public int getMaxScore() {
        return 13;  //TODO fixme
    }

    @Override
    public int getCurrentScore() {
        return 14; //TODO fixme
    }

    @Override
    public void tick() {
        removeBlasts();
        bomberman.apply();
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
//            listener.event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        } else if (wall instanceof DestroyWall) {
            listener.event(BombermanEvents.KILL_DESTROY_WALL.name());
        }
    }

    private void tactAllMeatChoppers() {
        if (walls instanceof MeatChoppers) {
            ((MeatChoppers) walls).tick();
            for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
                if (chopper.itsMe(bomberman.getX(), bomberman.getY())) {
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
            if (bomberman.itsMe(blast)) {
                bomberman.kill();
            }
            if (walls.itsMe(blast.getX(), blast.getY())) {
                destoyed.add(blast);

                Wall wall = walls.get(blast.getX(), blast.getY());
                eventWallDestroyed(wall);
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

    @Override
    public void newGame() {
        this.size = settings.getBoardSize();
        this.level = settings.getLevel();
        this.walls = settings.getWalls();
        this.bomberman = settings.getBomberman(level);
        this.bomberman.init(this);
    }

    @Override
    public String getBoardAsString() {
        return printer.print();
    }

    @Override
    public List<Plot> getPlots() {
        return plots.get();
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
        return bomberman;
    }
}
