package com.codenjoy.dojo.kata.client;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.AbstractTextBoard;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 05.10.13
 * Time: 11:56
 */
public class YourSolverTest {

    private Solver ai;

    @Before
    public void setup() {
        ai = new YourSolver();
    }

    private ClientBoard board(String board) {
        return new Board().forString(board);
    }

    @Test
    public void should() {
        asertAI("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'hello',\n" +
                "  'questions':[\n" +
                "    'hello'\n" +
                "  ]\n" +
                "}", "message('['world']')");

        asertAI("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'world',\n" +
                "  'questions':[\n" +
                "    'hello',\n" +
                "    'world'\n" +
                "  ]\n" +
                "}", "message('['world', 'hello']')");

        asertAI("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'qwe',\n" +
                "  'questions':[\n" +
                "    'hello',\n" +
                "    'world',\n" +
                "    'qwe'\n" +
                "  ]\n" +
                "}", "message('['world', 'hello', 'qwe']')");

        asertAI("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':1,\n" +
                "  'nextQuestion':'question',\n" +
                "  'questions':[\n" +
                "    'question'\n" +
                "  ]\n" +
                "}", "message('['your answer']')");
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }
}
