package com.codenjoy.dojo.football;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.football.client.Board;
import com.codenjoy.dojo.football.client.ai.DefaultSolver;
import com.codenjoy.dojo.football.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmokeTest {
    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(
                1, 2, 3, 0, 3, 2);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼☼☼☼" +
                        "☼☼☼┴┴┴☼☼☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼   ∙   ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼☼☼┬┬┬☼☼☼" +
                        "☼☼☼☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new DefaultSolver(dice),
                new Board());

        // then
        assertEquals("DICE:1\n" +
                        "DICE:2\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☺      ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼☺      ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☺  ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺ ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂x⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: \n" +
                        "Fire Event: WIN\n" +
                        "Fire Event: TOP_GOAL\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺    ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼  ☺    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂x⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: \n" +
                        "Fire Event: WIN\n" +
                        "Fire Event: TOP_GOAL\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺ ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
