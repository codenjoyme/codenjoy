package com.codenjoy.dojo.loderunner;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.loderunner.client.Board;
import com.codenjoy.dojo.loderunner.client.ai.AISolver;
import com.codenjoy.dojo.loderunner.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
            public Dice getDice() {
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
                new AISolver(dice),
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
                        "1:Scores: 0\n" +
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
                        "1:Scores: 0\n" +
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
                        "1:Scores: 0\n" +
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
                        "1:Scores: 0\n" +
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
                        "1:Scores: 0\n" +
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
                        "1:Scores: 0\n" +
                        "1:Answer: LEFT\n" +
                        "1:Fire Event: GET_GOLD\n" +
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
                        "1:Scores: 1\n" +
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
                        "1:Scores: 1\n" +
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
                        "1:Scores: 1\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: KILL_HERO\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼   Ѡ   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: \n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:6\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼► «H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: \n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼►« H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: \n" +
                        "1:Fire Event: KILL_HERO\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H   ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼Ѡ  H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: \n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H ► ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼ » H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼ $ ~~~  ☼\n" +
                        "1:☼H##   # ☼\n" +
                        "1:☼H~~~   $☼\n" +
                        "1:☼H   $   ☼\n" +
                        "1:☼H◄  ~~~~☼\n" +
                        "1:☼###H    ☼\n" +
                        "1:☼  »H   $☼\n" +
                        "1:☼########☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
