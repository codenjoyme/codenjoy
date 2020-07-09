package com.codenjoy.dojo.snakebattle;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.snakebattle.client.Board;
import com.codenjoy.dojo.snakebattle.client.ai.AISolver;
import com.codenjoy.dojo.snakebattle.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {

    private Dice dice;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 20;

        Dice dice = LocalGameRunner.getDice(
                // random numbers
                0, 1, 2, 4, 2, 4, 2, 4, 0, 4, 2, 4, 0, 1, 4, 4, 2, 4, 3, 4, 3, 4,
                0, 2, 2, 1, 2, 1, 2, 1, 0, 1, 2, 1, 0, 2, 1, 1, 2, 1, 3, 1, 3, 1,
                0, 1, 4, 2, 4, 2, 4, 2, 0, 2, 4, 2, 0, 1, 2, 2, 4, 2, 1, 2, 1, 2,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                1, 3, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 3, 1, 1, 0, 1, 0, 1, 0, 1,
                0, 1, 4, 2, 4, 2, 4, 2, 0, 2, 4, 2, 0, 1, 2, 2, 4, 2, 1, 2, 1, 2,
                0, 1, 4, 2, 4, 2, 4, 2, 0, 2, 4, 2, 0, 1, 2, 2, 4, 2, 1, 2, 1, 2,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 1, 0, 1,
                1, 3, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 3, 1, 1, 0, 1, 0, 1, 0, 1,
                0, 0, 1, 2, 1, 2, 1, 2, 0, 2, 1, 2, 0, 0, 2, 2, 1, 2, 0, 2, 0, 2,
                2, 2, 1, 2, 1, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 2, 1, 2, 0, 2, 0, 2,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                0, 2, 1, 2, 1, 2, 1, 2, 0, 2, 1, 2, 0, 2, 2, 2, 1, 2, 0, 2, 0, 2,
                0, 1, 1, 3, 1, 3, 1, 3, 0, 3, 1, 3, 0, 1, 3, 3, 1, 3, 3, 3, 3, 3,
                0, 0, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 0, 0, 2, 2, 2, 2, 0, 2, 0, 2,
                1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 0, 1, 0, 1,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                0, 1, 1, 3, 1, 3, 1, 3, 0, 3, 1, 3, 0, 1, 3, 3, 1, 3, 3, 3, 3, 3,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                1, 3, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 3, 1, 1, 0, 1, 0, 1, 0, 1,
                1, 2, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 2, 0, 0, 1, 0, 0, 0, 0, 0,
                1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 3, 0, 3, 0,
                0, 1, 1, 3, 1, 3, 1, 3, 0, 3, 1, 3, 0, 1, 3, 3, 1, 3, 3, 3, 3, 3,
                0, 1, 1, 3, 1, 3, 1, 3, 0, 1, 3, 1, 3, 1, 0, 3, 1, 3, 3, 3, 3, 3,
                1, 2, 1, 0, 1, 0, 1, 0, 1, 3, 2, 3, 2, 3, 1, 0, 1, 0, 0, 0, 0, 0,
                1, 2, 1, 0, 1, 0, 1, 0, 1, 3, 2, 3, 2, 3, 1, 0, 1, 0, 0, 0, 0, 0,
                0, 2, 1, 2, 1, 2, 1, 2, 0, 3, 2, 3, 2, 3, 1, 2, 1, 2, 0, 2, 0, 2,
                1, 1, 3, 1, 3, 1, 0, 1, 1, 1, 7, 1, 2, 1, 0, 1, 3, 1, 0, 1, 0, 1,
                1, 3, 2, 3, 2, 3, 1, 1, 1, 3, 1, 3, 1, 3, 0, 1, 3, 1, 0, 1, 0, 1,
                0, 3, 2, 3, 2, 3, 1, 2, 0, 3, 2, 3, 2, 3, 0, 2, 1, 2, 0, 2, 0, 2,
                1, 3, 2, 3, 2, 3, 1, 2, 1, 1, 3, 1, 3, 1, 0, 2, 2, 2, 2, 2, 2, 2,
                0, 1, 7, 1, 2, 1, 0, 1, 0, 3, 2, 3, 2, 3, 1, 1, 0, 1, 0, 1, 0, 1,
                0, 3, 1, 3, 1, 3, 0, 1, 0, 3, 2, 3, 2, 3, 1, 1, 0, 1, 0, 1, 0, 1,
                1, 3, 2, 3, 2, 3, 0, 2, 1, 3, 2, 3, 2, 3, 1, 2, 0, 2, 1, 2, 1, 2,
                1, 3, 7, 3, 2, 3, 0, 2, 1, 1, 7, 1, 2, 1, 0, 2, 0, 2, 1, 2, 1, 2,
                0, 3, 2, 5, 5, 3, 5, 1, 0, 3, 1, 3, 1, 1, 3, 0, 1, 3, 1, 3, 2, 1,
                0, 3, 7, 3, 2, 3, 0, 1, 0, 3, 2, 3, 2, 1, 0, 1, 3, 2, 3, 2, 2, 1,
                0, 1, 4, 5, 5, 1, 5, 1, 0, 3, 7, 3, 2, 1, 0, 1, 3, 2, 3, 2, 2, 1,
                0, 1, 3, 1, 3, 1, 3, 1, 0, 3, 2, 5, 5, 1, 2, 0, 3, 2, 3, 2, 1, 1,
                1, 4, 2, 3, 2, 3, 2, 3, 1, 3, 7, 3, 2, 0, 1, 1, 1, 7, 1, 2, 2, 3,
                1, 4, 2, 3, 2, 3, 2, 3, 1, 1, 4, 5, 5, 1, 1, 1, 3, 1, 3, 1, 2, 3,
                1, 1, 2, 3, 2, 3, 2, 3, 1, 3, 2, 3, 1, 1, 2, 0, 3, 2, 3, 2, 2, 3,
                0, 1, 2, 1, 7, 1, 2, 1, 0, 1, 2, 1, 0, 1, 2, 1, 1, 3, 1, 3, 1, 1,
                3, 4, 1, 3, 1, 3, 1, 3, 0, 3, 1, 3, 0, 0, 1, 0, 3, 2, 3, 2, 2, 3,
                0, 1, 2, 3, 2, 3, 2, 3, 0, 3, 2, 3, 5, 0, 1, 0, 3, 2, 3, 2, 2, 3,
                0, 4, 2, 3, 7, 3, 2, 3, 0, 3, 2, 3, 5, 0, 2, 1, 3, 2, 3, 2, 2, 3,
                3, 1, 2, 3, 2, 5, 5, 3, 5, 3, 2, 3, 0, 1, 3, 3, 2, 3, 5, 5, 5, 3,
                0, 4, 2, 3, 7, 3, 2, 3, 0, 3, 2, 3, 5, 1, 3, 3, 2, 3, 2, 3, 5, 3,
                3, 1, 2, 1, 4, 5, 5, 1, 5, 1, 2, 1, 0, 1, 1, 1, 2, 1, 5, 1, 5, 1,
                0, 4, 2, 1, 2, 1, 2, 1, 0, 1, 5, 1, 5, 1, 1, 1, 2, 1, 2, 1, 5, 1,
                0, 1, 2, 1, 2, 1, 2, 1, 0, 1, 5, 1, 5, 1, 1, 1, 2, 1, 2, 1, 2, 1,
                1, 3, 4, 4, 4, 4, 4, 4, 1, 4, 5, 4, 5, 3, 4, 4, 4, 4, 3, 4, 3, 4,
                1, 3, 2, 1, 2, 1, 2, 1, 1, 1, 5, 1, 1, 3, 1, 1, 2, 1, 4, 1, 4, 1,
                0, 1, 3, 1, 3, 1, 3, 1, 0, 1, 5, 1, 0, 1, 1, 1, 3, 1, 1, 1, 1, 1
        );

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼☼☼☼" +
                        "☼☼      ☼" +
                        "☼#      ☼" +
                        "☼☼ ☼# ○ ☼" +
                        "☼☼○    $☼" +
                        "☼☼   ●  ☼" +
                        "☼#      ☼" +
                        "☼☼  ●   ☼" +
                        "☼☼☼☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new LinkedList<Solver>(){{
                    add(new AISolver(dice));
                    add(new AISolver(dice));
                }},
                new LinkedList<ClientBoard>(){{
                    add(new Board());
                    add(new Board());
                }});

        // then
        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:~&      ☼\n" +
                        "1:☼☼ *ø ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:*ø      ☼\n" +
                        "2:☼☼ ~& ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "1:Fire Event: [....4....]\n" +
                        "2:Fire Event: [....4....]\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:~&      ☼\n" +
                        "1:☼☼ *ø ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:*ø      ☼\n" +
                        "2:☼☼ ~& ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "1:Fire Event: [...3...]\n" +
                        "2:Fire Event: [...3...]\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:~&      ☼\n" +
                        "1:☼☼ *ø ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:*ø      ☼\n" +
                        "2:☼☼ ~& ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "1:Fire Event: [..2..]\n" +
                        "2:Fire Event: [..2..]\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:~&      ☼\n" +
                        "1:☼☼ *ø ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:*ø      ☼\n" +
                        "2:☼☼ ~& ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "1:Fire Event: [.1.]\n" +
                        "2:Fire Event: [.1.]\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:~&      ☼\n" +
                        "1:☼☼ *ø ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:*ø      ☼\n" +
                        "2:☼☼ ~& ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "1:Fire Event: START\n" +
                        "1:Fire Event: [Round 1]\n" +
                        "2:Fire Event: START\n" +
                        "2:Fire Event: [Round 1]\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:╘►      ☼\n" +
                        "1:☼☼ ×> ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:×>      ☼\n" +
                        "2:☼☼ ╘► ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼╘►     ☼\n" +
                        "1:☼☼ ☼×>○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼×>     ☼\n" +
                        "2:☼☼ ☼╘►○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: RIGHT\n" +
                        "2:Fire Event: APPLE\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#╓     ☼\n" +
                        "1:☼☼▼☼×─> ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼# ○    ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#æ     ☼\n" +
                        "2:☼☼˅☼╘═► ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼# ○    ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 1\n" +
                        "2:Answer: DOWN\n" +
                        "1:Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#╓     ☼\n" +
                        "1:☼☼║☼#×┐ ☼\n" +
                        "1:☼☼▼   ˅$☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#○○    ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#æ     ☼\n" +
                        "2:☼☼│☼#╘╗ ☼\n" +
                        "2:☼☼˅   ▼$☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#○○    ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "2:Scores: 1\n" +
                        "2:Answer: RIGHT, ACT\n" +
                        "2:Fire Event: GOLD\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼╓☼# æ ☼\n" +
                        "1:☼☼║   └>☼\n" +
                        "1:☼☼▼○ ●  ☼\n" +
                        "1:☼#○○    ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼æ☼# ╓ ☼\n" +
                        "2:☼☼│   ╚►☼\n" +
                        "2:☼☼˅○ ●  ☼\n" +
                        "2:☼#○○    ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 11\n" +
                        "2:Answer: DOWN\n" +
                        "1:Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼╓☼#   ☼\n" +
                        "1:☼☼║   ×┐☼\n" +
                        "1:☼☼║○ ● ˅☼\n" +
                        "1:☼#▼○    ☼\n" +
                        "1:☼☼○○●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "1:Scores: 2\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼æ☼#   ☼\n" +
                        "2:☼☼│   ╘╗☼\n" +
                        "2:☼☼│○ ● ▼☼\n" +
                        "2:☼#˅○    ☼\n" +
                        "2:☼☼○○●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "2:Scores: 11\n" +
                        "2:Answer: LEFT\n" +
                        "1:Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:7\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼╓☼#   ☼\n" +
                        "1:☼☼║    æ☼\n" +
                        "1:☼☼║○ ●<┘☼\n" +
                        "1:☼#║○    ☼\n" +
                        "1:☼☼▼○●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:7\n" +
                        "DICE_CORRECTED < 4 :3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:5\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼æ☼#   ☼\n" +
                        "2:☼☼│    ╓☼\n" +
                        "2:☼☼│○ ●◄╝☼\n" +
                        "2:☼#│○    ☼\n" +
                        "2:☼☼˅○●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:5\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "2:Scores: 11\n" +
                        "2:Answer: DOWN\n" +
                        "1:Fire Event: APPLE\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼╓☼#○  ☼\n" +
                        "1:☼☼║     ☼\n" +
                        "1:☼☼║○ ●┌ö☼\n" +
                        "1:☼#║○  ˅ ☼\n" +
                        "1:☼☼╚►●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 3 :2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:7\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "1:Scores: 4\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼æ☼#○  ☼\n" +
                        "2:☼☼│     ☼\n" +
                        "2:☼☼│○ ●╔╕☼\n" +
                        "2:☼#│○  ▼ ☼\n" +
                        "2:☼☼└>●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:7\n" +
                        "DICE_CORRECTED < 4 :3\n" +
                        "2:Scores: 11\n" +
                        "2:Answer: LEFT, ACT\n" +
                        "1:Fire Event: APPLE\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼╓☼#○  ☼\n" +
                        "1:☼☼║     ☼\n" +
                        "1:☼☼║○ ●æ ☼\n" +
                        "1:☼#║▲ <┘ ☼\n" +
                        "1:☼☼╚╝●○  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "1:Scores: 5\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼æ☼#○  ☼\n" +
                        "2:☼☼│     ☼\n" +
                        "2:☼☼│○ ●╓ ☼\n" +
                        "2:☼#│˄ ◄╝ ☼\n" +
                        "2:☼☼└┘●○  ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "2:Scores: 11\n" +
                        "2:Answer: DOWN\n" +
                        "1:Fire Event: APPLE\n" +
                        "2:Fire Event: APPLE\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼╓☼#○  ☼\n" +
                        "1:☼☼║     ☼\n" +
                        "1:☼☼║▲ ●æ○☼\n" +
                        "1:☼#║║ ┌┘ ☼\n" +
                        "1:☼☼╚╝●˅  ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 3 :2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 4 :1\n" +
                        "DICE:5\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:5\n" +
                        "1:Scores: 6\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼æ☼#○  ☼\n" +
                        "2:☼☼│     ☼\n" +
                        "2:☼☼│˄ ●╓○☼\n" +
                        "2:☼#││ ╔╝ ☼\n" +
                        "2:☼☼└┘●▼  ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:5\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:7\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 4 :1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 3 :2\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 4 :1\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 3 :2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "2:Scores: 12\n" +
                        "2:Answer: RIGHT\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:4\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼ ☼#○  ☼\n" +
                        "1:☼☼╓▲○   ☼\n" +
                        "1:☼☼║║ ● ○☼\n" +
                        "1:☼#║║ ┌ö ☼\n" +
                        "1:☼☼╚╝●└> ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 3 :2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:5\n" +
                        "DICE_CORRECTED < 3 :2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:4\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Scores: 6\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼ ☼#○  ☼\n" +
                        "2:☼☼æ˄○   ☼\n" +
                        "2:☼☼││ ● ○☼\n" +
                        "2:☼#││ ╔╕ ☼\n" +
                        "2:☼☼└┘●╚► ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 12\n" +
                        "2:Answer: UP\n" +
                        "1:Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼ ☼#○  ☼\n" +
                        "1:☼☼╓╔►   ☼\n" +
                        "1:☼☼║║ ● ○☼\n" +
                        "1:☼#║║ æ˄ ☼\n" +
                        "1:☼☼╚╝●└┘ ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 7\n" +
                        "1:Answer: RIGHT, ACT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼ ☼#○  ☼\n" +
                        "2:☼☼æ┌>   ☼\n" +
                        "2:☼☼││ ● ○☼\n" +
                        "2:☼#││ ╓▲ ☼\n" +
                        "2:☼☼└┘●╚╝ ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 12\n" +
                        "2:Answer: UP, ACT\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼ ☼#○  ☼\n" +
                        "1:☼☼ ╔═►  ☼\n" +
                        "1:☼☼╓║ ●˄○☼\n" +
                        "1:☼#║║  │ ☼\n" +
                        "1:☼☼╚╝●×┘ ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 7\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼ ☼#○  ☼\n" +
                        "2:☼☼ ┌─>  ☼\n" +
                        "2:☼☼æ│ ●▲○☼\n" +
                        "2:☼#││  ║ ☼\n" +
                        "2:☼☼└┘●╘╝ ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 12\n" +
                        "2:Answer: RIGHT\n" +
                        "1:Fire Event: APPLE\n" +
                        "2:Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼ ☼#▲  ☼\n" +
                        "1:☼☼ ╔═╝  ☼\n" +
                        "1:☼☼╓║ ●┌>☼\n" +
                        "1:☼#║║  │ ☼\n" +
                        "1:☼☼╚╝●×┘ ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 8\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼ ☼#˄  ☼\n" +
                        "2:☼☼ ┌─┘  ☼\n" +
                        "2:☼☼æ│ ●╔►☼\n" +
                        "2:☼#││  ║ ☼\n" +
                        "2:☼☼└┘●╘╝ ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 13\n" +
                        "2:Answer: UP\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼ ○    ☼\n" +
                        "1:☼#   ▲  ☼\n" +
                        "1:☼☼ ☼#║  ☼\n" +
                        "1:☼☼ ╔═╝ ˄☼\n" +
                        "1:☼☼ ║ ●┌┘☼\n" +
                        "1:☼#╓║  │ ☼\n" +
                        "1:☼☼╚╝● ¤ ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 8\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼ ○    ☼\n" +
                        "2:☼#   ˄  ☼\n" +
                        "2:☼☼ ☼#│  ☼\n" +
                        "2:☼☼ ┌─┘ ▲☼\n" +
                        "2:☼☼ │ ●╔╝☼\n" +
                        "2:☼#æ│  ║ ☼\n" +
                        "2:☼☼└┘● ╙ ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Scores: 13\n" +
                        "2:Answer: UP\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
