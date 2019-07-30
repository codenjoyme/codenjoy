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
import com.codenjoy.dojo.excitebike.model.elements.BikeType;
import com.codenjoy.dojo.excitebike.model.items.Bike;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_LEFT;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_LEFT_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_RIGHT;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.BIKE_AT_SPRINGBOARD_RIGHT_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.OTHER_BIKE_AT_INHIBITOR;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.OTHER_BIKE_AT_KILLED_BIKE;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.OTHER_BIKE_AT_LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.BikeType.OTHER_BIKE_AT_LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.ACCELERATOR;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.FENCE;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.OBSTACLE;
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
        result = invertVertically(result);
        return result != null ? result.toString() : "";
    }

    private Direction invertVertically(Direction original) {
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
                return invertVertically(toCheck);
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
        boolean otherBikeAtDirection = board.checkNearMe(direction, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX));
        boolean otherBikeIsAtOppositeLineChanger = board.checkNearMe(direction, direction == UP ? OTHER_BIKE_AT_LINE_CHANGER_UP : OTHER_BIKE_AT_LINE_CHANGER_DOWN);
        boolean otherBikeAliveAtDirectionAndShouldBeAvoided = isOtherBikeAliveAndShouldBeAvoided(board, direction);
        boolean fallenBikeAtDirection = board.checkNearMe(direction, getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX));
        boolean fallenBikeOrObstacleOrFenceNextAtDirection = board.checkNearMe(Lists.newArrayList(direction, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OBSTACLE, FENCE));
        boolean otherBikeAtInhibitorNextAtDirection = board.checkNearMe(Lists.newArrayList(direction, RIGHT), OTHER_BIKE_AT_INHIBITOR);
        boolean acceleratorNextAtDirection = board.checkNearMe(Lists.newArrayList(direction, RIGHT), ACCELERATOR);
        boolean otherOrFallenBikeOrObstacleNextAfterNextAtDirection = board.checkNearMe(Lists.newArrayList(direction, RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OBSTACLE))
                || board.checkNearMe(Lists.newArrayList(direction, RIGHT, RIGHT), getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX));
        boolean acceleratorNextAtDirectionAndOtherOrFallenBikeAfterIt = acceleratorNextAtDirection && otherOrFallenBikeOrObstacleNextAfterNextAtDirection;
        return otherBikeAtDirection
                && !otherBikeIsAtOppositeLineChanger
                && !otherBikeAliveAtDirectionAndShouldBeAvoided
                && !fallenBikeAtDirection
                && !fallenBikeOrObstacleOrFenceNextAtDirection
                && !otherBikeAtInhibitorNextAtDirection
                && !acceleratorNextAtDirectionAndOtherOrFallenBikeAfterIt;
    }

    private boolean isOtherBikeAliveAndShouldBeAvoided(Board board, Direction direction) {
        boolean directionIsDownAndOtherBikeAtLineChangerUpBelow = direction == UP && board.checkNearMe(direction, OTHER_BIKE_AT_LINE_CHANGER_UP);
        boolean directionIsUpAndOtherBikeAtLineChangerDownAbove = direction == DOWN && board.checkNearMe(direction, OTHER_BIKE_AT_LINE_CHANGER_DOWN);
        boolean directionIsDownAndOtherBikeInFrontOfLineChangerUpBelow = direction == UP && board.checkNearMe(Lists.newArrayList(direction, RIGHT), LINE_CHANGER_UP);
        boolean directionIsUpAndOtherBikeInFrontOfLineChangerDownAbove = direction == DOWN && board.checkNearMe(Lists.newArrayList(direction, RIGHT), LINE_CHANGER_DOWN);
        boolean oppositeDirectionIsClear = !isElementShouldBeAvoided(board, Lists.newArrayList(invertVertically(direction), RIGHT));
        return oppositeDirectionIsClear
                && (directionIsDownAndOtherBikeAtLineChangerUpBelow
                || directionIsUpAndOtherBikeAtLineChangerDownAbove
                || directionIsUpAndOtherBikeInFrontOfLineChangerDownAbove
                || directionIsDownAndOtherBikeInFrontOfLineChangerUpBelow);
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
        List<Direction> toCheck = getNextDirection(board);
        boolean nextElementShouldBeAvoided = isElementShouldBeAvoided(board, toCheck);
        if (nextElementShouldBeAvoided) {
            boolean upDirectionClear = isVerticalDirectionClear(board, UP);
            boolean downDirectionClear = isVerticalDirectionClear(board, DOWN);
            if (upDirectionClear && !downDirectionClear) {
                command = UP;
            } else if (downDirectionClear && !upDirectionClear) {
                command = DOWN;
            } else if (upDirectionClear) {
                command = randomUpDown();
            }
            return command;
        }
        return command;
    }

    private boolean isElementShouldBeAvoided(Board board, List<Direction> toCheck) {
        boolean atLineChanger = board.checkAtMe(BIKE_AT_LINE_CHANGER_DOWN, BIKE_AT_LINE_CHANGER_UP);
        boolean otherBikeOrObstacleOrFenceIsNext = board.checkNearMe(toCheck, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX, OBSTACLE, FENCE));
        boolean fallenBikeIsNext = board.checkNearMe(toCheck, getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX));
        boolean lineChangerDownAtRightAndSmthToAvoidAtRightAndDown = board.checkNearMe(toCheck, LINE_CHANGER_DOWN)
                && board.checkNearMe(plusDirection(toCheck, UP), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE));
        boolean lineChangerUpAtRightAndSmthToAvoidAtRightAndUp = board.checkNearMe(toCheck, LINE_CHANGER_UP)
                && board.checkNearMe(plusDirection(toCheck, DOWN), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE));
        List<Direction> nextAfter = plusDirection(toCheck, RIGHT);
        boolean smthToAvoidInTwoSteps = board.checkNearMe(nextAfter, getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE));
        boolean otherBikeInTwoSteps = board.checkNearMe(nextAfter, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX));
        boolean lineChangerDownInTwoStepsAndSmthToAvoidInTwoStepsAndDown = board.checkNearMe(nextAfter, LINE_CHANGER_DOWN)
                && board.checkNearMe(plusDirection(toCheck, RIGHT, RIGHT, UP), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE));
        boolean lineChangerUpInTwoStepsAndSmthToAvoidInTwoStepsAndUp = board.checkNearMe(nextAfter, LINE_CHANGER_UP)
                && board.checkNearMe(plusDirection(toCheck, RIGHT, RIGHT, DOWN), getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, OBSTACLE, FENCE));
        boolean acceleratorAndSmthToAvoidInTwoSteps = board.checkNearMe(toCheck, ACCELERATOR)
                && (smthToAvoidInTwoSteps
                || otherBikeInTwoSteps
                || lineChangerDownInTwoStepsAndSmthToAvoidInTwoStepsAndDown
                || lineChangerUpInTwoStepsAndSmthToAvoidInTwoStepsAndUp);
        return !atLineChanger
                && (otherBikeOrObstacleOrFenceIsNext
                || fallenBikeIsNext
                || lineChangerDownAtRightAndSmthToAvoidAtRightAndDown
                || lineChangerUpAtRightAndSmthToAvoidAtRightAndUp
                || acceleratorAndSmthToAvoidInTwoSteps
                || smthToAvoidInTwoSteps);
    }

    private List<Direction> plusDirection(List<Direction> original, Direction... toAdd) {
        List<Direction> result = new ArrayList<>(original);
        result.addAll(Arrays.asList(toAdd));
        return result;
    }

    private ArrayList<Direction> getNextDirection(Board board) {
        boolean atSpringboardLeft = board.checkAtMe(BIKE_AT_SPRINGBOARD_LEFT) || board.checkAtMe(BIKE_AT_SPRINGBOARD_LEFT_DOWN);
        boolean atSpringboardRight = board.checkAtMe(BIKE_AT_SPRINGBOARD_RIGHT) || board.checkAtMe(BIKE_AT_SPRINGBOARD_RIGHT_DOWN);
        return atSpringboardLeft ? Lists.newArrayList(RIGHT, UP) : atSpringboardRight ? Lists.newArrayList(RIGHT, DOWN) : Lists.newArrayList(RIGHT);
    }

    private boolean isVerticalDirectionClear(Board board, Direction toCheck) {
        CharElements[] elementsToCheck = getBikeElementsBySuffixAndElements(Bike.FALLEN_BIKE_SUFFIX, OTHER_BIKE_AT_KILLED_BIKE, FENCE, OBSTACLE);
        boolean fallenOrOtherBikeAtKilledOrFenceOrObstacleAtDirection = board.checkNearMe(toCheck, elementsToCheck);
        boolean otherBikeAtDirection = board.checkNearMe(toCheck, getBikeElementsBySuffixAndElements(Bike.OTHER_BIKE_PREFIX));
        boolean fallenOrOtherBikeAtKilledOrFenceOrObstacleNextAtDirection = board.checkNearMe(Lists.newArrayList(toCheck, RIGHT), elementsToCheck);
        boolean pointExistsAtDirection = !board.isOutOfFieldRelativeToMe(toCheck);
        return pointExistsAtDirection
                && !fallenOrOtherBikeAtKilledOrFenceOrObstacleAtDirection
                && !otherBikeAtDirection
                && !fallenOrOtherBikeAtKilledOrFenceOrObstacleNextAtDirection;
    }

}
