package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.client.ai.ApofigSolver;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.client.LocalGameRunner;
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
        LocalGameRunner.countIterations = 20;

        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(
                1, 2, 3, 0, 3, 2, 2, 0,
                0, 3, 2, 1, 3, 1, 3, 3,
                0, 3, 2, 1, 3, 2, 3, 0,
                1, 1, 2, 1, 3, 2, 1, 2,
                0, 2, 1, 2, 1, 3, 3, 1,
                2, 3, 2, 1, 3, 3, 2, 2,
                1, 2, 3, 1, 1, 2, 1, 2,
                2, 3, 1, 1, 3, 3, 2, 2,
                1, 3, 2, 1, 1, 3, 1, 2,
                3, 2, 3, 2, 3, 1, 3, 1,
                2, 2, 0, 2, 1, 3, 3, 1,
                1, 3, 1, 1, 3, 3, 1, 2,
                3, 2, 3, 2, 1, 1, 3, 1,
                1, 3, 2, 1, 3, 3, 1, 2,
                0, 3, 1, 1, 1, 2, 3, 0,
                3, 2, 3, 2, 3, 1, 3, 1,
                1, 0, 2, 1, 3, 2, 1, 2,
                1, 3, 1, 1, 3, 2, 3, 0,
                3, 1, 3, 1, 3, 1, 3, 1,
                1, 1, 1, 1, 1, 3, 3, 1,
                1, 3, 1, 1, 3, 2, 3, 0,
                1, 0, 2, 1, 3, 2, 1, 2,
                0, 2, 1, 2, 1, 3, 3, 1,
                3, 3, 2, 1, 2, 3, 2, 1,
                2, 3, 2, 1, 2, 3, 1, 2,
                3, 1, 2, 3, 1, 1, 2, 0);

        DefaultGameSettings.BOARD_SIZE = 7;
        DefaultGameSettings.BOMB_POWER = 3;
        DefaultGameSettings.BOMBS_COUNT = 1;
        DefaultGameSettings.DESTROY_WALL_COUNT = 3;
        DefaultGameSettings.MEAT_CHOPPERS_COUNT = 1;

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected GameSettings getGameSettings() {
                return new DefaultGameSettings(dice);
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ApofigSolver(dice),
                new Board());

        // then
        assertEquals("☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼     ☼\n" +
                        "☼☺☼ ☼ ☼\n" +
                        "☼     ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [1,2]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: []\n" +
                        "Destroy walls at: []\n" +
                        "Bombs at: []\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: []\n" +
                        "Answer: DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼##   ☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼☺ &  ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [1,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[3,1]]\n" +
                        "Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: []\n" +
                        "Answer: ACT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼##   ☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼☻  & ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [1,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[4,1]]\n" +
                        "Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "Bombs at: [[1,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼##   ☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼3☺  &☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [2,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,1]]\n" +
                        "Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "Bombs at: [[1,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼##   ☼\n" +
                        "☼ ☼#☼&☼\n" +
                        "☼2 ☺  ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [3,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,2]]\n" +
                        "Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "Bombs at: [[1,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "Answer: ACT,RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼##  &☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼1  ☺ ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [4,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,3]]\n" +
                        "Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "Bombs at: [[1,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "Answer: RIGHT\n" +
                        "Fire Event: KILL_DESTROY_WALL\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼&☼\n" +
                        "☼H#   ☼\n" +
                        "☼҉☼#☼ ☼\n" +
                        "☼҉҉҉҉☺☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,4]]\n" +
                        "Destroy walls at: [[2,3], [3,2]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: [[1,1], [1,2], [2,1], [3,1], [4,1]]\n" +
                        "Expected blasts at: []\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼    &☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ ##  ☼\n" +
                        "☼ ☼#☼☺☼\n" +
                        "☼     ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,2]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,5]]\n" +
                        "Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: []\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼&☼\n" +
                        "☼ ## ☺☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼     ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,3]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,4]]\n" +
                        "Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: []\n" +
                        "Answer: ACT,DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ ## &☼\n" +
                        "☼ ☼#☼☺☼\n" +
                        "☼     ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,2]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,3]]\n" +
                        "Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: []\n" +
                        "Answer: ACT,DOWN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ ## 3☼\n" +
                        "☼ ☼#☼&☼\n" +
                        "☼    ☺☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,2]]\n" +
                        "Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "Bombs at: [[5,3]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[4,3], [5,2], [5,3], [5,4]]\n" +
                        "Answer: ACT,LEFT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ ## 2☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼   ☺&☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [4,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[5,1]]\n" +
                        "Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "Bombs at: [[5,3]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[4,3], [5,2], [5,3], [5,4]]\n" +
                        "Answer: ACT\n" +
                        "Fire Event: KILL_BOMBERMAN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ ## 1☼\n" +
                        "☼ ☼#☼ ☼\n" +
                        "☼  ☺& ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [3,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[4,1]]\n" +
                        "Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "Bombs at: [[5,3]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[4,3], [5,2], [5,3], [5,4]]\n" +
                        "Answer: ACT\n" +
                        "Fire Event: KILL_BOMBERMAN\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼    ҉☼\n" +
                        "☼ ☼ ☼҉☼\n" +
                        "☼ #H҉҉☼\n" +
                        "☼ ☼#☼҉☼\n" +
                        "☼  &☺҉☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [4,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[3,1]]\n" +
                        "Destroy walls at: [[2,3], [3,2]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: [[4,3], [5,1], [5,2], [5,3], [5,4], [5,5]]\n" +
                        "Expected blasts at: []\n" +
                        "Answer: ACT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ #   ☼\n" +
                        "☼#☼#☼ ☼\n" +
                        "☼ &3☻ ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [4,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[2,1]]\n" +
                        "Destroy walls at: [[1,2], [2,3], [3,2]]\n" +
                        "Bombs at: [[3,1], [4,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[2,1], [3,1], [3,2], [4,1], [5,1]]\n" +
                        "Answer: RIGHT\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ #   ☼\n" +
                        "☼#☼#☼ ☼\n" +
                        "☼& 23☺☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,1]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[1,1]]\n" +
                        "Destroy walls at: [[1,2], [2,3], [3,2]]\n" +
                        "Bombs at: [[3,1], [4,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[2,1], [3,1], [3,2], [4,1], [5,1]]\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ #   ☼\n" +
                        "☼#☼#☼☺☼\n" +
                        "☼ &12 ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,2]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[2,1]]\n" +
                        "Destroy walls at: [[1,2], [2,3], [3,2]]\n" +
                        "Bombs at: [[3,1], [4,1]]\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: [[2,1], [3,1], [3,2], [4,1], [5,1]]\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ #  ☺☼\n" +
                        "☼#☼H☼ ☼\n" +
                        "☼҉҉x1҉☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,3]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: []\n" +
                        "Destroy walls at: [[1,2], [2,3]]\n" +
                        "Bombs at: [[4,1]]\n" +
                        "Blasts: [[1,1], [2,1], [5,1]]\n" +
                        "Expected blasts at: [[3,1], [4,1], [5,1]]\n" +
                        "Answer: UP\n" +
                        "Fire Event: KILL_DESTROY_WALL\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼     ☼\n" +
                        "☼ ☼ ☼☺☼\n" +
                        "☼ #   ☼\n" +
                        "☼#☼&☼ ☼\n" +
                        "☼  H҉҉☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,4]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[3,2]]\n" +
                        "Destroy walls at: [[1,2], [2,3]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: [[4,1], [5,1]]\n" +
                        "Expected blasts at: []\n" +
                        "Answer: UP\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "☼    ☺☼\n" +
                        "☼ ☼ ☼ ☼\n" +
                        "☼ #&  ☼\n" +
                        "☼#☼ ☼ ☼\n" +
                        "☼#    ☼\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "\n" +
                        "Bomberman at: [5,5]\n" +
                        "Other bombermans at: []\n" +
                        "Meat choppers at: [[3,3]]\n" +
                        "Destroy walls at: [[1,1], [1,2], [2,3]]\n" +
                        "Bombs at: []\n" +
                        "Blasts: []\n" +
                        "Expected blasts at: []\n" +
                        "Answer: LEFT\n" +
                        "------------------------------------------------------------------------------------",
                String.join("\n", messages));

    }
}
