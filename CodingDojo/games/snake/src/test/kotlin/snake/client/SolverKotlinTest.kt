package com.codenjoy.dojo.snake.client

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


import com.codenjoy.dojo.client.Solver
import com.codenjoy.dojo.services.Dice
import com.codenjoy.dojo.services.Direction
import org.junit.Before
import org.junit.Test

import org.junit.Assert.assertEquals
import org.mockito.Matchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class SolverKotlinTest {

    private var dice: Dice? = null
    private var ai: Solver<Board>? = null

    @Before
    fun setup() {
        dice = mock(Dice::class.java)
        ai = YourSolver(dice)
    }

    private fun board(board: String): Board {
        return Board().forString(board) as Board
    }

    @Test
    fun should() {
        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼     ☼" +
                "☼ ☻▲  ☼" +
                "☼  ╙  ☼" +
                "☼☼☼☼☼☼☼", Direction.UP)

        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ☺  ☼" +
                "☼  ▲  ☼" +
                "☼ ☻╙  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼", Direction.UP)

        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ▲  ☼" +
                "☼  ╙  ☼" +
                "☼ ☻   ☼" +
                "☼    ☺☼" +
                "☼☼☼☼☼☼☼", Direction.UP)

    }

    private fun asertAI(board: String, expected: Direction) {
        val actual = ai!!.get(board(board))
        assertEquals(expected.toString(), actual)
    }

    private fun dice(direction: Direction) {
        `when`(dice!!.next(anyInt())).thenReturn(direction.value())
    }
}
