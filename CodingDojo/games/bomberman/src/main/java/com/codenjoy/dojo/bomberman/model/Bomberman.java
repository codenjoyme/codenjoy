package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Bomberman implements Field {

    private List<Player> players = new LinkedList<>();

    private Walls walls;
    private Parameter<Integer> size;
    private List<Bomb> bombs;
    private List<Blast> blasts;
    private GameSettings settings;
    private List<Point> destroyedWalls;
    private List<Bomb> destroyedBombs;

    public Bomberman(GameSettings settings) {
        this.settings = settings;
        bombs = new LinkedList<>();
        blasts = new LinkedList<>();
        destroyedWalls = new LinkedList<>();
        destroyedBombs = new LinkedList<>();
        size = settings.getBoardSize();
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
        removeBlasts();
        tactAllBombermans();
        meatChopperEatBombermans();
        walls.tick();
        meatChopperEatBombermans();
        tactAllBombs();
    }

    private void tactAllBombermans() {
        for (Player player : players) {
            player.getHero().apply();
        }
    }

    private void removeBlasts() {
        blasts.clear();
        for (Point pt : destroyedWalls) {
            walls.destroy(pt.getX(), pt.getY());
        }
        destroyedWalls.clear();
    }

    private void wallDestroyed(Wall wall, Blast blast) {
        for (Player player : players) {
            if (blast.itsMine(player.getHero())) {
                if (wall instanceof MeatChopper) {
                    player.event(Events.KILL_MEAT_CHOPPER);
                } else if (wall instanceof DestroyWall) {
                    player.event(Events.KILL_DESTROY_WALL);
                }
            }
        }
    }

    private void meatChopperEatBombermans() {
        for (MeatChopper chopper : walls.subList(MeatChopper.class)) {
            for (Player player : players) {
                Hero bomberman = player.getHero();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.event(Events.KILL_BOMBERMAN);
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
        return bombs;
    }

    @Override
    public List<Bomb> getBombs(Hero bomberman) {
        List<Bomb> result = new LinkedList<>();
        for (Bomb bomb : bombs) {
            if (bomb.itsMine(bomberman)) {
                result.add(bomb);
            }
        }
        return result;
    }

    @Override
    public List<Blast> getBlasts() {
        return blasts;
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
        List barriers = walls.subList(Wall.class);
        barriers.addAll(getBombermans());

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, size.getValue(), bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts, Bomb bomb) {
        for (Blast blast: blasts) {
            if (walls.itsMe(blast.getX(), blast.getY())) {
                destroyedWalls.add(blast);

                Wall wall = walls.get(blast.getX(), blast.getY());
                wallDestroyed(wall, blast);
            }
        }
        for (Blast blast: blasts) {
            for (Player dead : players) {
                if (dead.getHero().itsMe(blast)) {
                    dead.event(Events.KILL_BOMBERMAN);

                    for (Player bombOwner : players) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getHero())) {
                            bombOwner.event(Events.KILL_OTHER_BOMBERMAN);
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
        for (Hero bomberman : getBombermans()) {
            if (bomberman.itsMe(pt(x, y))) {
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
    public List<Hero> getBombermans() {
        List<Hero> result = new LinkedList<Hero>();
        for (Player player : players) {
            result.add(player.getHero());
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

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Bomberman.this.size();

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(Bomberman.this.getBombermans());
                    Bomberman.this.getWalls().forEach(this::add);
                    addAll(Bomberman.this.getBombs());
                    addAll(Bomberman.this.getBlasts());
                }};
            }
        };
    }
}
