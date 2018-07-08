package com.codenjoy.dojo.fifteen;

import com.codenjoy.dojo.fifteen.client.Board;
import com.codenjoy.dojo.fifteen.client.ai.FifteenSolver;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.fifteen.services.GameRunner;
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
                1, 2, 3, 0, 3, 2, 2, 0,
                0, 3, 2, 1, 3, 1, 3, 3,
                0, 3, 2, 1, 3, 2, 3, 0,
                1, 1, 2, 1, 3, 2, 1, 2,
                0, 2, 1, 2, 1, 3, 3, 1,
                1, 3, 1, 1, 3, 2, 3, 0,
                1, 0, 2, 1, 3, 2, 1, 2,
                0, 2, 1, 2, 1, 3, 3, 1,
                3, 3, 2, 1, 2, 3, 2, 1,
                2, 3, 2, 1, 2, 3, 1, 2,
                3, 1, 2, 3, 1, 1, 2, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new FifteenSolver(dice),
                new Board());

        // thend
        assertEquals("DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 2 :1\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+jli*\n" +
                        "1:*mkon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*mjli*\n" +
                        "1:*+kon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*mjli*\n" +
                        "1:*k+on*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "Answer: UP\n" +
                        "Fire Event: moveCount: 4, number: 0\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*m+li*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+mli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*m+li*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+mli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*kmli*\n" +
                        "1:*+jon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "Answer: UP\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+mli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "Answer: UP\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*+feg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*f+eg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*+feg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*f+eg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*fe+g*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "Board:\n" +
                        "1:******\n" +
                        "1:*felg*\n" +
                        "1:*hm+i*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
