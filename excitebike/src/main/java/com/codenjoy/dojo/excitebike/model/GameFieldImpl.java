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


import com.codenjoy.dojo.excitebike.model.items.*;
import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class GameFieldImpl implements GameField {

    private Dice dice;
    private MapParser mapParser;

    //TODO mb use Set not List
    private Map<CharElements, List<? extends Shiftable>> allShiftableElements;

    private List<Player> players;
    private List<Border> borders;
    private List<OffRoad> offRoads;
    private List<Accelerator> accelerators;
    private List<Inhibitor> inhibitors;
    private List<Obstacle> obstacles;
    private List<LineChanger> lineUpChangers;
    private List<LineChanger> lineDownChangers;


    public GameFieldImpl(MapParser mapParser, Dice dice) {
        this.dice = dice;
        this.mapParser = mapParser;

        players = new LinkedList<>();

        borders = mapParser.getBorders();
        offRoads = mapParser.getOffRoads();
        accelerators = mapParser.getAccelerators();
        inhibitors = mapParser.getInhibitors();
        obstacles = mapParser.getObstacles();
        lineUpChangers = mapParser.getLineUpChangers();
        lineDownChangers = mapParser.getLineDownChangers();

        allShiftableElements = new HashMap<>();
        allShiftableElements.put(GameElementType.OFF_ROAD, offRoads);
        allShiftableElements.put(GameElementType.ACCELERATOR, accelerators);
        allShiftableElements.put(GameElementType.INHIBITOR, inhibitors);
        allShiftableElements.put(GameElementType.OBSTACLE, obstacles);
        allShiftableElements.put(GameElementType.LINE_CHANGER_UP, lineUpChangers);
        allShiftableElements.put(GameElementType.LINE_CHANGER_DOWN, lineDownChangers);
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        shiftTrack();

        for (Player player : players) {
            Bike bike = player.getHero();

            bike.tick();
        }
//
//          /*  if (gold.contains(hero)) {
//                gold.remove(hero);
//                player.event(Events.WIN);
//
//                Point pos = getNewPlayerPosition();
//                gold.add(new Gold(pos));
//            }*/
//        }
//
//        for (Player player : players) {
//            Bike hero = player.getHero();
//
//            if (!hero.isAlive()) {
//                player.event(Events.LOOSE);
//            }
//        }
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
        return null;
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

    public List<Bike> getBikes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public List<Border> getBorders() {
        return borders;
    }

    //TODO mb add bikes to shiftables
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
                return new LinkedList<Point>() {{
                    addAll(GameFieldImpl.this.getBikes());
                    GameFieldImpl.this.allShiftableElements.values().forEach(els -> this.addAll(els));
                    addAll(getBorders());
                }};
            }
        };
    }

    private void shiftTrack() {
        final int lastPossibleX = 0;
        final int firstPossibleX = mapParser.getXSize();

        allShiftableElements.values().forEach(
                pointsOfElementType -> pointsOfElementType.forEach(Shiftable::shift)
        );

        allShiftableElements.values().forEach(
                pointsOfElementType -> pointsOfElementType.removeIf(point -> point.getX() < lastPossibleX)
        );

        generateNewTrackStep(mapParser.getXSize(), firstPossibleX);
    }

    private void generateNewTrackStep(final int laneNumber, final int firstPossibleX) {

        int pseudoRandomForElem = 3; //TODO 2<rnd<GameElementType.values().length   -none,-border
        int pseudoRandomForLane = 3; //TODO 1<rnd<laneNumber-1  -borders

        GameElementType randomType = GameElementType.values()[pseudoRandomForElem];
        List<Shiftable> elements = (List<Shiftable>) allShiftableElements.get(randomType);
//        elements.add(getNewElement(randomType, firstPossibleX, pseudoRandomForLane));
    }

    private Shiftable getNewElement(GameElementType randomType, int x, int y) {
        switch (randomType) {
            case OFF_ROAD:
                return new OffRoad(x, y);
            case ACCELERATOR:
                return new Accelerator(x, y);
            case INHIBITOR:
                return new Inhibitor(x, y);
            case OBSTACLE:
                return new Obstacle(x, y);
            case LINE_CHANGER_UP:
                return new LineChanger(x, y, true);
            case LINE_CHANGER_DOWN:
                return new LineChanger(x, y, false);
            default:
                return null;
        }
    }

//    private List<Point> replaceShiftableByBike(List<Shiftable> shiftables){
//    }
}
