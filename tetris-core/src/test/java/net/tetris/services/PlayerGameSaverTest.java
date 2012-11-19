package net.tetris.services;

import net.tetris.dom.Levels;
import net.tetris.services.levels.EasyLevels;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 11/15/12
 * Time: 2:07 AM
 */
public class PlayerGameSaverTest {

    @Test
    public void test() {
        PlayerFigures queue = new PlayerFigures();

        Levels levels = new EasyLevels(queue);
        levels.linesRemoved(10);
        levels.linesRemoved(10);

        PlayerScores scores = new PlayerScores(10);
        Information info = new Information() {
            @Override
            public String getMessage() {
                return "Some info";
            }
        };

        Player player = new Player("vasia", "http://127.0.0.1:8888", scores, levels, info);

        GameSaver saver = new PlayerGameSaver();

        saver.saveGame(player);

        Player loaded = saver.loadGame("vasia").getPlayer();
        assertEqualsProperties(player, loaded);

        saver.delete("vasia");
    }

    private void assertEqualsProperties(Player expected, Player actual) {
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCallbackUrl(), actual.getCallbackUrl());
        assertEquals(expected.getCurrentLevelNumber(), actual.getCurrentLevelNumber());
        assertEquals(expected.getNextLevelIngoingCriteria(), actual.getNextLevelIngoingCriteria());
        assertEquals(expected.getScore(), actual.getScore());
        assertEquals(expected.getTotalRemovedLines(), actual.getTotalRemovedLines());

        String newInfo = ", Level " + (expected.getCurrentLevelNumber() + 1);
        assertEquals(expected.getMessage(), actual.getMessage().replaceAll(newInfo, ""));
    }
}
