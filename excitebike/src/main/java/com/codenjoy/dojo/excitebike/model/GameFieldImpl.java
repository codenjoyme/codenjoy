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

    //TODO remove player when bike is not alive and cross last possible x
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

        //TODO login for each element
    }

    public int size() {
        return mapParser.getXSize();
    }

    @Override
    public boolean isBorder(int x, int y) {
        Point point = pt(x, y);
//        return level.getBorders().contains(point);
        return false;
    }

    @Override
    public boolean isInhibitor(int x, int y) {
        return inhibitors.contains(pt(x, y));
    }

    @Override
    public boolean isAccelerator(int x, int y) {
        return accelerators.contains(pt(x, y));
    }

    @Override
    public boolean isObstacle(int x, int y) {
        return obstacles.contains(pt(x, y));
    }

    @Override
    public boolean isLineChanger(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public boolean isOffRoad(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    @Override
    public boolean isBike(int x, int y) {
        Point point = pt(x, y);
        return false;
    }

    public List<Bike> getBikes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    public List<Border> getBorders() {
        return borders;
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
        final int firstPossibleX = mapParser.getXSize() - 1;

        allShiftableElements.values().forEach(
                pointsOfElementType -> {pointsOfElementType.forEach(Shiftable::shift);
                    pointsOfElementType.removeIf(point -> point.getX() < lastPossibleX);}
        );

//        allShiftableElements.values().forEach(
//                pointsOfElementType -> pointsOfElementType.removeIf(point -> point.getX() < lastPossibleX)
//        );

        generateNewTrackStep(mapParser.getXSize(), firstPossibleX);
    }

    private void generateNewTrackStep(final int laneNumber, final int firstPossibleX) {
        int rndNonBorderElementOrdinal = dice.next(GameElementType.values().length - 3) + 2;
        int rndNonBorderLaneNumber = dice.next(laneNumber - 3) + 1;

        GameElementType randomType = GameElementType.values()[rndNonBorderElementOrdinal];
        List<Shiftable> elements = (List<Shiftable>) allShiftableElements.get(randomType);
        Shiftable newElement = getNewElement(randomType, firstPossibleX, rndNonBorderLaneNumber);
        elements.add(newElement);
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
                throw new IllegalArgumentException("No such element for " + randomType);
        }
    }
}