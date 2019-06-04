package com.codenjoy.dojo.excitebike.model;

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



import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.model.items.Border;
import com.codenjoy.dojo.excitebike.services.Events;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class GameFieldImpl implements GameField {

    private List<Player> players;
    private MapParser mapParser;
    private Dice dice;

    public GameFieldImpl(MapParser mapParser, Dice dice) {
        this.dice = dice;
        players = new LinkedList<>();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            Bike hero = player.getHero();

            hero.tick();

          /*  if (gold.contains(hero)) {
                gold.remove(hero);
                player.event(Events.WIN);

                Point pos = getNewPlayerPosition();
                gold.add(new Gold(pos));
            }*/
        }

        for (Player player : players) {
            Bike hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(Events.LOOSE);
            }
        }
    }

    public int size() {
        return mapParser.getXSize();
    }

    @Override
    public Point getNewPlayerPosition() {
        //TODO implement right logic
        int x;
        int y;
        int c = 0;
//        do {
//            x = dice.next(level.getSize());
//            y = dice.next(level.getSize());
//        } while (!isFree(x, y) && c++ < 100);

        if (c >= 100) {
            return pt(0, 0);
        }

//        return pt(x, y);
        return  null;
    }

    @Override
    public boolean isBorder(int x, int y) {
        Point point = pt(x, y);
//        return level.getBorders().contains(point);
        return false;
    }

    @Override
    public boolean isInhibitor(int x, int y) {
        Point point = pt(x, y);
//        return level.getInhibitors().contains(point);
        return false;
    }

    @Override
    public boolean isAccelerator(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public boolean isObstacle(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public boolean isLineChanger(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public boolean isRoadElement(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public boolean isBike(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public void inclineBikeToLeft() {

    }

    @Override
    public void inclineBikeToRight() {

    }


    public List<Bike> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    //TODO make correct when Level class will be implemented
    @Override
    public BoardReader reader() {
        return new BoardReader() {

            @Override
            public int size() {
                return mapParser.getXSize();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>(){{
                    //addAll(GameFieldImpl.this.getWalls());
                    addAll(GameFieldImpl.this.getHeroes());
                    //addAll(GameFieldImpl.this.getGold());
                    //addAll(GameFieldImpl.this.getBombs());
                }};
            }
        };
    }
}
