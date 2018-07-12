package com.codenjoy.dojo.moebius;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.moebius.client.Board;
import com.codenjoy.dojo.moebius.client.ai.ApofigSolver;
import com.codenjoy.dojo.moebius.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmokeTest {
    private int index;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 11;

        Dice dice = LocalGameRunner.getDice(
                0, 0, 1, 1, 1,
                1, 1, 1, 2, 2,
                1, 2, 1, 3, 3,
                1, 3, 2, 1, 2,
                2, 1, 2, 2, 5,
                2, 2, 2, 3, 6,
                2, 3, 3, 1, 0,
                3, 1, 3, 2, 1,
                3, 2, 3, 3, 3);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected SettingsImpl createSettings() {
                SettingsImpl settings = super.createSettings();
                settings.addEditBox("Size").type(Integer.class).update(5);
                return settings;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ApofigSolver(dice),
                new Board());

        // then
        assertEquals("1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║   ║\n" +
                        "1:║   ║\n" +
                        "1:║   ║\n" +
                        "1:╚═══╝\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "1:Answer: ACT(0,0)\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║   ║\n" +
                        "1:║   ║\n" +
                        "1:║╚  ║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: ACT(1,1)\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║   ║\n" +
                        "1:║╔  ║\n" +
                        "1:║╔  ║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Answer: ACT(1,2)\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╗  ║\n" +
                        "1:║╗  ║\n" +
                        "1:║╔  ║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "1:Answer: ACT(1,3)\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╝  ║\n" +
                        "1:║╗  ║\n" +
                        "1:║╔╔ ║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Answer: ACT(2,1)\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:5\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╝  ║\n" +
                        "1:║╗║ ║\n" +
                        "1:║╔╗ ║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "1:Answer: ACT(2,2)\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:6\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╝╬ ║\n" +
                        "1:║╗═ ║\n" +
                        "1:║╔╗ ║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "1:Answer: ACT(2,3)\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╝╬ ║\n" +
                        "1:║╗═ ║\n" +
                        "1:║╔╗╝║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "1:Answer: ACT(3,1)\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╝╬ ║\n" +
                        "1:║╗═╚║\n" +
                        "1:║╔╗╚║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "1:Answer: ACT(3,2)\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║╝╬╗║\n" +
                        "1:║╗═╔║\n" +
                        "1:║╔╗╚║\n" +
                        "1:╔═══╝\n" +
                        "1:\n" +
                        "1:Answer: \n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "Fire Event: GAME_OVER\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:╔═══╗\n" +
                        "1:║   ║\n" +
                        "1:║   ║\n" +
                        "1:║   ║\n" +
                        "1:╚═══╝\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "1:Answer: ACT(3,3)\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
