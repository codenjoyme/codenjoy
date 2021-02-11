package com.codenjoy.dojo.icancode.client.ai;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.icancode.client.Board;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Mikhail_Udalyi on 20.09.2016.
 */
public class AISolverTest {
    private Board board;
    private AISolver solver;
    private Dice dice = mock(Dice.class);

    public static Board board(String... boardString) {
        return (Board) new Board().forString(boardString);
    }

    @Before
    public void before() {
        board = board(
                "╔═════════┐" +
                "║....◄...S│" +
                "║.S.┌─╗...│" +
                "║...│ ║˄.$│" +
                "║.┌─┘ └─╗&│" +
                "║E│     ║.│" +
                "║.╚═┐ ╔═╝$│" +
                "║..O│ ║..O│" +
                "║...╚═╝...│" +
                "║O.$.....E│" +
                "└─────────┘",

                "-----------" +
                "------↑----" +
                "--☺--------" +
                "-----------" +
                "---------←-" +
                "-----------" +
                "---------→-" +
                "-------B-x-" +
                "--------↓--" +
                "--B---X^---" +
                "-----------",

                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------");
        solver = new AISolver(dice);
    }

    @Test
    public void shouldFindWay_withoutBarriers() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withBoxes() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-B----" +
                "--B---" +
                "------" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, DOWN, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withHoles() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║O...│" +
                "║O...│" +
                "║OOO$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withOtherRobots() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-X----" +
                "------" +
                "-^-&--" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Ignore // TODO разобраться
    @Test
    public void shouldFindWay_withRobots() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-☺----" +
                "------" +
                "-*-☻--" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Ignore // TODO разобраться
    @Test
    public void shouldFindWay_withRobotsFalling() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║O...│" +
                "║O...│" +
                "║OOO$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-x----" +
                "-o----" +
                "-xox--" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withRobotsOverBox() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-B----" +
                "--B---" +
                "------" +
                "------",

                "------" +
                "------" +
                "-☻----" +
                "--☺---" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, DOWN, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withLasers() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-←----" +
                "-→----" +
                "-↑↓---" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withLaserMachines() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║.$..│" +
                "║....│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-˂˃˄--" +
                "---˅--" +
                "------" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[RIGHT, RIGHT, RIGHT, DOWN, DOWN, DOWN, LEFT, LEFT, UP]", way.toString());
    }

    @Test
    public void shouldFindWay_withLaserMachinesCharged() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║.$..│" +
                "║....│" +
                "└────┘",

                "------" +
                "-☺----" +
                "-◄►▲--" +
                "---▼--" +
                "------" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[RIGHT, RIGHT, RIGHT, DOWN, DOWN, DOWN, LEFT, LEFT, UP]", way.toString());
    }

    @Test
    public void shouldFindWay_withStartEnd() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║S...│" +
                "║E...│" +
                "║...$│" +
                "└────┘",

                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------",

                "------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = solver.getShortestWay(board, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }
}
