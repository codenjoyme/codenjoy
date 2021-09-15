package com.codenjoy.dojo.clifford;

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

import com.codenjoy.dojo.clifford.services.GameRunner;
import com.codenjoy.dojo.clifford.services.GameSettings;
import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.List;

import static com.codenjoy.dojo.clifford.services.GameSettings.Keys.ROBBERS_COUNT;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PerformanceTest {

    private Profiler profiler;

    @Test
    public void test() {

        // about 5 sec
        int robbers = 4;
        int players = 20; // TODO up to 100
        int ticks = 100;

        profiler = new Profiler(){{
            PRINT_SOUT = true;
        }};
        profiler.start();

        GameRunner runner = new GameRunner(){
            @Override
            public GameSettings getSettings() {
                return super.getSettings()
                    .bool(ROUNDS_ENABLED, false)
                    .integer(ROBBERS_COUNT, robbers);
            }
        };

        PrinterFactory factory = new PrinterFactoryImpl();

        List<Game> games = TestUtils.getGames(players, runner,
                factory, () -> mock(EventListener.class));

        profiler.done("creation");

        for (int i = 0; i < ticks; i++) {
            for (Game game : games) {
                Joystick joystick = game.getJoystick();
                int next = new RandomDice().next(5);
                if (next % 2 == 0) {
                    joystick.act();
                }
                switch (next) {
                    case 0: joystick.left(); break;
                    case 1: joystick.right(); break;
                    case 2: joystick.up(); break;
                    case 3: joystick.down(); break;
                }
            }
            // because of MULTIPLE there is only one tick for all
            games.get(0).getField().tick();
            for (Game game : games) {
                if (game.isGameOver()) {
                    game.newGame();
                }
            }
            profiler.done("tick");

            Object board = null;
            for (int j = 0; j < games.size(); j++) {
                board = games.get(j).getBoardAsString();
            }
//            System.out.println(board);
            profiler.done("print");
        }

        profiler.print();

        int reserve = 4;
        // сколько пользователей - столько раз выполнялось
        assertLess("print", 3000 * reserve);
        assertLess("tick", 20000 * reserve);
        // выполнялось единожды
        assertLess("creation", 1300 * reserve);
    }

    private void assertLess(String phase, double expected) {
        double actual = profiler.info(phase).getTime();
        assertEquals(actual + " > " + expected, true, actual < expected);
    }
}