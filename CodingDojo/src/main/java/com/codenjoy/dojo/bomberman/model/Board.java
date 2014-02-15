package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.settings.Parameter;
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
    private Parameter<Integer> size;
    private int currentSize;
    private List<Bomb> bombs;
    private List<Blast> blasts;
    private GameSettings settings;
    private List<PointImpl> destroyedWalls;
    private List<Bomb> destroyedBombs;

    public Board(GameSettings settings) {
        this.settings = settings;
        bombs = new LinkedList<Bomb>();
        blasts = new LinkedList<Blast>();
        destroyedWalls = new LinkedList<PointImpl>();
        destroyedBombs = new LinkedList<Bomb>();
        size = settings.getBoardSize();
        currentSize = size.getValue();
        walls = settings.getWalls(this);  // TODO как-то красивее сделать
    }

    public GameSettings getSettings() {
        return settings;
    }

    @Override
    public int size() {
        return size.getValue();
    }

    @Override
    public void tick() {
        logger.debug("--- tact start --------------------");

        removeBlasts();
        tactAllBombermans();
        meatChopperEatBombermans();
        walls.tick();
        meatChopperEatBombermans();
        tactAllBombs();

        logger.debug("--- tact end ----------------------");
    }

    private void tactAllBombermans() {
        for (Player player : players) {
            player.getBomberman().apply();
        }
    }

    private void removeBlasts() {
        blasts.clear();
        for (PointImpl pt : destroyedWalls) {
            walls.destroy(pt.getX(), pt.getY());
        }
        destroyedWalls.clear();
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
        for (Bomb bomb : bombs) {
            bomb.tick();
        }

        for (Bomb bomb : destroyedBombs) {
            bombs.remove(bomb);

            List<Blast> blast = makeBlast(bomb);
            killAllNear(blast, bomb);
            blasts.addAll(blast);
        }
        destroyedBombs.clear();
    }

    @Override
    public List<Bomb> getBombs() {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : bombs) {
            result.add(bomb);
        }
        return result;
    }

    @Override
    public List<Bomb> getBombs(MyBomberman bomberman) {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : bombs) {
            if (bomb.itsMine(bomberman)) {
                result.add(bomb);
            }
        }
        return result;
    }

    @Override
    public List<Point> getBlasts() {
        List<Point> result = new LinkedList<Point>();
        for (Point blast : blasts) {
            result.add(new PointImpl(blast));
        }
        return result;
    }

    @Override
    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            bombs.add(bomb);
        }
    }

    @Override
    public void removeBomb(Bomb bomb) {
        destroyedBombs.add(bomb);
    }

    private List<Blast> makeBlast(Bomb bomb) {
        List barriers = (List) walls.subList(Wall.class);
        barriers.addAll(getBombermans());

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size.getValue(), bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts, Bomb bomb) {
        for (Blast blast: blasts) {
            if (walls.itsMe(blast.getX(), blast.getY())) {
                destroyedWalls.add(blast);

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

    @Override
    public boolean isBarrier(int x, int y, boolean isWithMeatChopper) {
        for (Bomberman bomberman : getBombermans()) {
            if (bomberman.itsMe(new PointImpl(x, y))) {
                return true;
            }
        }
        for (Bomb bomb : bombs) {
            if (bomb.itsMe(x, y)) {
                return true;
            }
        }
        for (Wall wall : walls) {
            if (wall instanceof MeatChopper && !isWithMeatChopper) {
                continue;
            }
            if (wall.itsMe(x, y)) {
                return true;
            }
        }
        return x < 0 || y < 0 || x > size() - 1 || y > size() - 1;
    }

    @Override
    public List<Bomberman> getBombermans() {
        List<Bomberman> result = new LinkedList<Bomberman>();
        for (Player player : players) {
            result.add(player.getBomberman());
        }
        return result;
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }
}
