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

        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(
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
        assertEquals("Board:\n" +
                        "╔═══╗\n" +
                        "║   ║\n" +
                        "║   ║\n" +
                        "║   ║\n" +
                        "╚═══╝\n" +
                        "\n" +
                        "Answer: ACT(0,0)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║   ║\n" +
                        "║   ║\n" +
                        "║╚  ║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(1,1)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║   ║\n" +
                        "║╔  ║\n" +
                        "║╔  ║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(1,2)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╗  ║\n" +
                        "║╗  ║\n" +
                        "║╔  ║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(1,3)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╝  ║\n" +
                        "║╗  ║\n" +
                        "║╔╔ ║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(2,1)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╝  ║\n" +
                        "║╗║ ║\n" +
                        "║╔╗ ║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(2,2)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╝╬ ║\n" +
                        "║╗═ ║\n" +
                        "║╔╗ ║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(2,3)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╝╬ ║\n" +
                        "║╗═ ║\n" +
                        "║╔╗╝║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(3,1)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╝╬ ║\n" +
                        "║╗═╚║\n" +
                        "║╔╗╚║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: ACT(3,2)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║╝╬╗║\n" +
                        "║╗═╔║\n" +
                        "║╔╗╚║\n" +
                        "╔═══╝\n" +
                        "\n" +
                        "Answer: \n" +
                        "Fire Event: GAME_OVER\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "╔═══╗\n" +
                        "║   ║\n" +
                        "║   ║\n" +
                        "║   ║\n" +
                        "╚═══╝\n" +
                        "\n" +
                        "Answer: ACT(3,3)\n" +
                        "------------------------------------------------------------------------------------",
                String.join("\n", messages));

    }
}
