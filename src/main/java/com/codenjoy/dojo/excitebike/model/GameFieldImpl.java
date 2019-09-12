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


import com.codenjoy.dojo.excitebike.model.items.Bike;
import com.codenjoy.dojo.excitebike.model.items.Fence;
import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.excitebike.services.SettingsHandler;
import com.codenjoy.dojo.excitebike.services.generation.GenerationOption;
import com.codenjoy.dojo.excitebike.services.generation.TrackStepGenerator;
import com.codenjoy.dojo.excitebike.services.generation.WeightedRandomBag;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_FALLEN;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.ACCELERATOR;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.INHIBITOR;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.OBSTACLE;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_LEFT;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_LEFT_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_LEFT_UP;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_RIGHT;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_RIGHT_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_RIGHT_UP;
import static com.codenjoy.dojo.excitebike.model.elements.SpringboardElementType.SPRINGBOARD_TOP;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;


public class GameFieldImpl implements GameField {

    private final MapParser mapParser;
    private final Map<CharElements, List<Shiftable>> allShiftableElements = new HashMap<>();
    private final List<Player> players = new LinkedList<>();
    private final List<Fence> fences;
    private final TrackStepGenerator trackStepGenerator;
    private final SettingsHandler settingsHandler;
    private final Dice dice;

    public GameFieldImpl(MapParser mapParser, Dice dice, SettingsHandler settingsHandler) {
        this.mapParser = mapParser;
        this.settingsHandler = settingsHandler;
        this.dice = dice;

        fences = mapParser.getFences();

        allShiftableElements.put(ACCELERATOR, new ArrayList<>(mapParser.getAccelerators()));
        allShiftableElements.put(INHIBITOR, new ArrayList<>(mapParser.getInhibitors()));
        allShiftableElements.put(OBSTACLE, new ArrayList<>(mapParser.getObstacles()));
        allShiftableElements.put(LINE_CHANGER_UP, new ArrayList<>(mapParser.getLineUpChangers()));
        allShiftableElements.put(LINE_CHANGER_DOWN, new ArrayList<>(mapParser.getLineDownChangers()));
        allShiftableElements.put(BIKE_FALLEN, new ArrayList<>());

        allShiftableElements.put(SPRINGBOARD_LEFT_UP, new ArrayList<>(mapParser.getSpringboardLeftUpElements()));
        allShiftableElements.put(SPRINGBOARD_RIGHT, new ArrayList<>(mapParser.getSpringboardLightElements()));
        allShiftableElements.put(SPRINGBOARD_LEFT_DOWN, new ArrayList<>(mapParser.getSpringboardLeftDownElements()));
        allShiftableElements.put(SPRINGBOARD_RIGHT_UP, new ArrayList<>(mapParser.getSpringboardRightUpElements()));
        allShiftableElements.put(SPRINGBOARD_LEFT, new ArrayList<>(mapParser.getSpringboardDarkElements()));
        allShiftableElements.put(SPRINGBOARD_RIGHT_DOWN, new ArrayList<>(mapParser.getSpringboardRightDownElements()));
        allShiftableElements.put(SPRINGBOARD_TOP, new ArrayList<>(mapParser.getSpringboardNoneElements()));

        this.trackStepGenerator = new TrackStepGenerator(dice, mapParser.getXSize(), mapParser.getYSize());
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        shiftTrack();
        generateNewTrackStep();

        players.forEach(player -> player.getHero().changeYDependsOnSpringboard());
        players.stream()
                .sorted((o1, o2) -> dice.next(3) - 1)
                .forEach(player -> player.getHero().tick());
        players.forEach(player -> player.getHero().setTicked(false));
        players.stream()
                .filter(player -> player.getHero().getX() < 0)
                .forEach(player -> player.newHero(this));
        allShiftableElements.put(BIKE_FALLEN, players.stream()
                .map(Player::getHero)
                .filter(h -> h != null && !h.isAlive())
                .collect(toList())
        );
    }

    public int xSize() {
        return mapParser.getXSize();
    }

    @Override
    public int ySize() {
        return mapParser.getYSize();
    }

    @Override
    public boolean isFence(int x, int y) {
        return y < 1 || y > mapParser.getYSize() - 2;
    }

    @Override
    public boolean isInhibitor(int x, int y) {
        return allShiftableElements.get(INHIBITOR).contains(pt(x, y));
    }

    @Override
    public boolean isAccelerator(int x, int y) {
        return allShiftableElements.get(ACCELERATOR).contains(pt(x, y));
    }

    @Override
    public boolean isObstacle(int x, int y) {
        return allShiftableElements.get(OBSTACLE).contains(pt(x, y));
    }

    @Override
    public boolean isUpLineChanger(int x, int y) {
        return allShiftableElements.get(LINE_CHANGER_UP).contains(pt(x, y));
    }

