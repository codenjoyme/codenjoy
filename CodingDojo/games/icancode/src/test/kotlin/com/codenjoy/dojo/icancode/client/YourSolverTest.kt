package com.codenjoy.dojo.icancode.client

import com.codenjoy.dojo.client.Direction
import com.codenjoy.dojo.client.Solver
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

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


class YourKotlinSolverTest() {
    private var ai: Solver<Board>? = null

    @Before
    fun setup() {
        ai = YourKotlinSolver()
    }

    private fun board(layer1: String, layer2: String): Board {
        return Board().forString(layer1, layer2) as Board
    }

    @Test
    fun should() {
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
                Direction.RIGHT)

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
                Direction.RIGHT)

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
                Direction.RIGHT)

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
                Direction.RIGHT)

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
                Direction.DOWN)

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
                Direction.DOWN)

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
                Direction.DOWN)

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
                Direction.DOWN)

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
                null)
    }

    private fun assertL(layer1: String, layer2: String, expected: Direction?) {
        val actual = ai!!.get(board(layer1, layer2))
        val expectedString = if (expected != null) expected.toString() else ""
        assertEquals(expectedString, actual)
    }
}

