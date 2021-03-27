package com.codenjoy.dojo.bomberman.services;

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


import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.bomberman.services.GameSettings.Keys.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PerformanceTest {

    @Test
    public void test() {
        int boardSize = 100;
        int walls = 600;
        int meatChoppers = 100;
        int players = 100;
        int ticks = 1;

        Profiler p = new Profiler();
        p.start();

        GameRunner runner = new GameRunner();
        runner.getSettings()
                .integer(BOARD_SIZE, boardSize)
                .integer(DESTROY_WALL_COUNT, walls)
                .integer(MEAT_CHOPPERS_COUNT, meatChoppers);

        PrinterFactory factory = new PrinterFactoryImpl();

        List<Game> games = new LinkedList<>();
        for (int i = 0; i < players; i++) {
            games.add(TestUtils.buildGame(runner, mock(EventListener.class), factory));
        }

        p.done("creation");

        for (int i = 0; i < ticks; i++) {
            games.get(0).getField().tick();
            p.done("tick");

            for (int j = 0; j < games.size(); j++) {
                games.get(j).getBoardAsString();
            }
            p.done("print");
        }

        p.print();

//        assertLess(p.get("creation"), 1000);
//        assertLess(p.get("print"), 600);
//        assertLess(p.get("tick"), 600);

    }

    private void assertLess(long actual, int expected) {
        assertTrue(actual + " > " + expected, actual < expected);
    }
}
