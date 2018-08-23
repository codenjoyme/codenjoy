package com.codenjoy.dojo.reversi.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.reversi.client.AbstractSolverTest;
import org.junit.Test;

public class AISolverTest extends AbstractSolverTest {

    @Override
    protected Solver getSolver() {
        return new AISolver(dice);
    }

    @Test
    public void should() {
        asertAI("    " +
                "+xO " +
                " Ox " +
                "    ");

        asertAI("    " +
                "ooo " +
                "+oX " +
                "    ");

        asertAI("    " +
                "OOO " +
                "xxx " +
                "+   ");

        asertAI("+   " +
                "ooo " +
                "ooX " +
                "o   ");

        asertAI("x+  " +
                "OxO " +
                "OOx " +
                "O   ");

        asertAI("Xo+ " +
                "ooo " +
                "ooX " +
                "o   ");

        asertAI("xxx " +
                "OOx " +
                "OOx " +
                "O  +");

        asertAI("XXX " +
                "ooX " +
                "ooo " +
                "o+ o");

        asertAI("xxx " +
                "Oxx " +
                "OxO " +
                "Ox+O");

        asertAI("XXX " +
                "oXX " +
                "ooo " +
                "oooo");

        asertAI("xxx " +
                "Oxx+" +
                "OOO " +
                "OOOO");

        asertAI("XXX " +
                "oooo" +
                "ooo+" +
                "oooo");

        asertAI("xxx+" +
                "OOxO" +
                "OOOx" +
                "OOOO");

        asertAI("XXXo" +
                "oooo" +
                "oooX" +
                "oooo");
    }

}
