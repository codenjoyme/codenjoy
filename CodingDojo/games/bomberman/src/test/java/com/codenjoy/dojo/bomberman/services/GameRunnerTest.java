package com.codenjoy.dojo.bomberman.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.bomberman.model.Bomberman;
import com.codenjoy.dojo.services.*;
import org.fest.reflect.core.Reflection;
import org.junit.Ignore;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 5:23 PM
 */
public class GameRunnerTest {

    private PrinterFactory printerFactory = new PrinterFactoryImpl();;

    @Ignore
    @Test
    public void shouldWork() {
        int size = 11;

        EventListener listener = mock(EventListener.class);
        GameType bombermanGame = new GameRunner();

        bombermanGame.getSettings().getParameter("Board size").type(Integer.class).update(size);
        int countDestroyWalls = 5;
        bombermanGame.getSettings().getParameter("Destroy wall count").type(Integer.class).update(5);
        int meatChoppersCount = 15;
        bombermanGame.getSettings().getParameter("Meat choppers count").type(Integer.class).update(meatChoppersCount);

        com.codenjoy.dojo.services.Game game = bombermanGame.newGame(listener, printerFactory, null, null);
        game.tick();

        PlayerScores scores = bombermanGame.getPlayerScores(10);
        assertEquals(10, scores.getScore());
        scores.event(Events.KILL_MEAT_CHOPPER);
        assertEquals(110, scores.getScore());

        assertEquals(size, bombermanGame.getBoardSize().getValue().intValue());

        Joystick joystick = game.getJoystick();

        int countWall = (size - 1) * 4 + (size / 2 - 1) * (size / 2 - 1);

        String actual = (String)game.getBoardAsString();
        assertCharCount(actual, "☼", countWall);
        assertCharCount(actual, "#", countDestroyWalls);
        assertCharCount(actual, "☺", 1);
        assertCharCount(actual, "&", meatChoppersCount);  // TODO тут ошибка опять появилась
        assertCharCount(actual, " ", size * size - countWall - countDestroyWalls - meatChoppersCount - 1);

        assertEquals(0, game.getMaxScore());
        assertEquals(0, game.getCurrentScore());
        assertFalse(game.isGameOver());

        joystick.act();
        for (int index = 0; index < 100; index ++) {
            game.tick();
        }

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldOneBoardForAllGames() {
        EventListener listener = mock(EventListener.class);
        GameType bombermanGame = new GameRunner();
        com.codenjoy.dojo.services.Game game1 = bombermanGame.newGame(listener, printerFactory, null, null);
        com.codenjoy.dojo.services.Game game2 = bombermanGame.newGame(listener, printerFactory, null, null);
        assertSame(getBoard(game1), getBoard(game2));
    }

    private Bomberman getBoard(com.codenjoy.dojo.services.Game game) {
        return Reflection.field("game").ofType(Bomberman.class).in(game).get();
    }

    private void assertCharCount(String actual, String ch, int count) {
        assertEquals(count, actual.length() - actual.replace(ch, "").length());
    }

}
