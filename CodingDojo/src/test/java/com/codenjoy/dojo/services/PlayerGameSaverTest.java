package com.codenjoy.dojo.services;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 11/15/12
 * Time: 2:07 AM
 */
public class PlayerGameSaverTest {

    @Test
    public void test() {
        PlayerScores scores = mock(PlayerScores.class);
        when(scores.getScore()).thenReturn(10);

        Information info = mock(Information.class);
        when(info.getMessage()).thenReturn("Some info");

        GameType gameType = mock(GameType.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);

        Player player = new Player("vasia", "http://127.0.0.1:8888", scores, info, Protocol.HTTP);

        GameSaver saver = new PlayerGameSaver();

        saver.saveGame(player);

        Player loaded = saver.loadGame("vasia").getPlayer(gameType);
        assertEqualsProperties(player, loaded);

        saver.delete("vasia");
    }

    private void assertEqualsProperties(Player expected, Player actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCallbackUrl(), actual.getCallbackUrl());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.getCurrentLevel(), actual.getCurrentLevel());

        String newInfo = ", Level " + (expected.getCurrentLevel() + 1);
        assertEquals(expected.getMessage(), actual.getMessage().replaceAll(newInfo, ""));
    }
}
