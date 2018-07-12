package com.codenjoy.dojo.loderunner;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.loderunner.client.Board;
import com.codenjoy.dojo.loderunner.client.ai.ApofigSolver;
import com.codenjoy.dojo.loderunner.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmokeTest {
    private int index;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(
                0, 1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼☼☼☼☼" +
                        "☼ $ ~~~» ☼" +
                        "☼H##   # ☼" +
                        "☼H~~~   $☼" +
                        "☼H   $   ☼" +
                        "☼H   ~~~~☼" +
                        "☼###H    ☼" +
                        "☼$  H   $☼" +
                        "☼########☼" +
                        "☼☼☼☼☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ApofigSolver(dice),
                new Board());

        // then
        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~» ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H  [$   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼$  H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~<  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H  ►~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼$  H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~<~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###Y    ☼\n" +
                        "1:☼$  H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ <~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼$  Y   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##«  # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼$ ◄H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~<   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼$◄ H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: LEFT\n" +
                        "Fire Event: GET_GOLD\n" +
                        "DICE:6\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H  «$   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼◄  H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H  «~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼ ► H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###Q    ☼\n" +
                        "1:☼  ►H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: KILL_HERO\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:6\n" +
                        "DICE:6\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~ [ $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼   Q   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $[  ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###Q    ☼\n" +
                        "1:☼   H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H  »~}~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼   H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   >~~~☼\n" +
                        "1:☼###H [  ☼\n" +
                        "1:☼   H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~>~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼   H ► $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~>~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼   H  ►$☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: GET_GOLD\n" +
                        "DICE:6\n" +
                        "DICE:6\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
