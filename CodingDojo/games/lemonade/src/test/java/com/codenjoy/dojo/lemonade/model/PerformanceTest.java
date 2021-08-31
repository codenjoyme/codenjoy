package com.codenjoy.dojo.lemonade.model;

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


import com.codenjoy.dojo.lemonade.services.GameRunner;
import com.codenjoy.dojo.lemonade.services.GameSettings;
import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.lemonade.services.GameSettings.Keys.LIMIT_DAYS;
import static org.mockito.Mockito.mock;

public class PerformanceTest {

    @Test
    public void test() {
        GameSettings settings = new GameSettings()
                .integer(LIMIT_DAYS, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            public GameSettings getSettings() {
                return settings;
            }
        };

        List<Game> games = new LinkedList<>();

        PrinterFactory factory = gameType.getPrinterFactory();
        for (int index = 0; index < 50; index++) {
            Game game = TestUtils.buildGame(gameType, mock(EventListener.class), factory);
            games.add(game);
        }

        Profiler profiler = new Profiler();

        for (Game game : games) {
            profiler.start();

            game.getBoardAsString();

            profiler.done("getBoardAsString");
            profiler.print();
        }
    }
}
