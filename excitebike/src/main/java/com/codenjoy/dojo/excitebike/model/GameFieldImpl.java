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
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardGenerator;
import com.codenjoy.dojo.excitebike.services.Events;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.codenjoy.dojo.excitebike.model.items.GameElementType.*;
import static com.codenjoy.dojo.excitebike.model.items.bike.Bike.OTHER_BIKE_PREFIX;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.BIKE_FALLEN;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;


public class GameFieldImpl implements GameField {

    private Dice dice;
    private MapParser mapParser;
    private Map<CharElements, List<Shiftable>> allShiftableElements = new HashMap<>();
    private List<Player> players = new LinkedList<>();

    private List<Border> borders;

    private int generationLock;

    private final int SPRINGBOARD_GENERATION_CHANCE = 2;

    public GameFieldImpl(MapParser mapParser, Dice dice) {
        this.dice = dice;
        this.mapParser = mapParser;

        borders = mapParser.getBorders();

        allShiftableElements.put(ACCELERATOR, new ArrayList<>(mapParser.getAccelerators()));
        allShiftableElements.put(INHIBITOR, new ArrayList<>(mapParser.getInhibitors()));
        allShiftableElements.put(OBSTACLE, new ArrayList<>(mapParser.getObstacles()));
        allShiftableElements.put(LINE_CHANGER_UP, new ArrayList<>(mapParser.getLineUpChangers()));
        allShiftableElements.put(LINE_CHANGER_DOWN, new ArrayList<>(mapParser.getLineDownChangers()));
        allShiftableElements.put(BIKE_FALLEN, new ArrayList<>(mapParser.getFallenBikes()));

        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_LEFT_UP, new ArrayList<>(mapParser.getSpringboardLeftUpElements()));
        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_LIGHT, new ArrayList<>(mapParser.getSpringboardLightElements()));
        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_LEFT_DOWN, new ArrayList<>(mapParser.getSpringboardLeftDownElements()));
        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_RIGHT_UP, new ArrayList<>(mapParser.getSpringboardRightUpElements()));
        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_DARK, new ArrayList<>(mapParser.getSpringboardDarkElements()));
        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_RIGHT_DOWN, new ArrayList<>(mapParser.getSpringboardRightDownElements()));
        allShiftableElements.put(SpringboardElementType.SPRINGBOARD_NONE, new ArrayList<>(mapParser.getSpringboardNoneElements()));

    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        shiftTrack();
        players.forEach(player -> player.getHero().tick());
        players.forEach(player -> player.getHero().setTicked(false));
        if (players.stream().filter(Player::isAlive).count() <= 1 && players.size() > 1) {
            players.stream().filter(Player::isAlive).findFirst().ifPresent(player -> player.event(Events.WIN));
            restart();
        }
        allShiftableElements.put(BIKE_FALLEN, players.stream()
                .map(Player::getHero)
                .filter(h -> h != null && !h.isAlive())
                .collect(toList())
        );
    }

    private void restart() {
        players.forEach(player -> player.setHero(null));
        allShiftableElements.values().forEach(List::clear);
    }

    public int size() {
        return mapParser.getXSize();
    }

    @Override
    public boolean isBorder(int x, int y) {
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
    public boolean isSpringboardDarkElements(int x, int y) {
        return allShiftableElements.get(SpringboardElementType.SPRINGBOARD_DARK).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardLightElements(int x, int y) {
        return allShiftableElements.get(SpringboardElementType.SPRINGBOARD_LIGHT).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardLeftDownElements(int x, int y) {
        return allShiftableElements.get(SpringboardElementType.SPRINGBOARD_LEFT_DOWN).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardLeftUpElements(int x, int y) {
        return allShiftableElements.get(SpringboardElementType.SPRINGBOARD_LEFT_UP).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardRightDownElements(int x, int y) {
        return allShiftableElements.get(SpringboardElementType.SPRINGBOARD_RIGHT_DOWN).contains(pt(x, y));
    }

    @Override
    public boolean isSpringboardRightUpElements(int x, int y) {
        return allShiftableElements.get(SpringboardElementType.SPRINGBOARD_RIGHT_UP).contains(pt(x, y));
    }

    @Override
    public Optional<Bike> getEnemyBike(int x, int y, Player player) {
        return player != null ?
                players.parallelStream()
                        .map(Player::getHero)
                        .filter(bike -> bike.state(player).name().contains(OTHER_BIKE_PREFIX) && bike.itsMe(x, y))
                        .findFirst()
                : Optional.empty();
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Bike getNewFreeBike() {
        return createNewFreeBike(getBikesCountOnEachY());
    }

    private Map<Integer, Long> getBikesCountOnEachY() {
        Map<Integer, Long> bikesCountOnEachY = getBikes().stream().collect(
                groupingBy(
                        PointImpl::getY,
                        Collectors.counting()
                )
        );
        IntStream.range(1, mapParser.getYSize() - 1).forEach(value -> bikesCountOnEachY.putIfAbsent(value, 0L));
        return bikesCountOnEachY;
    }

    private int getYWithMinNumberOfBikes(Map<Integer, Long> bikesCountOnEachY) {
        return bikesCountOnEachY.entrySet()
                .stream()
                .min(
                        Comparator.comparingLong(Map.Entry::getValue)
                )
                .map(Map.Entry::getKey)
                .orElseGet(() -> 1);
    }

    private Bike createNewFreeBike(Map<Integer, Long> bikesCountOnEachY) {
        final int minPossibleX = 0;
        final int step = 3;
        int y = getYWithMinNumberOfBikes(bikesCountOnEachY);

        Bike newBike = new Bike(minPossibleX, y);

        for (int i = minPossibleX; i < mapParser.getXSize(); i += step) {
            newBike.setX(newBike.getY() % 2 == 0 ? i + 1 : i);
            if (isFree(newBike) || tryToSetFreeCoordinates(newBike, bikesCountOnEachY, i)) {
                break;
            }
            newBike.setY(1);
        }
        return newBike;
    }

    private boolean tryToSetFreeCoordinates(Bike bike, Map<Integer, Long> bikesOnYCount, int x) {
        return bikesOnYCount.entrySet()
                .stream()
                .sorted(comparingByValue())
                .map(Map.Entry::getKey)
                .anyMatch(y -> {
                    bike.setY(y);
                    bike.setX(y % 2 == 0 ? x + 1 : x);
                    return isFree(bike);
                });
    }

    private boolean isFree(Point point) {
        return !getBikes().contains(point) && !borders.contains(point) && !allShiftableElements.get(OBSTACLE).contains(point);
    }

    public List<Bike> getBikes() {
        return players.stream()
                .map(Player::getHero)
                .filter(Objects::nonNull)
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
        BoardReader reader = new BoardReader() {

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
        return reader;
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
        if (generationLock > 0) {
            generationLock--;
            return;
        }

        boolean needGenerate = dice.next(10) < 5;
        if (needGenerate) {
            boolean isSpringboard = dice.next(10) < SPRINGBOARD_GENERATION_CHANCE;
            if (isSpringboard) {
                final int clearLinesAroundSpringboard = 1;
                final int springboardTopMaXWidth = 5;
                int springboardWidth = dice.next(springboardTopMaXWidth) + 2;
                generationLock = springboardWidth + clearLinesAroundSpringboard * 2;
                SpringboardGenerator generator = new SpringboardGenerator(firstPossibleX + clearLinesAroundSpringboard, mapParser.getYSize(), springboardWidth);

                generator.getElements()
                        .forEach((key, elements) -> allShiftableElements.merge(key, elements, (currentElements, newElements) -> {
                                    currentElements.addAll(newElements);
                                    return currentElements;
                                }
                        ));

            } else {
                int rndNonBorderElementOrdinal = dice.next(values().length - 2) + 2;
                int rndNonBorderLaneNumber = dice.next(laneNumber - 2) + 1;

                CharElements randomType = GameElementType.values()[rndNonBorderElementOrdinal];
                List<Shiftable> elements = allShiftableElements.get(randomType);
                Shiftable newElement = getNewElement(randomType, firstPossibleX, rndNonBorderLaneNumber);
                elements.add(newElement);
            }
        }
    }

    private Shiftable getNewElement(CharElements randomType, int x, int y) {
        if (ACCELERATOR.equals(randomType)) {
            return new Accelerator(x, y);
        } else if (INHIBITOR.equals(randomType)) {
            return new Inhibitor(x, y);
        } else if (OBSTACLE.equals(randomType)) {
            return new Obstacle(x, y);
        } else if (LINE_CHANGER_UP.equals(randomType)) {
            return new LineChanger(x, y, true);
        } else if (LINE_CHANGER_DOWN.equals(randomType)) {
            return new LineChanger(x, y, false);
        }
        throw new IllegalArgumentException("No such element for " + randomType);
    }

    @Override
    public Player getPlayerOfBike(Bike bike) {
        return players.parallelStream().filter(p -> Objects.equals(p.getHero(), bike)).findFirst().orElse(null);
    }
}