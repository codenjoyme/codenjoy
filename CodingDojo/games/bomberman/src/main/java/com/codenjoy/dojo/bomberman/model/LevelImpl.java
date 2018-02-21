package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:59
 */
public class LevelImpl implements Level {

    private final LengthToXY xy;
    private String map;
    private Field board;
    private Walls walls;
    private Parameter<Integer> bombs;
    private Parameter<Integer> size;

    private LinkedList<PointImpl> destroyedWalls;
    private LinkedList<Bomb> destroyedBombs;
    private LinkedList<Bomb> listBombs;
    private LinkedList<Blast> listBlast;
    private LinkedList<Player> listPlayers;

    private OriginalWalls originalWalls;
    private EatSpaceWalls eatWalls;
    private MeatChoppers meatChoppers;

    public LevelImpl(String map) {
        this.map = map;
        this.size = v((int)Math.sqrt(map.length()));
        this.xy = new LengthToXY(this.size.getValue());
        this.bombs = v(getPointsOf(Elements.BOOM).size());
        this.destroyedWalls = new LinkedList<PointImpl>();
        this.destroyedBombs = new LinkedList<Bomb>();
        this.listBombs = new LinkedList<Bomb>();
        this.listBlast = new LinkedList<Blast>();
        this.listPlayers = new LinkedList<Player>();

        //выставляем стены
        List<Point> points = getPointsOf(Elements.WALL);
        this.originalWalls = new OriginalWalls(points);

        //Выставляем уничтожаемые стены
        points = getPointsOf(Elements.DESTROYABLE_WALL);
        this.eatWalls = new EatSpaceWalls(originalWalls, this, points, new RandomDice());

        //Выставляем мясников
        points = getPointsOf(Elements.MEAT_CHOPPER);
        this.meatChoppers = new MeatChoppers(eatWalls, this, points, new RandomDice());

        this.walls = originalWalls;

    }

    // Map
    @Override
    public Parameter<Integer> getSize() {
        return v((int)Math.sqrt(map.length()));
    }

    public Field getBoard() {
        return this.board;
    }

    //Bombs Section
    @Override
    public Parameter<Integer> bombsCount() {
        return this.bombs;
    }

    @Override
    public Parameter<Integer> bombsPower() {
        return v(3);
    }

    @Override
    public int size() {
        return size.getValue();
    }

    @Override
    public List<Bomb> getBombs() {
        return this.listBombs;
    }

    @Override
    public List<Bomb> getBombs(HeroImpl bomberman) {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : getBombs()) {
            if (bomb.itsMine(bomberman)) {
                result.add(bomb);
            }
        }
        return result;
    }

    @Override
    public List<Bomb> getDestroyedBombs() {
        return this.destroyedBombs;
    }

    @Override
    public boolean isBarrier(int x, int y, boolean isWithMeatChopper) {
        for (Player bomberman : getPlayers()) {
            if (bomberman.getBomberman().itsMe(new PointImpl(x, y))) {
                return true;
            }
        }
        for (Bomb bomb : getBombs()) {
            if (bomb.itsMe(x, y)) {
                return true;
            }
        }
        for (Wall wall : getWalls()) {
            if (wall instanceof MeatChopper && !isWithMeatChopper) {
                continue;
            }
            if (wall.itsMe(x, y)) {
                return true;
            }
        }
        return x < 0 || y < 0 || x > size.getValue() - 1 || y > size.getValue() - 1;
    }

    //Blast Section
    @Override
    public List<Blast> getBlasts() {
        return this.listBlast;
    }

    @Override
    public void drop(Bomb bomb) {
        if (!existAtPlace(bomb.getX(), bomb.getY())) {
            getBombs().add(bomb);
        }
    }

    @Override
    public void removeBomb(Bomb bomb) {
        this.destroyedBombs.add(bomb);
    }


    @Override
    public List<Player> getPlayers() {
        return this.listPlayers;
    }

    //Wall Section
    @Override
    public Walls getWalls() {
        return this.walls;
    }


    @Override
    public List<PointImpl> getDestroyedWalls() {
        return this.destroyedWalls;
    }

    @Override
    public List<Hero> getBombermans() {
        List<Hero> result = new LinkedList<Hero>();
        for (Player player : this.listPlayers) {
            result.add(player.getBomberman());
        }
        return result;
    }

    @Override
    public void remove(Player player) {
        this.listPlayers.remove(player);
    }

    // Private methods
    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.getChar()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

    private boolean existAtPlace(int x, int y) {
        for (Bomb bomb : getBombs()) {
            if (bomb.getX() == x && bomb.getY() == y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        this.walls.tick();
        this.originalWalls.tick();
        this.eatWalls.tick();
        this.meatChoppers.tick();
    }
}
