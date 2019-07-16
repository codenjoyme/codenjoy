package com.codenjoy.dojo.excitebike.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.excitebike.client.Board;
import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.excitebike.model.items.GameElementType.ACCELERATOR;
import static com.codenjoy.dojo.excitebike.model.items.GameElementType.FENCE;
import static com.codenjoy.dojo.excitebike.model.items.GameElementType.LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.items.GameElementType.LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.items.GameElementType.OBSTACLE;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.BIKE_AT_LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.BIKE_AT_LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_AT_KILLED_BIKE;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_AT_INHIBITOR;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_AT_LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_AT_LINE_CHANGER_UP;
import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.RIGHT;
import static com.codenjoy.dojo.services.Direction.UP;

/**
 * Это алгоритм твоего бота. Он будет запускаться в игру с первым
 * зарегистрировавшимся игроком, чтобы ему не было скучно играть самому.
 * Реализуй его как хочешь, хоть на Random (только используй для этого
 * {@see Dice} что приходит через конструктор).
 * Для его запуска воспользуйся методом {@see ApofigSolver#main}
 */
public class AISolver implements Solver<Board> {

    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        if (board.isGameOver()) return "";
        Direction result = getCommand(board);
        result = invertIfNeeded(result);
        return result != null ? result.toString() : "";
    }

    private Direction invertIfNeeded(Direction original) {
        return (original == DOWN || original == UP) ? original.inverted() : original;
    }

    private Direction getCommand(Board board) {
        return checksChain(board,
                this::neutralizeOrForceLineChanging,
                this::evadeElementAtRight,
                this::pushOrEvadeUpDownBike
        );
    }


    @SafeVarargs
    private final Direction checksChain(Board board, Function<Board, Direction>... checks) {
        Direction command = null;
        for (Function<Board, Direction> check : checks) {
            command = check.apply(board);
            if (command != null) {
                return command;
            }
        }
        return command;
    }

    private Direction neutralizeOrForceLineChanging(Board board) {
        Direction toCheck = board.checkAtMe(BIKE_AT_LINE_CHANGER_UP) ? DOWN : board.checkAtMe(BIKE_AT_LINE_CHANGER_DOWN) ? UP : null;
        if (toCheck == null) {
            return null;
        }
        if (board.checkNearMe(Lists.newArrayList(toCheck, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))
                || board.checkNearMe(Lists.newArrayList(toCheck, RIGHT), OTHER_BIKE_AT_INHIBITOR)
                || board.checkNearMe(Lists.newArrayList(toCheck, RIGHT), ACCELERATOR)
                && board.checkNearMe(Lists.newArrayList(toCheck, RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OBSTACLE))) {
            if (board.checkNearMe(RIGHT, getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))) {
                if (!board.checkNearMe(Lists.newArrayList(toCheck, toCheck, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))) {
                    return toCheck;
                }
            } else {
                return invertIfNeeded(toCheck);
            }
        }
        return null;
    }

    private Direction pushOrEvadeUpDownBike(Board board) {
        boolean otherBikeBelowAliveAndShouldBePushed = isOtherBikeAliveAndShouldBePushed(board, UP);
        boolean otherBikeAboveAliveAndShouldBePushed = isOtherBikeAliveAndShouldBePushed(board, DOWN);
        if (otherBikeBelowAliveAndShouldBePushed && otherBikeAboveAliveAndShouldBePushed) {
            return randomUpDown();
        }
        if (otherBikeBelowAliveAndShouldBePushed) {
            return UP;
        }
        if (otherBikeAboveAliveAndShouldBePushed) {
            return DOWN;
        }
        if (isOtherBikeAliveAndShouldBeAvoided(board, UP)) {
            return DOWN;
        }
        if (isOtherBikeAliveAndShouldBeAvoided(board, DOWN)) {
            return UP;
        }
        return null;
    }

    private Direction randomUpDown() {
        return dice.next(2) == 1 ? UP : DOWN;
    }

    private boolean isOtherBikeAliveAndShouldBePushed(Board board, Direction direction) {
        return board.checkNearMe(direction, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX))
                && !isOtherBikeAliveAndShouldBeAvoided(board, direction)
                && !board.checkNearMe(direction, getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX))
                && !board.checkNearMe(Lists.newArrayList(direction, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OBSTACLE))
                && !board.checkNearMe(Lists.newArrayList(direction, RIGHT), OTHER_BIKE_AT_INHIBITOR)
                && !(board.checkNearMe(Lists.newArrayList(direction, RIGHT), ACCELERATOR)
                && (board.checkNearMe(Lists.newArrayList(direction, RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OBSTACLE))
                || board.checkNearMe(Lists.newArrayList(direction, RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX)))
        );
    }

    private boolean isOtherBikeAliveAndShouldBeAvoided(Board board, Direction direction) {
        return board.checkNearMe(direction, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX))
                && (direction == UP && board.checkNearMe(direction, OTHER_BIKE_AT_LINE_CHANGER_UP)
                || direction == DOWN && board.checkNearMe(direction, OTHER_BIKE_AT_LINE_CHANGER_DOWN)
                || direction == UP && board.checkNearMe(Lists.newArrayList(direction, RIGHT), LINE_CHANGER_UP)
                || direction == DOWN && board.checkNearMe(Lists.newArrayList(direction, RIGHT), LINE_CHANGER_DOWN));
    }

    private CharElements[] getBikeElementsBySuffixAndElements(String suffix, CharElements... elements) {
        BikeType[] bikeElementsBySuffix = Arrays.stream(BikeType.values())
                .filter(e -> e.name().contains(suffix))
                .collect(Collectors.toList())
                .toArray(new BikeType[]{});
        List<CharElements> bikeElementsList = new ArrayList<>(Arrays.asList(bikeElementsBySuffix));
        bikeElementsList.addAll(Arrays.asList(elements));
        return bikeElementsList.toArray(new CharElements[]{});
    }

    private Direction evadeElementAtRight(Board board) {
        Direction command = null;
        if (!board.checkAtMe(BIKE_AT_LINE_CHANGER_DOWN, BIKE_AT_LINE_CHANGER_UP) &&
                (board.checkNearMe(RIGHT, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX, OBSTACLE))
                        || board.checkNearMe(RIGHT, getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE))
                        || board.checkNearMe(RIGHT, LINE_CHANGER_DOWN)
                        && board.checkNearMe(Lists.newArrayList(UP, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))
                        || board.checkNearMe(RIGHT, LINE_CHANGER_UP)
                        && board.checkNearMe(Lists.newArrayList(DOWN, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))
                        || board.checkNearMe(RIGHT, ACCELERATOR)
                        && (board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE))
                        || board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX))
                        || board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT), LINE_CHANGER_DOWN)
                        && board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT, RIGHT, UP), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))
                        || board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT), LINE_CHANGER_UP)
                        && board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT, RIGHT, DOWN), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE)))
                        || board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE))
                        && board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT, UP), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))
                        && board.checkNearMe(Lists.newArrayList(RIGHT, RIGHT, DOWN), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE))
                )) {
            if (isVerticalDirectionClear(board, UP) && !isVerticalDirectionClear(board, DOWN)) {
                command = UP;
            } else if (isVerticalDirectionClear(board, DOWN) && !isVerticalDirectionClear(board, UP)) {
                command = DOWN;
            } else if (isVerticalDirectionClear(board, UP) && isVerticalDirectionClear(board, DOWN)) {
                command = randomUpDown();
            }
            return command;
        }
        return command;
    }

    private boolean isVerticalDirectionClear(Board board, Direction toCheck) {
        CharElements[] elementsToCheck = getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, FENCE, OBSTACLE);
        return !board.checkNearMe(toCheck, elementsToCheck) && !board.checkNearMe(toCheck, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX))
                && !board.checkNearMe(Lists.newArrayList(toCheck, RIGHT), elementsToCheck);
    }

}
