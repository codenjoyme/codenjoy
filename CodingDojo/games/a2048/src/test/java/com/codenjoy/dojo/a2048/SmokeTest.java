package com.codenjoy.dojo.a2048;

import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.a2048.client.ai.ApofigSolver;
import com.codenjoy.dojo.a2048.services.GameRunner;
import com.codenjoy.dojo.client.LocalGameRunner;
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

        // when
        LocalGameRunner.run(new GameRunner(),
                new ApofigSolver(null),
                new Board());

        // then
        assertEquals("Board:\n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(8)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "2   2\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(12)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   4\n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "2   4\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "Fire Event: SUM(16)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "4   8\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(20)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   4\n" +
                        "     \n" +
                        "     \n" +
                        "     \n" +
                        "2  48\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "Fire Event: SUM(24)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "     \n" +
                        "    4\n" +
                        "4  48\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(28)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   4\n" +
                        "     \n" +
                        "     \n" +
                        "    4\n" +
                        "2  88\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "Fire Event: SUM(32)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "     \n" +
                        "    8\n" +
                        "4  88\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(36)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   4\n" +
                        "     \n" +
                        "     \n" +
                        "    8\n" +
                        "2  4A\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "Fire Event: SUM(40)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "    4\n" +
                        "    8\n" +
                        "4  4A\n" +
                        "\n" +
                        "Answer: LEFT\n" +
                        "Fire Event: SUM(44)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "4   2\n" +
                        "     \n" +
                        "4    \n" +
                        "8    \n" +
                        "8A  2\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "Fire Event: SUM(48)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "     \n" +
                        "8    \n" +
                        "AA  4\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(52)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   4\n" +
                        "     \n" +
                        "     \n" +
                        "    8\n" +
                        "2  B4\n" +
                        "\n" +
                        "Answer: DOWN\n" +
                        "Fire Event: SUM(56)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   2\n" +
                        "     \n" +
                        "    4\n" +
                        "    8\n" +
                        "4  B4\n" +
                        "\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: SUM(60)\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "2   4\n" +
                        "     \n" +
                        "    4\n" +
                        "    8\n" +
                        "2 4B4\n" +
                        "\n" +
                        "Answer: UP\n" +
                        "Fire Event: SUM(64)\n" +
                        "------------------------------------------------------------------------------------",
                String.join("\n", messages));

    }
}
