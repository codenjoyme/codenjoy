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
    private List<Point> blasts;
    private GameSettings settings;
    private List<Point> destoyed;
    private EventListener listener;

    public Board(GameSettings settings, EventListener listener) {
        this.settings = settings;
        this.listener = listener;
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Point>();
        destoyed = new LinkedList<Point>();
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
            for (Player player : players) { // TODO only for my bombs
                player.event(BombermanEvents.KILL_MEAT_CHOPPER.name());
            }
        } else if (wall instanceof DestroyWall) {
            for (Player player : players) {
                player.event(BombermanEvents.KILL_DESTROY_WALL.name());
            }
        }
        for (Player player : players) {
            player.increaseScore();
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
                wallDestroyed(wall);
            }
        }
        for (Point blast: blasts) {
            for (Player player : players) {
                if (player.getBomberman().itsMe(blast)) {
                    player.gameOver();
                    player.event(BombermanEvents.KILL_BOMBERMAN.name());
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

    public void newGame() {
        this.size = settings.getBoardSize();
        this.walls = settings.getWalls();
//        bombs = new LinkedList<Bomb>();  // TODO implement me
        blasts = new LinkedList<Point>();
//        destoyed = new LinkedList<Point>();
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

    public void add(Player player) {
        players.add(player);
        player.init(settings, listener);
    }
}
