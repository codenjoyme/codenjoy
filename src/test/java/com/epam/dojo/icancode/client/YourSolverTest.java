package com.epam.dojo.icancode.client;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class YourSolverTest {

    private Solver ai;

    @Before
    public void setup() {
        ai = new YourSolver();
    }

    private Board board(String layer1, String layer2) {
        return (Board) new Board().forString(layer1, layer2);
    }

    @Test
    public void should() {
        assertL("╔═════┐" +
                "║S...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Direction.RIGHT);

        assertL("╔═════┐" +
                "║S...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Direction.RIGHT);

        assertL("╔═════┐" +
                "║S...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Direction.RIGHT);

        assertL("╔═════┐" +
                "║S...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "----☺--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Direction.RIGHT);

        assertL("╔═════┐" +
                "║S....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Direction.DOWN);

        assertL("╔═════┐" +
                "║S....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-------" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Direction.DOWN);

        assertL("╔═════┐" +
                "║S....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-------" +
                "-------" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------",
                Direction.DOWN);

        assertL("╔═════┐" +
                "║S....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-----☺-" +
                "-------" +
                "-------",
                Direction.DOWN);

        assertL("╔═════┐" +
                "║S....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-----☺-" +
                "-------",
                null);
    }

    private void assertL(String layer1, String layer2, Direction expected) {
        String actual = ai.get(board(layer1, layer2));
        String expectedString = (expected != null) ? expected.toString() : "";
        assertEquals(expectedString, actual);
    }
}