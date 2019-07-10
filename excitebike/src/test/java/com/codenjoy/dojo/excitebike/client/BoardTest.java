package com.codenjoy.dojo.excitebike.client;

import com.codenjoy.dojo.excitebike.model.items.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

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

/**
 * Created by Pavel Bobylev 6/14/2019
 */
public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void valueOf__shouldThrowIllegalArgumentException__ifElementWithGivenCharDoesntExist() {
        //given
        char givenChar = '%';
        Board board = new Board();

        //when
        board.valueOf(givenChar);

        //then
        //exception
    }

    @Test
    public void getMe__shouldReturnPointWithBikeElement() {
        //given
        Board board = toBoard("■■■■■" +
                " B > " +
                "  E |" +
                " e ▼ " +
                "■■■■■"
        );

        //when
        Point result = board.getMe();

        //then
        assertThat(result.getX(), is(1));
        assertThat(result.getY(), is(1));
    }

    @Test
    public void getMe__shouldReturnPointWithBikeFallenElement() {
        //given
        Board board = toBoard("b■■■■" +
                "   > " +
                "  E |" +
                " P ▼ " +
                "■■■■■"
        );

        //when
        Point result = board.getMe();

        //then
        assertThat(result.getX(), is(0));
        assertThat(result.getY(), is(0));
    }

    @Test
    public void isGameOver__shouldReturnTrueIfThereIsBikeFallenElement() {
        //given
        Board board = toBoard("■■■■■" +
                "   > " +
                "  E |" +
                " P ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.isGameOver();

        //then
        assertThat(result, is(true));
    }

    @Test
    public void isGameOver__shouldReturnFalseIfThereIsNoBikeFallenElement() {
        //given
        Board board = toBoard("■■■■■" +
                " B >I" +
                "A E |" +
                " P ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.isGameOver();

        //then
        assertThat(result, is(false));
    }

    @Test
    public void checkNearMe__shouldReturnTrueIfThereIsOneOfExpectedElementsNearMeAtRightDirection() {
        //given
        Board board = toBoard("■■■■■" +
                " B>  " +
                "  E |" +
                " P ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkNearMe(Direction.RIGHT, GameElementType.ACCELERATOR);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void checkNearMe__shouldReturnTrueIfThereIsOneOfExpectedElementsNearMeAtDownDirection() {
        //given
        Board board = toBoard("■■■■■" +
                " P   " +
                " E | " +
                " B ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkNearMe(Direction.UP, BikeType.OTHER_BIKE);

        //then
        assertThat(result, is(false));
    }

    @Test
    public void checkNearMe__shouldReturnTrueIfThereIsOneOfExpectedElementsNearMeAtLeftDirection() {
        //given
        Board board = toBoard("■■■■■" +
                "╝B>  " +
                " E | " +
                " P ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkNearMe(Direction.LEFT, SpringboardElementType.SPRINGBOARD_RIGHT_DOWN);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void checkNearMe__shouldReturnTrueIfThereIsOneOfExpectedElementsNearMeAtUpDirection() {
        //given
        Board board = toBoard("■■■■■" +
                "╝Q>  " +
                " E | " +
                " B ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkNearMe(Direction.DOWN, GameElementType.BORDER);

        //then
        assertThat(result, is(false));
    }

    @Test
    public void checkNearMe__shouldReturnTrueIfThereIsOneOfExpectedElementsNearMeAtRightRightUpDirection() {
        //given
        Board board = toBoard("■■■■■" +
                " B   " +
                "  E>|" +
                " P ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkNearMe(Lists.newArrayList(Direction.RIGHT, Direction.RIGHT, Direction.UP), GameElementType.ACCELERATOR);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void checkAtMe__shouldReturnTrue__ifAtMeIsGivenElement() {
        //given
        Board board = toBoard("■■■■■" +
                "╝Q>  " +
                " E | " +
                " A ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkAtMe(BikeType.BIKE_AT_ACCELERATOR);

        //then
        assertThat(result, is(true));
    }

    @Test
    public void checkAtMe__shouldReturnFalse__ifAtMeIsNotGivenElement() {
        //given
        Board board = toBoard("■■■■■" +
                "╝Q>  " +
                " E | " +
                " B ▼ " +
                "■■■■■"
        );

        //when
        boolean result = board.checkAtMe(BikeType.BIKE_AT_ACCELERATOR);

        //then
        assertThat(result, is(false));
    }

    private Board toBoard(String board) {
        return (Board) new Board().forString(board);
    }
}
