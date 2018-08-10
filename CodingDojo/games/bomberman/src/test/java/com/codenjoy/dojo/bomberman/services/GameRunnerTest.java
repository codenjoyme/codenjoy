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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

public class GameRunnerTest {

    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Ignore
    @Test
    public void shouldWork() {
        int size = 11;

        EventListener listener = mock(EventListener.class);
        GameType gameType = new GameRunner();

        gameType.getSettings().getParameter("Board size").type(Integer.class).update(size);
        int countDestroyWalls = 5;
        gameType.getSettings().getParameter("Destroy wall count").type(Integer.class).update(5);
        int meatChoppersCount = 15;
        gameType.getSettings().getParameter("Meat choppers count").type(Integer.class).update(meatChoppersCount);

        Game game = TestUtils.buildGame(gameType, listener, printerFactory);
        game.getField().tick();

        PlayerScores scores = gameType.getPlayerScores(10);
        assertEquals(10, scores.getScore());
        scores.event(Events.KILL_MEAT_CHOPPER);
        assertEquals(110, scores.getScore());

        assertEquals(size, gameType.getBoardSize().getValue().intValue());

        Joystick joystick = game.getJoystick();

        int countWall = (size - 1) * 4 + (size / 2 - 1) * (size / 2 - 1);

        String actual = (String)game.getBoardAsString();
        assertCharCount(actual, "☼", countWall);
        assertCharCount(actual, "#", countDestroyWalls);
        assertCharCount(actual, "☺", 1); // TODO почему тут скачет тест?
        assertCharCount(actual, "&", meatChoppersCount);  // TODO тут ошибка опять появилась
        assertCharCount(actual, " ", size * size - countWall - countDestroyWalls - meatChoppersCount - 1);

        assertFalse(game.isGameOver());

        joystick.act();
        for (int index = 0; index < 100; index ++) {
            game.getField().tick();
        }

        assertTrue(game.isGameOver());
    }

    private void assertCharCount(String actual, String ch, int count) {
        assertEquals(count, actual.length() - actual.replace(ch, "").length());
    }

}
