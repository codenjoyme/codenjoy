package com.codenjoy.dojo.sudoku.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.sudoku.client.Board;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ApofigSolverTest {

    private Solver ai;
    private Board board;

    @Before
    public void setup() {
        ai = new ApofigSolver(new RandomDice());
    }

    private void shBoard(String boardString) {
        this.board = (Board) new Board().forString(boardString);
    }

    @Test
    public void should() {
        shBoard("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(5, 5, 5);
        assertNext(2, 5, 2);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼42 ☼853☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(10, "[(4:6=9), (4:4=7), (5:7=3), (4:7=5), (5:3=4), (6:3=2), (1:3=1), (1:7=9), (4:1=6), (4:3=3)]");
        assertNext(10, "[(4:9=2), (1:9=3), (1:8=2), (3:8=7), (2:8=8), (6:1=8), (6:7=7), (6:9=6), (8:5=9), (3:5=6)]");
        assertNext(10, "[(7:5=7), (7:3=5), (7:4=4), (6:4=1), (2:4=5), (2:6=1), (2:9=4), (2:2=7), (3:4=9), (3:6=3)]");
        assertNext(10, "[(3:7=1), (3:9=5), (6:6=4), (7:6=8), (7:2=3), (7:8=6), (7:9=1), (7:1=9), (8:4=2), (8:2=4)]");

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼678☼9  ☼" +
                "☼67 ☼195☼34 ☼" +
                "☼198☼342☼56 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼8 6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼28 ☼" +
                "☼287☼419☼6 5☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        assertNext(9, "[(3:2=2), (3:1=4), (8:1=1), (8:6=5), (8:8=3), (9:1=2), (9:2=8), (9:3=7), (9:7=4)]");

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼534☼678☼912☼" +
                "☼672☼195☼348☼" +
                "☼198☼342☼567☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼859☼761☼423☼" +
                "☼426☼853☼791☼" +
                "☼713☼924☼856☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼961☼537☼284☼" +
                "☼287☼419☼635☼" +
                "☼345☼286☼179☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    public static String assertE(String expected) {
        int size = (int) Math.sqrt(expected.length());
        return inject(expected, size, "\n");
    }

    public static String inject(String string, int position, String substring) {
        StringBuilder result = new StringBuilder();
        for (int index = 1; index < string.length() / position + 1; index++) {
            result.append(string.substring((index - 1)*position, index*position)).append(substring);
        }
        result.append(string.substring((string.length() / position) * position, string.length()));
        return result.toString();
    }

    private void asrtBrd(String expected) {
        assertEquals(assertE(expected), board.toString());
    }

    private void assertNext(int x, int y, int n) {
        String actual = ai.get(board);
        assertEquals(String.format("ACT(%s,%s,%s)", x, y, n), actual);
        board.set(x, y, n);
    }

    private void assertNext(int count, String expected) {
        List<String> result = new LinkedList<String>();
        for (int i = 0; i < count; i++) {
            String actual = ai.get(board);
            if (actual.length() == 0) break;
            int x = Integer.valueOf("" + actual.charAt(4));
            int y = Integer.valueOf("" + actual.charAt(6));
            int n = Integer.valueOf("" + actual.charAt(8));
            result.add(String.format("(%s:%s=%s)", x, y, n));
            board.set(x, y, n);
        }
        assertEquals(expected, result.toString());
    }

}
