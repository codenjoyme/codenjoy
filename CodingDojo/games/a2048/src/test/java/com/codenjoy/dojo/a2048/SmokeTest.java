package com.codenjoy.dojo.a2048;

import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.a2048.client.ai.ApofigSolver;
import com.codenjoy.dojo.a2048.services.GameRunner;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {
    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(0);

        // when
        GameRunner gameType = new GameRunner(){
            @Override
            protected Dice getDice() {
                return dice;
            }
        };
        LocalGameRunner.run(gameType,
                new ApofigSolver(null),
                new Board());

        // then
        assertEquals("1:Board:\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(8)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:2   2\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(12)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:2   4\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: SUM(16)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:4   8\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(20)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:2  48\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: SUM(24)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:4  48\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(28)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:2  88\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: SUM(32)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    8\n" +
                        "1:4  88\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(36)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    8\n" +
                        "1:2  4A\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: SUM(40)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:    8\n" +
                        "1:4  4A\n" +
                        "1:\n" +
                        "1:Answer: LEFT\n" +
                        "Fire Event: SUM(44)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:4   2\n" +
                        "1:     \n" +
                        "1:4    \n" +
                        "1:8    \n" +
                        "1:8A  2\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: SUM(48)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:8    \n" +
                        "1:AA  4\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(52)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    8\n" +
                        "1:2  B4\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: SUM(56)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:    8\n" +
                        "1:4  B4\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: SUM(60)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:    8\n" +
                        "1:2 4B4\n" +
                        "1:\n" +
                        "1:Answer: UP\n" +
                        "Fire Event: SUM(64)\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
