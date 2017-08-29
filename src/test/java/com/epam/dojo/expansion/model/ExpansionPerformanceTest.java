package com.epam.dojo.expansion.model;

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


import com.codenjoy.dojo.profile.Profiler;
import com.codenjoy.dojo.services.Game;
import com.epam.dojo.expansion.services.GameRunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 15.02.14.
 */
public class ExpansionPerformanceTest {

    @Test // TODO закончить как будет настроение :)
    public void test() {
        GameRunner gameRunner = new GameRunner();

        List<com.codenjoy.dojo.services.Game> games = new LinkedList<com.codenjoy.dojo.services.Game>();

        PrinterFactory factory = new PrinterFactoryImpl();
        for (int index = 0; index < 50; index++) {
            Game game = gameRunner.newGame(mock(EventListener.class), factory, null);
            games.add(game);
        }

        Profiler profiler = new Profiler();

        profiler.start();
        for (Game game : games) {
            game.getBoardAsString();
            profiler.done("getBoardAsString");
        }
        profiler.print();
    }
}
