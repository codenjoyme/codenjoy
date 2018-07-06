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

        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(
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
        assertEquals("Board:\n" +
                        "******\n" +
                        "*ijm+*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*ijmc*\n" +
                        "*kln+*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*ijm+*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*ij+m*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*i+jm*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*+ijm*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*i+jm*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*+ijm*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*kijm*\n" +
                        "*+lnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*+ijm*\n" +
                        "*klnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*kijm*\n" +
                        "*+lnc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*kijm*\n" +
                        "*l+nc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*k+jm*\n" +
                        "*linc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*+kjm*\n" +
                        "*linc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "******\n" +
                        "*k+jm*\n" +
                        "*linc*\n" +
                        "*oadg*\n" +
                        "*ebhf*\n" +
                        "******\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------",
                String.join("\n", messages));

    }
}
