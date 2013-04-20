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
public class Board implements Tickable, IBoard {

    private List<Player> players = new LinkedList<Player>();

    private Walls walls;
    private int size;
    private List<Bomb> bombs;
    private List<Blast> blasts;
    private GameSettings settings;
    private List<Point> destoyed;
    private int timer;

    public Board(GameSettings settings) {
        timer = 0;
        this.settings = settings;
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Blast>();
        destoyed = new LinkedList<Point>();
        size = settings.getBoardSize();
        walls = settings.getWalls();
    }

    public GameSettings getSettings() {
        return settings;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void tick() {
        timer++;
        if (timer >= players.size()) {
            timer = 0;
        } else {
            return;
        }

        removeBlasts();
        for (Player player : players) {
            player.getBomberman().apply();
        }
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

    private void wallDestroyed(Wall wall) {
        if (wall instanceof MeatChopper) {
            for (Player player : players) {
                player.event(BombermanEvents.KILL_MEAT_CHOPPER);
            }
        } else if (wall instanceof DestroyWall) {
            for (Player player : players) {
                player.event(BombermanEvents.KILL_DESTROY_WALL);
            }
        }
    }

    private void tactAllMeatChoppers() {
        if (walls instanceof MeatChoppers) {
            ((MeatChoppers) walls).tick();
            for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
                for (Player player : players) {
                    if (chopper.itsMe(player.getBomberman().getX(), player.getBomberman().getY())) {
                        player.getBomberman().kill();
                    }
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : bombs.toArray(new Bomb[0])) {
            bomb.tick();
        }
    }

    @Override
    public List<Bomb> getBombs() {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : bombs) {
            result.add(new BombCopier(bomb));
        }
        return result;
    }

    @Override
    public List<IPoint> getBlasts() {
        List<IPoint> result = new LinkedList<IPoint>();
        for (IPoint blast : blasts) {
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
                    makeBlast(bomb);
                    killAllNear();
                }
            });
            bombs.add(bomb);
        }
    }

    private void makeBlast(Bomb bomb) {
        blasts.addAll(new BoomEngineOriginal(bomb.getOwner()).boom((List) walls.subList(Wall.class), size, bomb, bomb.getPower()));
    }

    private void killAllNear() {
        for (Blast blast: blasts) {
            if (walls.itsMe(blast.getX(), blast.getY())) {
                destoyed.add(blast);

                Wall wall = walls.get(blast.getX(), blast.getY());
                wallDestroyed(wall);
            }
        }
        for (Blast blast: blasts) {
            for (Player dead : players) {
                if (dead.getBomberman().itsMe(blast)) {
                    dead.event(BombermanEvents.KILL_BOMBERMAN);

                    for (Player bombOwner : players) {
                        if (dead != bombOwner && blast.checkOwner(bombOwner.getBomberman())) {
                            bombOwner.event(BombermanEvents.KILL_MEAT_CHOPPER);
                        }
                    }
                }
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

    @Override
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

    @Override
    public Bomberman getBomberman() {
        throw new UnsupportedOperationException(); // TODO fixme
    }

    @Override
    public List<Bomberman> getBombermans() {
        List<Bomberman> result = new LinkedList<Bomberman>();
        for (Player player : players) {
            result.add(player.getBomberman());
        }
        return result;
    }

    public void add(Player player) {
        players.add(player);
        player.init(settings);
    }
}
