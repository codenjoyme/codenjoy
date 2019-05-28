package com.codenjoy.dojo.spacerace.client.ai;

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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.spacerace.client.Board;

public class AI3SolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new AI3Solver(dice);
    }

    private Board board(String board) {

        return (Board) new Board().forString(board);
    }

    @Test
    public void shouldUP() {
        assertA("☼   ☼" +
                "☼ 7 ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼"
                , Direction.UP);
    }

    @Test
    public void shouldRight() {
        assertA("☼   ☼" +
                "☼☺ 7☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.RIGHT);
    }

    @Test
    public void shouldLeft() {
        assertA("☼   ☼" +
                "☼7 ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.LEFT);
    }

    @Test
    public void shouldDown() {
        assertA("☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼  7☼" +
                "☼   ☼"
                , Direction.DOWN);
    }

    @Test
    public void shouldStopWhenStoneIsLeftUp() {
        assertA("☼ 0 ☼" +
                "☼7 ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.STOP);

        assertA("☼   ☼" +
                "☼70☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.LEFT);
        
        assertA("☼   ☼" +
                "☼7☺ ☼" +
                "☼ 0 ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.LEFT);
    }

    @Test
    public void shouldStopWhenStoneIsRightUp() {
        assertA("☼ 0 ☼" +
                "☼☺ 7☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.STOP);

        assertA("☼   ☼" +
                "☼☺07☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.RIGHT);

    }

    @Test
    public void shouldLeftWhenStoneIsUp() {
        assertA("☼ 7 ☼" +
                "☼   ☼" +
                "☼ 0 ☼" +
                "☼ ☺ ☼" +
                "☼   ☼"
                , Direction.LEFT);

        assertA("☼   ☼" +
                "☼ 7 ☼" +
                "☼   ☼" +
                "☼☺0 ☼" +
                "☼   ☼"
                , Direction.UP);
    }

    @Test
    public void shouldLeftWhenStoneIsUp2() {
        assertA("☼ 7 ☼" +
                "☼ 0 ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼"
                , Direction.LEFT);

        assertA("☼ 7 ☼" +
                "☼   ☼" +
                "☼ 0 ☼" +
                "☼☺  ☼" +
                "☼   ☼"
                , Direction.UP);
    }

    @Test
    public void shouldRightWhenRightAndBombIsUp() {
        assertA("☼  ♣  ☼" +
                "☼     ☼" +
                "☼    7☼" +
                "☼  ☺  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼"
                , Direction.RIGHT);

        assertA("☼     ☼" +
                "☼  ♣  ☼" +
                "☼    7☼" +
                "☼   ☺ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼"
                , Direction.RIGHT);

        assertA("☼     ☼" +
                "☼     ☼" +
                "☼  ♣ 7☼" +
                "☼    ☺☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼"
                , Direction.UP);


    }

    @Test
    public void shouldRightWhenUpAndBombIsUp() {
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼"
                , Direction.RIGHT);

        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ☺ ☼"
                , Direction.RIGHT);

        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼    ☺☼"
                , Direction.UP);
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣ ☺☼" +
                "☼     ☼"
                , Direction.UP);
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ☺☼" +
                "☼     ☼" +
                "☼  ♣  ☼"
                , Direction.UP);

    }

    @Ignore  // todo очень временно (доделать подсчет дистанций)
    @Test
    public void shouldLefttWhenUpAndBombIsUp2() {
        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼"
                , Direction.LEFT);

        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ☺   ☼"
                , Direction.LEFT);

        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼☺    ☼"
                , Direction.UP);
        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☺ ♣  ☼" +
                "☼     ☼"
                , Direction.UP);
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☺    ☼" +
                "☼     ☼" +
                "☼  ♣  ☼"
                , Direction.UP);

    }

    @Test
    public void shouldLefttWhenRightAndBombIsUp3() {
        assertA("☼    7☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ♣ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼"
                , Direction.LEFT);

        assertA("☼    7☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ♣ ☼" +
                "☼     ☼" +
                "☼ ☺   ☼"
                , Direction.UP);

        assertA("☼    7☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ☺ ♣ ☼" +
                "☼     ☼"
                , Direction.UP);

    }


    private void assertA(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