    @Override
    public boolean isDownLineChanger(int x, int y) {
        return allShiftableElements.get(LINE_CHANGER_DOWN).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardLeftOrDownElement(int x, int y) {
        return allShiftableElements.get(SPRINGBOARD_LEFT).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardRightElement(int x, int y) {
        return allShiftableElements.get(SPRINGBOARD_RIGHT).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardLeftDownElement(int x, int y) {
        return allShiftableElements.get(SPRINGBOARD_LEFT_DOWN).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardRightDownElement(int x, int y) {
        return allShiftableElements.get(SPRINGBOARD_RIGHT_DOWN).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardTopElement(int x, int y) {
        return allShiftableElements.get(SPRINGBOARD_TOP).contains(pt(x, y));
    }

    @Override
    public Optional<Bike> getEnemyBike(int x, int y, Player player) {
        return player != null ?
                players.parallelStream()
                        .map(Player::getHero)
                        .filter(bike -> bike != null && !bike.getPlayerName().equals(player.getHero().getPlayerName()) && bike.itsMe(x, y))
                        .findFirst()
                : Optional.empty();
    }

    @Override
    public Point findFreePosition() {
        Point result = findFreePosition(true);
        if (result == null) {
            result = findFreePosition(false);
        }
        if (result == null) {
            throw new RuntimeException("Game field is full, can't add more bikes!");
        }
        return result;
    }

    private Point findFreePosition(boolean chessOrder) {
        for (int xi = 0; xi < mapParser.getXSize(); xi++) {
            for (int yi = 1; yi < mapParser.getYSize() - 1; yi++) {
                if (chessOrder && (even(xi) && even(yi) || !even(xi) && !even(yi))) {
                    continue;
                }
                Point lowestPointAtColumn = new PointImpl(xi, 1);
                boolean atSpringboard = pointAtSpringboard(lowestPointAtColumn);
                Point spawnPlaceCandidate = new PointImpl(xi, atSpringboard ? yi + 1 : yi);
                if (isFree(spawnPlaceCandidate)) {
                    return spawnPlaceCandidate;
                }
            }
        }
        return null;
    }

    private boolean even(int number) {
        return number % 2 == 0;
    }

    private boolean pointAtSpringboard(Point point) {
        return allShiftableElements.get(SPRINGBOARD_LEFT).contains(point);
    }

    private boolean isFree(Point point) {
        Point nextPoint = new PointImpl(point.getX()+1, point.getY());
        return !getAliveBikes().contains(point)
                && !fences.contains(point)
                && !fences.contains(nextPoint)
                && !allShiftableElements.get(OBSTACLE).contains(point)
                && !allShiftableElements.get(OBSTACLE).contains(nextPoint)
                && !allShiftableElements.get(BIKE_FALLEN).contains(point)
                && !allShiftableElements.get(BIKE_FALLEN).contains(nextPoint);
    }

    public List<Bike> getAliveBikes() {
        return players.stream()
                .map(Player::getHero)
                .filter(b -> Objects.nonNull(b) && b.isAlive())
                .collect(toList());
    }

    public List<Fence> getFences() {
        return fences;
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
            player.newHero(this);
        }
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
                    addAll(GameFieldImpl.this.getAliveBikes());
                    addAll(GameFieldImpl.this.allShiftableElements.get(BIKE_FALLEN));
                    GameFieldImpl.this.allShiftableElements.entrySet()
                            .stream()
                            .filter(e -> e.getKey() != BIKE_FALLEN)
                            .forEach(e -> this.addAll(e.getValue()));
                    addAll(getFences());
                }};
            }
        };
    }

    private void shiftTrack() {
        final int lastPossibleX = 0;
        allShiftableElements.values().parallelStream().forEach(
                pointsOfElementType -> {
                    pointsOfElementType.forEach(Shiftable::shift);
                    pointsOfElementType.removeIf(point -> point.getX() < lastPossibleX);
                }
        );
    }

    private void generateNewTrackStep() {
        WeightedRandomBag<GenerationOption> weightedRandomBag = settingsHandler.getWeightedRandomBag();
        Map<? extends CharElements, List<Shiftable>> generated = trackStepGenerator.generate(weightedRandomBag);
        if (generated != null) {
            generated.forEach((key, elements) -> allShiftableElements.merge(key, elements, (currentElements, newElements) -> {
                        currentElements.addAll(newElements);
                        return currentElements;
                    }
            ));
        }
    }

    @Override
    public Player getPlayerOfBike(Bike bike) {
        return players.parallelStream()
                .filter(p -> p.getHero() != null && p.getHero().getPlayerName().equals(bike.getPlayerName()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void removeFallenBike(Bike bike) {
        allShiftableElements.get(BIKE_FALLEN).remove(bike);
    }
}