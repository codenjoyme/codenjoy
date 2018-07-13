package com.codenjoy.dojo.sudoku;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.sudoku.client.Board;
import com.codenjoy.dojo.sudoku.client.ai.ApofigSolver;
import com.codenjoy.dojo.sudoku.services.GameRunner;
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
        LocalGameRunner.countIterations = 10;

        Dice dice = LocalGameRunner.getDice(
                0, 0,
                0, 1,
                1, 2,
                2, 3,
                3, 4,
                4, 5,
                5, 6,
                6, 7,
                7, 8,
                3, 0,
                2, 1,
                4, 2,
                5, 3,
                6, 4,
                3, 5,
                2, 6,
                4, 7,
                5, 8,
                2, 0,
                4, 1,
                6, 2,
                7, 3,
                8, 4,
                6, 5,
                5, 6,
                4, 7,
                2, 8,
                3, 0,
                5, 1,
                6, 2,
                7, 3,
                8, 4,
                5, 5,
                4, 6,
                3, 7,
                2, 8,
                4, 0,
                6, 1,
                6, 2,
                7, 3,
                4, 4,
                3, 5,
                4, 6,
                2, 7,
                3, 8,
                0, 3,
                5, 6,
                6, 0,
                4, 6,
                1, 3,
                0, 4,
                7, 6,
                6, 0,
                4, 5,
                6, 3,
                2, 1,
                4, 0,
                2, 3,
                5, 4,
                6, 1,
                2, 1,
                5, 3,
                2, 0,
                1, 3,
                2, 1);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ApofigSolver(dice),
                new Board());

        // then
        assertEquals("DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "DICE:6\n" +
                        "DICE:6\n" +
                        "DICE:7\n" +
                        "DICE:7\n" +
                        "DICE:8\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:5\n" +
                        "DICE:3\n" +
                        "DICE:6\n" +
                        "DICE:4\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:2\n" +
                        "DICE:6\n" +
                        "DICE:4\n" +
                        "DICE:7\n" +
                        "DICE:5\n" +
                        "DICE:8\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:6\n" +
                        "DICE:2\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "DICE:8\n" +
                        "DICE:4\n" +
                        "DICE:6\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "DICE:6\n" +
                        "DICE:4\n" +
                        "DICE:7\n" +
                        "DICE:2\n" +
                        "DICE:8\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:6\n" +
                        "DICE:2\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "DICE:8\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "DICE:4\n" +
                        "DICE:6\n" +
                        "DICE:3\n" +
                        "DICE:7\n" +
                        "DICE:2\n" +
                        "DICE:8\n" +
                        "DICE:4\n" +
                        "DICE:0\n" +
                        "DICE:6\n" +
                        "DICE:1\n" +
                        "DICE:6\n" +
                        "DICE:2\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:4\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:4\n" +
                        "DICE:6\n" +
                        "DICE:2\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "DICE:8\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:6\n" +
                        "DICE:6\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:6\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:7\n" +
                        "DICE:6\n" +
                        "DICE:6\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "DICE:6\n" +
                        "DICE:3\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼6 8☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼   ☼761☼4  ☼\n" +
                        "1:☼4  ☼85 ☼7 1☼\n" +
                        "1:☼713☼  4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 6 ☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(3,4,9)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼6 8☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼4  ☼85 ☼7 1☼\n" +
                        "1:☼713☼  4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 6 ☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(2,5,2)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼6 8☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼42 ☼85 ☼7 1☼\n" +
                        "1:☼713☼  4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 6 ☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(3,5,6)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼6 8☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼  4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 6 ☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(3,7,1)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼6 8☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼  4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 61☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(4,6,9)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼6 8☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼9 4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 61☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(5,1,7)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼678☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼9 4☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 61☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(5,6,2)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼678☼ 1 ☼\n" +
                        "1:☼  2☼19 ☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼924☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 61☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(6,2,5)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼678☼ 1 ☼\n" +
                        "1:☼  2☼195☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼924☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 61☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(1,2,6)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  4☼678☼ 1 ☼\n" +
                        "1:☼6 2☼195☼3  ☼\n" +
                        "1:☼  8☼ 42☼ 6 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼  9☼761☼4  ☼\n" +
                        "1:☼426☼85 ☼7 1☼\n" +
                        "1:☼713☼924☼85 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ 61☼ 3 ☼2  ☼\n" +
                        "1:☼2 7☼ 19☼6  ☼\n" +
                        "1:☼3 5☼28 ☼1  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: ACT(2,2,7)\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
