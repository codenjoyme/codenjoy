package com.codenjoy.dojo.spacerace;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.spacerace.client.Board;
import com.codenjoy.dojo.spacerace.client.YourSolver;
import com.codenjoy.dojo.spacerace.client.ai.AlAnSolver;
import com.codenjoy.dojo.spacerace.services.GameRunner;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {

    private Dice dice;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(
                0, 1, 2, 3, 4, // random numbers
                0, 2, 2, 3, 1,
                0, 1, 4, 1, 2,
                1, 3, 2, 4, 1,
                1, 3, 0, 0, 1,
                1, 3, 0, 0, 1,
                0, 0, 1, 0, 2,
                0, 0, 1, 0, 2,
                1, 3, 2, 4, 1,
                1, 3, 2, 4, 1,
                0, 1, 1, 3, 3,
                0, 1, 1, 3, 3,
                0, 1, 1, 3, 3,
                0, 2, 0, 0, 2,
                0, 2, 0, 0, 2,
                0, 1, 2, 2, 1,
                0, 1, 2, 2, 1,
                1, 3, 4, 3, 4,
                1, 3, 2, 4, 1,
                0, 1, 3, 1, 1);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼       ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼   ☺   ☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new LinkedList<Solver>(){{
                    add(new YourSolver(dice));
                    add(new AlAnSolver(dice));
                }},
                new LinkedList<ClientBoard>(){{
                    add(new Board());
                    add(new Board());
                }});

        // then
        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:0\n" +
                        "1:Board:\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHT\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHTACT\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼ ♣70   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼ ♣70   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼ ♣ 0   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼ ♣ 0   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ♣ 0   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ♣ 0   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFTACT\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼0♣7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ♣ 0   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼0♣7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ♣ 0   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ☻     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHT\n" +
                        "Fire Event: LOOSE\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼0♣     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼xxx    ☼\n" +
                        "1:☼xxx0   ☼\n" +
                        "1:☼xxx    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼0♣     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼xxx    ☼\n" +
                        "2:☼xxx0   ☼\n" +
                        "2:☼xxx    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☻   ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼0♣     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   0   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼0♣     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   0   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☻   ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHTACT\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼ ♣0    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼0♣     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   0   ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼ ♣0    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼0♣     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   0   ☼\n" +
                        "2:☼   ☻   ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFT\n" +
                        "Fire Event: LOOSE\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼ ♣0    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼0♣     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼ ♣0    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼0♣     ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼  ☻    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHT\n" +
                        "Fire Event: LOOSE\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ♣0    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼xxx    ☼\n" +
                        "1:☼xxx    ☼\n" +
                        "1:☼xxx    ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ♣0    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼xxx    ☼\n" +
                        "2:☼xxx    ☼\n" +
                        "2:☼xxx    ☼\n" +
                        "2:☼   ☻   ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFTACT\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼ 07♣   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ♣0    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼0      ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼ 07♣   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ♣0    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼0      ☼\n" +
                        "2:☼   ☻   ☼\n" +
                        "2:☼   ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼ 0 ♣   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ♣0    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼0  ☺   ☼\n" +
                        "1:☼    ☻  ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼ 0 ♣   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ♣0    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼0  ☻   ☼\n" +
                        "2:☼    ☺  ☼\n" +
                        "2:\n" +
                        "2:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼  7    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ 0 ♣   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ♣0    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼0  ☻   ☼\n" +
                        "1:\n" +
                        "1:Answer: STOP\n" +
                        "2:Board:\n" +
                        "2:☼  7    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ 0 ♣   ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼ ♣0    ☼\n" +
                        "2:☼       ☼\n" +
                        "2:☼   ☻   ☼\n" +
                        "2:☼0  ☺   ☼\n" +
                        "2:\n" +
                        "2:Answer: RIGHTACT\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
