package com.codenjoy.dojo.expansion.client

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
import com.codenjoy.dojo.services.QDirection
import com.codenjoy.dojo.expansion.model.Forces
import com.codenjoy.dojo.expansion.model.ForcesMoves
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

import com.codenjoy.dojo.services.PointImpl.pt
import com.codenjoy.dojo.expansion.client.Command.*
import junit.framework.Assert.assertEquals
import org.mockito.Mockito.mock

class YourSolverTest {

    private var ai: Solver<*>? = null

    @Before
    fun setup() {
        ai = YourSolver()
    }

    private fun board(json: String, layer1: String, layer2: String): Board {
        val board = Board().forString(layer1, layer2) as Board
        board.setSource(JSONObject(json))
        return board
    }

    @Test
    fun should() {
        assertL(
                "{'myBase':{'x':1,'y':5}," +
                        "'myColor':0," +
                        "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#'" +
                        "}",
                "╔═════┐" +
                "║1...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "-♥-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(1, 5), 10)).move(ForcesMoves(pt(1, 5), 5, QDirection.RIGHT)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "-♥♥----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(2, 5), 10)).move(ForcesMoves(pt(2, 5), 5, QDirection.RIGHT)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "-♥♥♥---" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(3, 5), 10)).move(ForcesMoves(pt(3, 5), 5, QDirection.RIGHT)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005005-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1...$│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....F│" +
                "└─────┘",
                "-------" +
                "-♥♥♥♥--" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(4, 5), 10)).move(ForcesMoves(pt(4, 5), 5, QDirection.RIGHT)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005005005-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-♥♥♥♥♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(5, 5), 10)).move(ForcesMoves(pt(5, 5), 5, QDirection.DOWN)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005005005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-♥♥♥♥♥-" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(5, 4), 10)).move(ForcesMoves(pt(5, 4), 5, QDirection.DOWN)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005005005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-♥♥♥♥♥-" +
                "-----♥-" +
                "-----♥-" +
                "-------" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(5, 3), 10)).move(ForcesMoves(pt(5, 3), 5, QDirection.DOWN)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005005005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-♥♥♥♥♥-" +
                "-----♥-" +
                "-----♥-" +
                "-----♥-" +
                "-------" +
                "-------",
                Command.increase(Forces(pt(5, 2), 10)).move(ForcesMoves(pt(5, 2), 5, QDirection.DOWN)).build())

        assertL("{'myBase':{'x':1,'y':5}," +
                "'myColor':0," +
                "'forces':'" +
                "-=#-=#-=#-=#-=#-=#-=#" +
                "-=#00F005005005005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#005-=#" +
                "-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════┐" +
                "║1....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║....E│" +
                "└─────┘",
                "-------" +
                "-♥♥♥♥♥-" +
                "-----♥-" +
                "-----♥-" +
                "-----♥-" +
                "-----♥-" +
                "-------",
                doNothing())
    }

    private fun assertL(json: String, layer1: String, layer2: String, expected: Command) {
        val actual = ai!!.get(board(json, layer1, layer2))
        assertEquals(String.format("message('%s')", expected.toString()), actual)
    }
}