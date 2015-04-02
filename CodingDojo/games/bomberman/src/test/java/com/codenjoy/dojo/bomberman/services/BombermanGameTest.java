package com.codenjoy.dojo.bomberman.services;

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
public class BombermanGameTest {

    private PrinterFactory printerFactory = new PrinterFactoryImpl();;

    @Ignore
    @Test
    public void shouldWork() {
        int size = 11;

        EventListener listener = mock(EventListener.class);
        GameType bombermanGame = new BombermanGame();

        bombermanGame.getSettings().getParameter("Board size").type(Integer.class).update(size);
        int countDestroyWalls = 5;
        bombermanGame.getSettings().getParameter("Destroy wall count").type(Integer.class).update(5);
        int meatChoppersCount = 15;
        bombermanGame.getSettings().getParameter("Meat choppers count").type(Integer.class).update(meatChoppersCount);

        Game game = bombermanGame.newGame(listener, printerFactory);
        game.tick();

        PlayerScores scores = bombermanGame.getPlayerScores(10);
        assertEquals(10, scores.getScore());
        scores.event(BombermanEvents.KILL_MEAT_CHOPPER);
        assertEquals(110, scores.getScore());

        assertEquals(size, bombermanGame.getBoardSize().getValue().intValue());

        Joystick joystick = game.getJoystick();

        int countWall = (size - 1) * 4 + (size / 2 - 1) * (size / 2 - 1);

        String actual = game.getBoardAsString();
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
        GameType bombermanGame = new BombermanGame();
        Game game1 = bombermanGame.newGame(listener, printerFactory);
        Game game2 = bombermanGame.newGame(listener, printerFactory);
        assertSame(getBoard(game1), getBoard(game2));
    }

    private Bomberman getBoard(Game game) {
        return Reflection.field("game").ofType(Bomberman.class).in(game).get();
    }

    private void assertCharCount(String actual, String ch, int count) {
        assertEquals(count, actual.length() - actual.replace(ch, "").length());
    }

}
