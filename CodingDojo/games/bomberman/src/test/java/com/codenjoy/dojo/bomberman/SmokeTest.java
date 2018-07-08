package com.codenjoy.dojo.bomberman;

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

        Dice dice = LocalGameRunner.getDice(
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
        assertEquals("DICE:1\n" +
                        "DICE:2\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼☺☼ ☼ ☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [1,2]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: []\n" +
                        "1:Destroy walls at: []\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:3\n" +
                        "Answer: DOWN\n" +
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
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼##   ☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼☺ &  ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [1,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[3,1]]\n" +
                        "1:Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "Answer: ACT\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼##   ☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼☻  & ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [1,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[4,1]]\n" +
                        "1:Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[1,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "DICE:1\n" +
                        "Answer: RIGHT\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼##   ☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼3☺  &☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [2,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,1]]\n" +
                        "1:Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[1,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "DICE:1\n" +
                        "Answer: RIGHT\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼##   ☼\n" +
                        "1:☼ ☼#☼&☼\n" +
                        "1:☼2 ☺  ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [3,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,2]]\n" +
                        "1:Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[1,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "Answer: ACT,RIGHT\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼##  &☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼1  ☺ ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [4,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,3]]\n" +
                        "1:Destroy walls at: [[1,3], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[1,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[1,1], [1,2], [2,1]]\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "Answer: RIGHT\n" +
                        "DICE:1\n" +
                        "Fire Event: KILL_DESTROY_WALL\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼&☼\n" +
                        "1:☼H#   ☼\n" +
                        "1:☼҉☼#☼ ☼\n" +
                        "1:☼҉҉҉҉☺☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,4]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: [[1,1], [1,2], [2,1], [3,1], [4,1]]\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "Answer: UP\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼    &☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ ##  ☼\n" +
                        "1:☼ ☼#☼☺☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,2]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,5]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "Answer: UP\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼&☼\n" +
                        "1:☼ ## ☺☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,3]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,4]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "Answer: ACT,DOWN\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ ## &☼\n" +
                        "1:☼ ☼#☼☺☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,2]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,3]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "Answer: ACT,DOWN\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ ## 3☼\n" +
                        "1:☼ ☼#☼&☼\n" +
                        "1:☼    ☺☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,2]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "1:Bombs at: [[5,3]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[4,3], [5,2], [5,3], [5,4]]\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "Answer: ACT,LEFT\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ ## 2☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼   ☺&☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [4,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[5,1]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "1:Bombs at: [[5,3]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[4,3], [5,2], [5,3], [5,4]]\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "Answer: ACT\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "Fire Event: KILL_BOMBERMAN\n" +
                        "PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ ## 1☼\n" +
                        "1:☼ ☼#☼ ☼\n" +
                        "1:☼  ☺& ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [3,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[4,1]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2], [3,3]]\n" +
                        "1:Bombs at: [[5,3]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[4,3], [5,2], [5,3], [5,4]]\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "Answer: ACT\n" +
                        "DICE:3\n" +
                        "Fire Event: KILL_BOMBERMAN\n" +
                        "PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼    ҉☼\n" +
                        "1:☼ ☼ ☼҉☼\n" +
                        "1:☼ #H҉҉☼\n" +
                        "1:☼ ☼#☼҉☼\n" +
                        "1:☼  &☺҉☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [4,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[3,1]]\n" +
                        "1:Destroy walls at: [[2,3], [3,2]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: [[4,3], [5,1], [5,2], [5,3], [5,4], [5,5]]\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "Answer: ACT\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ #   ☼\n" +
                        "1:☼#☼#☼ ☼\n" +
                        "1:☼ &3☻ ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [4,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[2,1]]\n" +
                        "1:Destroy walls at: [[1,2], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[3,1], [4,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[2,1], [3,1], [3,2], [4,1], [5,1]]\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "Answer: RIGHT\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ #   ☼\n" +
                        "1:☼#☼#☼ ☼\n" +
                        "1:☼& 23☺☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,1]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[1,1]]\n" +
                        "1:Destroy walls at: [[1,2], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[3,1], [4,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[2,1], [3,1], [3,2], [4,1], [5,1]]\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "Answer: UP\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ #   ☼\n" +
                        "1:☼#☼#☼☺☼\n" +
                        "1:☼ &12 ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,2]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[2,1]]\n" +
                        "1:Destroy walls at: [[1,2], [2,3], [3,2]]\n" +
                        "1:Bombs at: [[3,1], [4,1]]\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: [[2,1], [3,1], [3,2], [4,1], [5,1]]\n" +
                        "DICE:2\n" +
                        "Answer: UP\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ #  ☺☼\n" +
                        "1:☼#☼H☼ ☼\n" +
                        "1:☼҉҉x1҉☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,3]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: []\n" +
                        "1:Destroy walls at: [[1,2], [2,3]]\n" +
                        "1:Bombs at: [[4,1]]\n" +
                        "1:Blasts: [[1,1], [2,1], [5,1]]\n" +
                        "1:Expected blasts at: [[3,1], [4,1], [5,1]]\n" +
                        "DICE:2\n" +
                        "Answer: UP\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "Fire Event: KILL_DESTROY_WALL\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼     ☼\n" +
                        "1:☼ ☼ ☼☺☼\n" +
                        "1:☼ #   ☼\n" +
                        "1:☼#☼&☼ ☼\n" +
                        "1:☼  H҉҉☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,4]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[3,2]]\n" +
                        "1:Destroy walls at: [[1,2], [2,3]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: [[4,1], [5,1]]\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:2\n" +
                        "Answer: UP\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "☼☼☼☼☼☼☼\n" +
                        "1:☼    ☺☼\n" +
                        "1:☼ ☼ ☼ ☼\n" +
                        "1:☼ #&  ☼\n" +
                        "1:☼#☼ ☼ ☼\n" +
                        "1:☼#    ☼\n" +
                        "1:☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Bomberman at: [5,5]\n" +
                        "1:Other bombermans at: []\n" +
                        "1:Meat choppers at: [[3,3]]\n" +
                        "1:Destroy walls at: [[1,1], [1,2], [2,3]]\n" +
                        "1:Bombs at: []\n" +
                        "1:Blasts: []\n" +
                        "1:Expected blasts at: []\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "Answer: LEFT\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
