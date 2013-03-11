package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 5:23 PM
 */
public class BombermanGameTest {

    @Test
    public void shouldWork() {
        EventListener listener = mock(EventListener.class);
        GameType bombermanGame = new BombermanGame();
        Game game = bombermanGame.newGame(listener);

        PlayerScores scores = bombermanGame.getPlayerScores(10);
        assertEquals(10, scores.getScore());
        scores.event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        assertEquals(110, scores.getScore());

        assertEquals(15, bombermanGame.getBoardSize());

        Joystick joystick = game.getJoystick();

        String actual = game.getBoardAsString();
        assertCharCount(actual, "#", 22);
        assertCharCount(actual, "&", BombermanGame.MEAT_CHOPPERS_COUNT);
        assertCharCount(actual, "☺", 1);
        assertEquals("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                    "☼             ☼\n" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                    "☼             ☼\n" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                    "☼             ☼\n" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                    "☼             ☼\n" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                    "☼             ☼\n" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                    "☼             ☼\n" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼\n" +
                    "☼             ☼\n" +
                    "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n", actual.replaceAll("[#&☺]", " "));

        assertEquals(13, game.getMaxScore());
        assertEquals(14, game.getCurrentScore());
        assertFalse(game.isGameOver());

        assertEquals(225, game.getPlots().size());

        joystick.act();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertTrue(game.isGameOver());
    }

    private void assertCharCount(String actual, String ch, int count) {
        assertEquals(count, actual.length() - actual.replace(ch, "").length());
    }

}
