package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.DefaultGameSettings;
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

        assertEquals(DefaultGameSettings.BOARD_SIZE, bombermanGame.getBoardSize());

        Joystick joystick = game.getJoystick();

        String actual = game.getBoardAsString();
        assertEquals("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                    "☼             ☼" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼" +
                    "☼             ☼" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼" +
                    "☼             ☼" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼" +
                    "☼             ☼" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼" +
                    "☼             ☼" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼" +
                    "☼             ☼" +
                    "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼" +
                    "☼             ☼" +
                    "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼", actual.replaceAll("[J]", "☼").replaceAll("[ANKL]", " "));
        assertCharCount(actual, "N", 106);
        assertCharCount(actual, "K", 22);
        assertCharCount(actual, "L", DefaultGameSettings.MEAT_CHOPPERS_COUNT);
        assertCharCount(actual, "A", 1);

        assertEquals(0, game.getMaxScore());
        assertEquals(0, game.getCurrentScore());
        assertFalse(game.isGameOver());

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
