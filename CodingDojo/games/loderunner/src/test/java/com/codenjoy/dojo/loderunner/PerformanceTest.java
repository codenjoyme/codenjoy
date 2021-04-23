package com.codenjoy.dojo.loderunner;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.loderunner.services.GameRunner;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ENEMIES_COUNT;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class PerformanceTest {

    private Profiler profiler;

    @Test
    public void test() {

        // about 30 sec
        int enemies = 4;
        int players = 100;
        int ticks = 100;

        profiler = new Profiler(){{
            PRINT_SOUT = true;
        }};
        profiler.start();

        GameRunner runner = new GameRunner(){
            @Override
            public GameSettings getSettings() {
                return super.getSettings()
                    .integer(ENEMIES_COUNT, enemies);
            }
        };

        PrinterFactory factory = new PrinterFactoryImpl();

        List<Game> games = TestUtils.getGames(players, runner,
                factory, () -> mock(EventListener.class));

        profiler.done("creation");

        for (int i = 0; i < ticks; i++) {
            for (Game game : games) {
                game.getField().tick();
            }
            profiler.done("tick");

            for (int j = 0; j < games.size(); j++) {
                games.get(j).getBoardAsString();
            }
            profiler.done("print");
        }

        profiler.print();

        int reserve = 3;
        // сколько пользователей - столько раз выполнялось
        assertLess("print", 4500 * reserve);
        assertLess("tick", 28000 * reserve);
        // выполнялось единожды
        assertLess("creation", 1500 * reserve);

    }

    private void assertLess(String phase, double expected) {
        double actual = profiler.info(phase).getTime();
        assertTrue(actual + " > " + expected, actual < expected);
    }
}