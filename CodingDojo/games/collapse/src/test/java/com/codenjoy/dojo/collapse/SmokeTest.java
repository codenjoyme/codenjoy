package com.codenjoy.dojo.collapse;

import com.codenjoy.dojo.collapse.client.Board;
import com.codenjoy.dojo.collapse.client.ai.ApofigSolver;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.collapse.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.settings.Settings;
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
                0, 1, 3, 3, 3, 1, 2, 3, 0, 1, 2, 3, 0,
                1, 2, 2, 1, 0, 2, 0, 3, 3, 1, 3, 3, 0,
                3, 2, 2, 1, 0, 3, 0, 3, 2, 1, 2, 3, 0,
                2, 1, 1, 2, 1, 1, 2, 0, 0, 1, 2, 2, 1,
                1, 1, 2, 2, 3, 2, 2, 0, 0, 3, 1, 2, 2,
                0, 2, 1, 3, 0, 3, 2, 3, 1, 1, 2, 3, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            public GameField createGame() {
                settings.getParameter("Field size").type(Integer.class).update(5);
                return super.createGame();
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ApofigSolver(dice),
                new Board());

        // then
        assertEquals("Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼682☼\n" +
                        "☼371☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(1,2),DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼382☼\n" +
                        "☼671☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(0,1),UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼382☼\n" +
                        "☼671☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(2,1),LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼382☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(2,0),DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼382☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(3,1),DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼382☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(3,0),DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼382☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(2,2),RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼328☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(0,3),LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼328☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(3,2),RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼135☼\n" +
                        "☼328☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(2,3),LEFT\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼ 15☼\n" +
                        "☼ 28☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(2,1),RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼215☼\n" +
                        "☼328☼\n" +
                        "☼716☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(1,2),LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼215☼\n" +
                        "☼328☼\n" +
                        "☼716☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(0,1),UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼215☼\n" +
                        "☼328☼\n" +
                        "☼716☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(2,1),RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "Board:\n" +
                        "☼☼☼☼☼\n" +
                        "☼215☼\n" +
                        "☼328☼\n" +
                        "☼761☼\n" +
                        "☼☼☼☼☼\n" +
                        "\n" +
                        "Answer: ACT(1,2),UP\n" +
                        "Fire Event: SUCCESS\n" +
                        "------------------------------------------------------------------------------------",
                String.join("\n", messages));

    }
}
