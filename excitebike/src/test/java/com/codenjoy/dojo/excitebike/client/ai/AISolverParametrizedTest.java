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

import com.codenjoy.dojo.excitebike.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Random;

import static com.codenjoy.dojo.excitebike.model.items.GameElementType.BORDER;
import static com.codenjoy.dojo.excitebike.model.items.GameElementType.NONE;
import static com.codenjoy.dojo.excitebike.model.items.GameElementType.OBSTACLE;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_FALLEN;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_INCLINE_LEFT;
import static com.codenjoy.dojo.excitebike.model.items.bike.BikeType.OTHER_BIKE_INCLINE_RIGHT;
import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.STOP;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AISolverParametrizedTest {

    private Dice dice;
    private AISolver solver;
    private char elementAtRight;
    private char elementAbove;
    private char elementBelow;
    private Direction expectedDirection;

    public AISolverParametrizedTest(CharElements elementAtRight, CharElements elementAbove, CharElements elementBelow, Direction expectedDirection) {
        this.elementAtRight = elementAtRight.ch();
        this.elementAbove = elementAbove.ch();
        this.elementBelow = elementBelow.ch();
        this.expectedDirection = expectedDirection;
        dice = mock(Dice.class);
        solver = new AISolver(dice);
    }

    @Parameterized.Parameters(name = "Element to be evaded: {0}, element above: {1}, element below: {2}, expected direction: {3}")
    public static List<Object[]> data() {
        return Lists.newArrayList(
                // avoid obstacle - random choice
                new Object[]{OBSTACLE, NONE, NONE, null},

                // avoid obstacle - choose not border
                new Object[]{OBSTACLE, NONE, BORDER, DOWN},
                new Object[]{OBSTACLE, OTHER_BIKE, BORDER, DOWN},
                new Object[]{OBSTACLE, OTHER_BIKE_INCLINE_LEFT, BORDER, DOWN},
                new Object[]{OBSTACLE, OTHER_BIKE_INCLINE_RIGHT, BORDER, DOWN},

                // no way to survive
                new Object[]{OBSTACLE, OTHER_BIKE_FALLEN, BORDER, STOP},

                // avoid obstacle - choose not border
                new Object[]{OBSTACLE, BORDER, NONE, UP},
                new Object[]{OBSTACLE, BORDER, OTHER_BIKE, UP},
                new Object[]{OBSTACLE, BORDER, OTHER_BIKE_INCLINE_LEFT, UP},
                new Object[]{OBSTACLE, BORDER, OTHER_BIKE_INCLINE_RIGHT, UP},

                // no way to survive
                new Object[]{OBSTACLE, BORDER, OTHER_BIKE_FALLEN, STOP},

                // avoid other bike - random choice
                new Object[]{OTHER_BIKE, NONE, NONE, null},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE, NONE, BORDER, DOWN},
                new Object[]{OTHER_BIKE, OTHER_BIKE, BORDER, DOWN},
                new Object[]{OTHER_BIKE, OTHER_BIKE_INCLINE_LEFT, BORDER, DOWN},
                new Object[]{OTHER_BIKE, OTHER_BIKE_INCLINE_RIGHT, BORDER, DOWN},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE, OTHER_BIKE_FALLEN, BORDER, STOP},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE, BORDER, NONE, UP},
                new Object[]{OTHER_BIKE, BORDER, OTHER_BIKE, UP},
                new Object[]{OTHER_BIKE, BORDER, OTHER_BIKE_INCLINE_LEFT, UP},
                new Object[]{OTHER_BIKE, BORDER, OTHER_BIKE_INCLINE_RIGHT, UP},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE, BORDER, OTHER_BIKE_FALLEN, STOP},

                // avoid other bike - random choice
                new Object[]{OTHER_BIKE_INCLINE_LEFT, NONE, NONE, null},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE_INCLINE_LEFT, NONE, BORDER, DOWN},
                new Object[]{OTHER_BIKE_INCLINE_LEFT, OTHER_BIKE, BORDER, DOWN},
                new Object[]{OTHER_BIKE_INCLINE_LEFT, OTHER_BIKE_INCLINE_LEFT, BORDER, DOWN},
                new Object[]{OTHER_BIKE_INCLINE_LEFT, OTHER_BIKE_INCLINE_RIGHT, BORDER, DOWN},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE_INCLINE_LEFT, OTHER_BIKE_FALLEN, BORDER, STOP},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE_INCLINE_LEFT, BORDER, NONE, UP},
                new Object[]{OTHER_BIKE_INCLINE_LEFT, BORDER, OTHER_BIKE, UP},
                new Object[]{OTHER_BIKE_INCLINE_LEFT, BORDER, OTHER_BIKE_INCLINE_LEFT, UP},
                new Object[]{OTHER_BIKE_INCLINE_LEFT, BORDER, OTHER_BIKE_INCLINE_RIGHT, UP},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE_INCLINE_LEFT, BORDER, OTHER_BIKE_FALLEN, STOP},

                // avoid other bike - random choice
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, NONE, NONE, null},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, NONE, BORDER, DOWN},
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, OTHER_BIKE, BORDER, DOWN},
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, OTHER_BIKE_INCLINE_LEFT, BORDER, DOWN},
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, OTHER_BIKE_INCLINE_RIGHT, BORDER, DOWN},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, OTHER_BIKE_FALLEN, BORDER, STOP},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, BORDER, NONE, UP},
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, BORDER, OTHER_BIKE, UP},
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, BORDER, OTHER_BIKE_INCLINE_LEFT, UP},
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, BORDER, OTHER_BIKE_INCLINE_RIGHT, UP},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE_INCLINE_RIGHT, BORDER, OTHER_BIKE_FALLEN, STOP},

                // avoid other bike - random choice
                new Object[]{OTHER_BIKE_FALLEN, NONE, NONE, null},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE_FALLEN, NONE, BORDER, DOWN},
                new Object[]{OTHER_BIKE_FALLEN, OTHER_BIKE, BORDER, DOWN},
                new Object[]{OTHER_BIKE_FALLEN, OTHER_BIKE_INCLINE_LEFT, BORDER, DOWN},
                new Object[]{OTHER_BIKE_FALLEN, OTHER_BIKE_INCLINE_RIGHT, BORDER, DOWN},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE_FALLEN, OTHER_BIKE_FALLEN, BORDER, STOP},

                // avoid other bike - choose not border
                new Object[]{OTHER_BIKE_FALLEN, BORDER, NONE, UP},
                new Object[]{OTHER_BIKE_FALLEN, BORDER, OTHER_BIKE, UP},
                new Object[]{OTHER_BIKE_FALLEN, BORDER, OTHER_BIKE_INCLINE_LEFT, UP},
                new Object[]{OTHER_BIKE_FALLEN, BORDER, OTHER_BIKE_INCLINE_RIGHT, UP},

                // no way to avoid other bike / change the line
                new Object[]{OTHER_BIKE_FALLEN, BORDER, OTHER_BIKE_FALLEN, STOP},

                // hit the bike
                new Object[]{NONE, OTHER_BIKE, NONE, DOWN},
                new Object[]{NONE, OTHER_BIKE_INCLINE_LEFT, NONE, DOWN},
                new Object[]{NONE, OTHER_BIKE_INCLINE_RIGHT, NONE, DOWN},
                new Object[]{NONE, NONE, OTHER_BIKE, UP},
                new Object[]{NONE, NONE, OTHER_BIKE_INCLINE_LEFT, UP},
                new Object[]{NONE, NONE, OTHER_BIKE_INCLINE_RIGHT, UP}

                // incline the bike according to the springboard
                //TODO implement after Springboard task (#26)
                //new Object[]{SpringboardType.LEFT_DOWN, GameElementType.NONE, GameElementType.NONE, Direction.LEFT},
                //new Object[]{SpringboardType.RIGHT_DOWN, GameElementType.NONE, GameElementType.NONE, Direction.RIGHT}
        );
    }

    @Test
    public void get__shouldReturnAppropriateDirection__accordingToGameElementTypeAround() {
        //given
        Board board = toBoard("■■■■■" +
                        "  " + elementBelow + "  " +
                        "  o" + elementAtRight + " " +
                        "  " + elementAbove + "  " +
                        "■■■■■"
        );
        if (expectedDirection == null) {
            boolean randomBool = new Random().nextBoolean();
            when(dice.next(2)).thenReturn(randomBool ? 1 : 0);
            expectedDirection = randomBool ? DOWN : UP;
        }

        //when
        String result = solver.get(board);

        //then
        assertThat(result, is(expectedDirection.toString()));
    }

    private Board toBoard(String board) {
        return (Board) new Board().forString(board);
    }

}
