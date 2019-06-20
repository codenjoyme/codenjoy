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

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;


//TODO remove player when bike is not alive and cross last possible x TASK - 31-make-correct-bike-lose-and-game-finish-logic
public class GameFieldImpl implements GameField {

    private Dice dice;
    private MapParser mapParser;

    private Map<GameElementType, List<? extends Shiftable>> allShiftableElements = new EnumMap<>(GameElementType.class);

    private List<Player> players = new LinkedList<>();

    private List<Border> borders;

    public GameFieldImpl(MapParser mapParser, Dice dice) {
        this.dice = dice;
        this.mapParser = mapParser;

        borders = mapParser.getBorders();

        allShiftableElements.put(GameElementType.ACCELERATOR, mapParser.getAccelerators());
        allShiftableElements.put(GameElementType.INHIBITOR, mapParser.getInhibitors());
        allShiftableElements.put(GameElementType.OBSTACLE, mapParser.getObstacles());
        allShiftableElements.put(GameElementType.LINE_CHANGER_UP, mapParser.getLineUpChangers());
        allShiftableElements.put(GameElementType.LINE_CHANGER_DOWN, mapParser.getLineDownChangers());
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        shiftTrack();
        players.forEach(player -> player.getHero().tick());
        players.forEach(player -> player.getHero().setTicked(false));
    }

    public int size() {
        return mapParser.getXSize();
    }

    @Override
    public boolean isBorder(int x, int y) {
        return y < 1 || y > mapParser.getYSize()-2;
    }

    @Override
    public boolean isInhibitor(int x, int y) {
        return allShiftableElements.get(GameElementType.INHIBITOR).contains(pt(x, y));
    }

    @Override
    public boolean isAccelerator(int x, int y) {
        return allShiftableElements.get(GameElementType.ACCELERATOR).contains(pt(x, y));
    }

    @Override
    public boolean isObstacle(int x, int y) {
        return allShiftableElements.get(GameElementType.OBSTACLE).contains(pt(x, y));
    }

    @Override
    public boolean isUpLineChanger(int x, int y) {
        return allShiftableElements.get(GameElementType.LINE_CHANGER_UP).contains(pt(x,y));
    }

    @Override
    public boolean isDownLineChanger(int x, int y) {
        return allShiftableElements.get(GameElementType.LINE_CHANGER_DOWN).contains(pt(x,y));
    }

    @Override
    public Optional<Bike> getEnemyBike(int x, int y) {
        return players.parallelStream().map(Player::getHero).filter(bike -> bike.itsMe(x, y)).findFirst();
    }

    @Override
    public int getPlayersNumber() {
        return players.size();
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
                    GameFieldImpl.this.allShiftableElements.values().forEach(this::addAll);
                    addAll(getBorders());
                }};
            }
        };
    }

    private void shiftTrack() {
        final int lastPossibleX = 0;
        final int firstPossibleX = mapParser.getXSize() - 1;

        allShiftableElements.values().parallelStream().forEach(
                pointsOfElementType -> {
                    pointsOfElementType.forEach(Shiftable::shift);
                    pointsOfElementType.removeIf(point -> point.getX() < lastPossibleX);
                }
        );

        generateNewTrackStep(mapParser.getXSize(), firstPossibleX);
    }

    private void generateNewTrackStep(final int laneNumber, final int firstPossibleX) {
        boolean needGenerate = dice.next(10) < 5;
        if (needGenerate) {
            int rndNonBorderElementOrdinal = dice.next(GameElementType.values().length - 3) + 2;
            int rndNonBorderLaneNumber = dice.next(laneNumber - 3) + 1;

            GameElementType randomType = GameElementType.values()[rndNonBorderElementOrdinal];
            List<Shiftable> elements = (List<Shiftable>) allShiftableElements.get(randomType);
            Shiftable newElement = getNewElement(randomType, firstPossibleX, rndNonBorderLaneNumber);
            elements.add(newElement);
        }
    }

    private Shiftable getNewElement(GameElementType randomType, int x, int y) {
        switch (randomType) {
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