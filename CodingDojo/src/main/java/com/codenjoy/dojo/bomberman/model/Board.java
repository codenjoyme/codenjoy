package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Board implements Tickable, IBoard {
    private static Logger logger = LoggerFactory.getLogger(Board.class);

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
        walls = settings.getWalls(this);  // TODO как-то красивее сделать
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
        if (collectTicks()) return;
        logger.debug("--- tact start --------------------");

        removeBlasts();
        tactAllBombermans();
        meatChopperEatBombermans();
        walls.tick();
        meatChopperEatBombermans();
        tactAllBombs();

        logger.debug("--- tact end ----------------------");
    }

    private boolean collectTicks() {
        timer++;
        if (timer >= players.size()) {
            timer = 0;
        } else {
            return true;
        }
        return false;
    }

    private void tactAllBombermans() {
        for (Player player : players) {
            player.getBomberman().apply();
        }
    }

    private void removeBlasts() {
        blasts.clear();
        for (Point pt : destoyed) {
            walls.destroy(pt.x, pt.y);
        }
        destoyed.clear();
    }

    private void wallDestroyed(Wall wall, Blast blast, Bomb bomb) {
        for (Player player : players) {
            if (blast.itsMine(player.getBomberman())) {
                if (wall instanceof MeatChopper) {
                    player.event(BombermanEvents.KILL_MEAT_CHOPPER);

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Player %s kills meat chopper %s with blast %s with bomb %s",
                                player.hashCode(), wall.hashCode(), blast.hashCode(), bomb.hashCode()));
                    }
                } else if (wall instanceof DestroyWall) {
                    player.event(BombermanEvents.KILL_DESTROY_WALL);

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Player %s kills wall %s with blast %s with bomb %s",
                                player.hashCode(), wall.hashCode(), blast.hashCode(), bomb.hashCode()));
                    }
                }
            }
        }
    }

    private void meatChopperEatBombermans() {
        for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
            for (Player player : players) {
                Bomberman bomberman = player.getBomberman();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.event(BombermanEvents.KILL_BOMBERMAN);

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Player %s die from meatchopper %s",
                                player.hashCode(), chopper.hashCode()));
                    }
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : bombs.toArray(new Bomb[0])) {
            if (bomb != null) { // TODO исследовать, как тут может быть null
                bomb.tick();
            }
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

    public List<Bomb> getBombs(MyBomberman bomberman) {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : bombs) {
            if (bomb.itsMine(bomberman)) {
                result.add(new BombCopier(bomb));
            }
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
                    List<Blast> blast = makeBlast(bomb);
                    killAllNear(blast, bomb);
                    blasts.addAll(blast);
                }
            });
            bombs.add(bomb);
        }
    }

    private List<Blast> makeBlast(Bomb bomb) {
        List barriers = (List) walls.subList(Wall.class);
        barriers.addAll(getBombermans());

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size, bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts, Bomb bomb) {
        for (Blast blast: blasts) {
            if (walls.itsMe(blast.getX(), blast.getY())) {
                destoyed.add(blast);

                Wall wall = walls.get(blast.getX(), blast.getY());
                wallDestroyed(wall, blast, bomb);
            }
        }
        for (Blast blast: blasts) {
            for (Player dead : players) {
                if (dead.getBomberman().itsMe(blast)) {
                    dead.event(BombermanEvents.KILL_BOMBERMAN);

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("Player %s die from blast %s from bomb %s",
                            dead.hashCode(), blast.hashCode(), bomb.hashCode()));
                    }

                    for (Player bombOwner : players) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getBomberman())) {
                            bombOwner.event(BombermanEvents.KILL_OTHER_BOMBERMAN);

                            if (logger.isDebugEnabled()) {
                                logger.debug(String.format("...and killer is %s with bomb %s",
                                        bombOwner.hashCode(), bomb.hashCode()));
                            }
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
        for (Bomberman bomberman : getBombermans()) {
            if (bomberman.itsMe(new Point(x, y))) {
                return true;
            }
        }
        for (Bomb bomb : bombs) {
            if (bomb.itsMe(x, y)) {
                return true;
            }
        }
        for (Wall wall : walls) {
            if (wall instanceof MeatChopper) {
                continue;
            }
            if (wall.itsMe(x, y)) {
                return true;
            }
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

    public void remove(Player player) {
        players.remove(player);
    }

}
