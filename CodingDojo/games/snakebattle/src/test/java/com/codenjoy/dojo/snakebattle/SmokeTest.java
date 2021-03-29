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
import com.codenjoy.dojo.snakebattle.services.GameSettings;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.LEVEL_MAP;
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
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

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
            public GameSettings getSettings() {
                return super.getSettings()
                        .string(LEVEL_MAP,
                                "☼☼☼☼☼☼☼☼☼" +
                                "☼☼      ☼" +
                                "☼#      ☼" +
                                "☼☼ ☼# ○ ☼" +
                                "☼☼○    $☼" +
                                "☼☼   ●  ☼" +
                                "☼#      ☼" +
                                "☼☼  ●   ☼" +
                                "☼☼☼☼☼☼☼☼☼");
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
        assertEquals("1: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "1: 1:☼☼      ☼\n" +
                    "1: 1:~&      ☼\n" +
                    "1: 1:☼☼ *ø ○ ☼\n" +
                    "1: 1:☼☼○    $☼\n" +
                    "1: 1:☼☼   ●  ☼\n" +
                    "1: 1:☼#      ☼\n" +
                    "1: 1:☼☼  ●   ☼\n" +
                    "1: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "1: 1:\n" +
                    "1: 1:Scores: 0\n" +
                    "1: 1:Answer: RIGHT\n" +
                    "1: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "1: 2:☼☼      ☼\n" +
                    "1: 2:*ø      ☼\n" +
                    "1: 2:☼☼ ~& ○ ☼\n" +
                    "1: 2:☼☼○    $☼\n" +
                    "1: 2:☼☼   ●  ☼\n" +
                    "1: 2:☼#      ☼\n" +
                    "1: 2:☼☼  ●   ☼\n" +
                    "1: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "1: 2:\n" +
                    "1: 2:Scores: 0\n" +
                    "1: 2:Answer: RIGHT\n" +
                    "1: 1:Fire Event: [....4....]\n" +
                    "1: 2:Fire Event: [....4....]\n" +
                    "------------------------------------------\n" +
                    "2: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "2: 1:☼☼      ☼\n" +
                    "2: 1:~&      ☼\n" +
                    "2: 1:☼☼ *ø ○ ☼\n" +
                    "2: 1:☼☼○    $☼\n" +
                    "2: 1:☼☼   ●  ☼\n" +
                    "2: 1:☼#      ☼\n" +
                    "2: 1:☼☼  ●   ☼\n" +
                    "2: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "2: 1:\n" +
                    "2: 1:Scores: 0\n" +
                    "2: 1:Answer: RIGHT\n" +
                    "2: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "2: 2:☼☼      ☼\n" +
                    "2: 2:*ø      ☼\n" +
                    "2: 2:☼☼ ~& ○ ☼\n" +
                    "2: 2:☼☼○    $☼\n" +
                    "2: 2:☼☼   ●  ☼\n" +
                    "2: 2:☼#      ☼\n" +
                    "2: 2:☼☼  ●   ☼\n" +
                    "2: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "2: 2:\n" +
                    "2: 2:Scores: 0\n" +
                    "2: 2:Answer: RIGHT\n" +
                    "2: 1:Fire Event: [...3...]\n" +
                    "2: 2:Fire Event: [...3...]\n" +
                    "------------------------------------------\n" +
                    "3: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "3: 1:☼☼      ☼\n" +
                    "3: 1:~&      ☼\n" +
                    "3: 1:☼☼ *ø ○ ☼\n" +
                    "3: 1:☼☼○    $☼\n" +
                    "3: 1:☼☼   ●  ☼\n" +
                    "3: 1:☼#      ☼\n" +
                    "3: 1:☼☼  ●   ☼\n" +
                    "3: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "3: 1:\n" +
                    "3: 1:Scores: 0\n" +
                    "3: 1:Answer: RIGHT\n" +
                    "3: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "3: 2:☼☼      ☼\n" +
                    "3: 2:*ø      ☼\n" +
                    "3: 2:☼☼ ~& ○ ☼\n" +
                    "3: 2:☼☼○    $☼\n" +
                    "3: 2:☼☼   ●  ☼\n" +
                    "3: 2:☼#      ☼\n" +
                    "3: 2:☼☼  ●   ☼\n" +
                    "3: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "3: 2:\n" +
                    "3: 2:Scores: 0\n" +
                    "3: 2:Answer: RIGHT\n" +
                    "3: 1:Fire Event: [..2..]\n" +
                    "3: 2:Fire Event: [..2..]\n" +
                    "------------------------------------------\n" +
                    "4: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "4: 1:☼☼      ☼\n" +
                    "4: 1:~&      ☼\n" +
                    "4: 1:☼☼ *ø ○ ☼\n" +
                    "4: 1:☼☼○    $☼\n" +
                    "4: 1:☼☼   ●  ☼\n" +
                    "4: 1:☼#      ☼\n" +
                    "4: 1:☼☼  ●   ☼\n" +
                    "4: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "4: 1:\n" +
                    "4: 1:Scores: 0\n" +
                    "4: 1:Answer: RIGHT\n" +
                    "4: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "4: 2:☼☼      ☼\n" +
                    "4: 2:*ø      ☼\n" +
                    "4: 2:☼☼ ~& ○ ☼\n" +
                    "4: 2:☼☼○    $☼\n" +
                    "4: 2:☼☼   ●  ☼\n" +
                    "4: 2:☼#      ☼\n" +
                    "4: 2:☼☼  ●   ☼\n" +
                    "4: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "4: 2:\n" +
                    "4: 2:Scores: 0\n" +
                    "4: 2:Answer: RIGHT\n" +
                    "4: 1:Fire Event: [.1.]\n" +
                    "4: 2:Fire Event: [.1.]\n" +
                    "------------------------------------------\n" +
                    "5: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "5: 1:☼☼      ☼\n" +
                    "5: 1:~&      ☼\n" +
                    "5: 1:☼☼ *ø ○ ☼\n" +
                    "5: 1:☼☼○    $☼\n" +
                    "5: 1:☼☼   ●  ☼\n" +
                    "5: 1:☼#      ☼\n" +
                    "5: 1:☼☼  ●   ☼\n" +
                    "5: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "5: 1:\n" +
                    "5: 1:Scores: 0\n" +
                    "5: 1:Answer: RIGHT\n" +
                    "5: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "5: 2:☼☼      ☼\n" +
                    "5: 2:*ø      ☼\n" +
                    "5: 2:☼☼ ~& ○ ☼\n" +
                    "5: 2:☼☼○    $☼\n" +
                    "5: 2:☼☼   ●  ☼\n" +
                    "5: 2:☼#      ☼\n" +
                    "5: 2:☼☼  ●   ☼\n" +
                    "5: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "5: 2:\n" +
                    "5: 2:Scores: 0\n" +
                    "5: 2:Answer: RIGHT\n" +
                    "5: 1:Fire Event: START\n" +
                    "5: 1:Fire Event: [Round 1]\n" +
                    "5: 2:Fire Event: START\n" +
                    "5: 2:Fire Event: [Round 1]\n" +
                    "------------------------------------------\n" +
                    "6: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "6: 1:☼☼      ☼\n" +
                    "6: 1:╘►      ☼\n" +
                    "6: 1:☼☼ ×> ○ ☼\n" +
                    "6: 1:☼☼○    $☼\n" +
                    "6: 1:☼☼   ●  ☼\n" +
                    "6: 1:☼#      ☼\n" +
                    "6: 1:☼☼  ●   ☼\n" +
                    "6: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "6: 1:\n" +
                    "6: 1:Scores: 0\n" +
                    "6: 1:Answer: RIGHT\n" +
                    "6: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "6: 2:☼☼      ☼\n" +
                    "6: 2:×>      ☼\n" +
                    "6: 2:☼☼ ╘► ○ ☼\n" +
                    "6: 2:☼☼○    $☼\n" +
                    "6: 2:☼☼   ●  ☼\n" +
                    "6: 2:☼#      ☼\n" +
                    "6: 2:☼☼  ●   ☼\n" +
                    "6: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "6: 2:\n" +
                    "6: 2:Scores: 0\n" +
                    "6: 2:Answer: RIGHT\n" +
                    "------------------------------------------\n" +
                    "7: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "7: 1:☼☼      ☼\n" +
                    "7: 1:☼╘►     ☼\n" +
                    "7: 1:☼☼ ☼×>○ ☼\n" +
                    "7: 1:☼☼○    $☼\n" +
                    "7: 1:☼☼   ●  ☼\n" +
                    "7: 1:☼#      ☼\n" +
                    "7: 1:☼☼○ ●   ☼\n" +
                    "7: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "7: 1:\n" +
                    "7: 1:Scores: 0\n" +
                    "7: 1:Answer: DOWN, ACT\n" +
                    "7: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "7: 2:☼☼      ☼\n" +
                    "7: 2:☼×>     ☼\n" +
                    "7: 2:☼☼ ☼╘►○ ☼\n" +
                    "7: 2:☼☼○    $☼\n" +
                    "7: 2:☼☼   ●  ☼\n" +
                    "7: 2:☼#      ☼\n" +
                    "7: 2:☼☼○ ●   ☼\n" +
                    "7: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "7: 2:\n" +
                    "7: 2:Scores: 0\n" +
                    "7: 2:Answer: RIGHT\n" +
                    "7: 2:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "8: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "8: 1:☼☼      ☼\n" +
                    "8: 1:☼#╓     ☼\n" +
                    "8: 1:☼☼▼☼×─> ☼\n" +
                    "8: 1:☼☼○    $☼\n" +
                    "8: 1:☼☼   ●  ☼\n" +
                    "8: 1:☼# ○    ☼\n" +
                    "8: 1:☼☼○ ●   ☼\n" +
                    "8: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "8: 1:\n" +
                    "8: 1:Scores: 0\n" +
                    "8: 1:Answer: DOWN\n" +
                    "8: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "8: 2:☼☼      ☼\n" +
                    "8: 2:☼#æ     ☼\n" +
                    "8: 2:☼☼˅☼╘═► ☼\n" +
                    "8: 2:☼☼○    $☼\n" +
                    "8: 2:☼☼   ●  ☼\n" +
                    "8: 2:☼# ○    ☼\n" +
                    "8: 2:☼☼○ ●   ☼\n" +
                    "8: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "8: 2:\n" +
                    "8: 2:Scores: 1\n" +
                    "8: 2:Answer: DOWN\n" +
                    "8: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "9: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "9: 1:☼☼      ☼\n" +
                    "9: 1:☼#╓     ☼\n" +
                    "9: 1:☼☼║☼#×┐ ☼\n" +
                    "9: 1:☼☼▼   ˅$☼\n" +
                    "9: 1:☼☼   ●  ☼\n" +
                    "9: 1:☼#○○    ☼\n" +
                    "9: 1:☼☼○ ●   ☼\n" +
                    "9: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "9: 1:\n" +
                    "9: 1:Scores: 1\n" +
                    "9: 1:Answer: DOWN, ACT\n" +
                    "9: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "9: 2:☼☼      ☼\n" +
                    "9: 2:☼#æ     ☼\n" +
                    "9: 2:☼☼│☼#╘╗ ☼\n" +
                    "9: 2:☼☼˅   ▼$☼\n" +
                    "9: 2:☼☼   ●  ☼\n" +
                    "9: 2:☼#○○    ☼\n" +
                    "9: 2:☼☼○ ●   ☼\n" +
                    "9: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "9: 2:\n" +
                    "9: 2:Scores: 1\n" +
                    "9: 2:Answer: RIGHT, ACT\n" +
                    "9: 2:Fire Event: GOLD\n" +
                    "------------------------------------------\n" +
                    "10: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "10: 1:☼☼      ☼\n" +
                    "10: 1:☼#      ☼\n" +
                    "10: 1:☼☼╓☼# æ ☼\n" +
                    "10: 1:☼☼║   └>☼\n" +
                    "10: 1:☼☼▼○ ●  ☼\n" +
                    "10: 1:☼#○○    ☼\n" +
                    "10: 1:☼☼○ ●   ☼\n" +
                    "10: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "10: 1:\n" +
                    "10: 1:Scores: 1\n" +
                    "10: 1:Answer: DOWN\n" +
                    "10: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "10: 2:☼☼      ☼\n" +
                    "10: 2:☼#      ☼\n" +
                    "10: 2:☼☼æ☼# ╓ ☼\n" +
                    "10: 2:☼☼│   ╚►☼\n" +
                    "10: 2:☼☼˅○ ●  ☼\n" +
                    "10: 2:☼#○○    ☼\n" +
                    "10: 2:☼☼○ ●   ☼\n" +
                    "10: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "10: 2:\n" +
                    "10: 2:Scores: 11\n" +
                    "10: 2:Answer: DOWN\n" +
                    "10: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "11: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "11: 1:☼☼      ☼\n" +
                    "11: 1:☼#      ☼\n" +
                    "11: 1:☼☼╓☼#   ☼\n" +
                    "11: 1:☼☼║   ×┐☼\n" +
                    "11: 1:☼☼║○ ● ˅☼\n" +
                    "11: 1:☼#▼○    ☼\n" +
                    "11: 1:☼☼○○●   ☼\n" +
                    "11: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "11: 1:\n" +
                    "11: 1:Scores: 2\n" +
                    "11: 1:Answer: DOWN, ACT\n" +
                    "11: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "11: 2:☼☼      ☼\n" +
                    "11: 2:☼#      ☼\n" +
                    "11: 2:☼☼æ☼#   ☼\n" +
                    "11: 2:☼☼│   ╘╗☼\n" +
                    "11: 2:☼☼│○ ● ▼☼\n" +
                    "11: 2:☼#˅○    ☼\n" +
                    "11: 2:☼☼○○●   ☼\n" +
                    "11: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "11: 2:\n" +
                    "11: 2:Scores: 11\n" +
                    "11: 2:Answer: LEFT\n" +
                    "11: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "12: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "12: 1:☼☼ ○    ☼\n" +
                    "12: 1:☼#      ☼\n" +
                    "12: 1:☼☼╓☼#   ☼\n" +
                    "12: 1:☼☼║    æ☼\n" +
                    "12: 1:☼☼║○ ●<┘☼\n" +
                    "12: 1:☼#║○    ☼\n" +
                    "12: 1:☼☼▼○●   ☼\n" +
                    "12: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "12: 1:\n" +
                    "12: 1:Scores: 3\n" +
                    "12: 1:Answer: RIGHT\n" +
                    "12: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "12: 2:☼☼ ○    ☼\n" +
                    "12: 2:☼#      ☼\n" +
                    "12: 2:☼☼æ☼#   ☼\n" +
                    "12: 2:☼☼│    ╓☼\n" +
                    "12: 2:☼☼│○ ●◄╝☼\n" +
                    "12: 2:☼#│○    ☼\n" +
                    "12: 2:☼☼˅○●   ☼\n" +
                    "12: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "12: 2:\n" +
                    "12: 2:Scores: 11\n" +
                    "12: 2:Answer: DOWN\n" +
                    "12: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "13: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "13: 1:☼☼ ○    ☼\n" +
                    "13: 1:☼#      ☼\n" +
                    "13: 1:☼☼╓☼#○  ☼\n" +
                    "13: 1:☼☼║     ☼\n" +
                    "13: 1:☼☼║○ ●┌ö☼\n" +
                    "13: 1:☼#║○  ˅ ☼\n" +
                    "13: 1:☼☼╚►●   ☼\n" +
                    "13: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "13: 1:\n" +
                    "13: 1:Scores: 4\n" +
                    "13: 1:Answer: UP\n" +
                    "13: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "13: 2:☼☼ ○    ☼\n" +
                    "13: 2:☼#      ☼\n" +
                    "13: 2:☼☼æ☼#○  ☼\n" +
                    "13: 2:☼☼│     ☼\n" +
                    "13: 2:☼☼│○ ●╔╕☼\n" +
                    "13: 2:☼#│○  ▼ ☼\n" +
                    "13: 2:☼☼└>●   ☼\n" +
                    "13: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "13: 2:\n" +
                    "13: 2:Scores: 11\n" +
                    "13: 2:Answer: LEFT, ACT\n" +
                    "13: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "14: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "14: 1:☼☼ ○    ☼\n" +
                    "14: 1:☼#      ☼\n" +
                    "14: 1:☼☼╓☼#○  ☼\n" +
                    "14: 1:☼☼║     ☼\n" +
                    "14: 1:☼☼║○ ●æ ☼\n" +
                    "14: 1:☼#║▲ <┘ ☼\n" +
                    "14: 1:☼☼╚╝●○  ☼\n" +
                    "14: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "14: 1:\n" +
                    "14: 1:Scores: 5\n" +
                    "14: 1:Answer: UP\n" +
                    "14: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "14: 2:☼☼ ○    ☼\n" +
                    "14: 2:☼#      ☼\n" +
                    "14: 2:☼☼æ☼#○  ☼\n" +
                    "14: 2:☼☼│     ☼\n" +
                    "14: 2:☼☼│○ ●╓ ☼\n" +
                    "14: 2:☼#│˄ ◄╝ ☼\n" +
                    "14: 2:☼☼└┘●○  ☼\n" +
                    "14: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "14: 2:\n" +
                    "14: 2:Scores: 11\n" +
                    "14: 2:Answer: DOWN\n" +
                    "14: 1:Fire Event: APPLE\n" +
                    "14: 2:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "15: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "15: 1:☼☼ ○    ☼\n" +
                    "15: 1:☼#      ☼\n" +
                    "15: 1:☼☼╓☼#○  ☼\n" +
                    "15: 1:☼☼║     ☼\n" +
                    "15: 1:☼☼║▲ ●æ○☼\n" +
                    "15: 1:☼#║║ ┌┘ ☼\n" +
                    "15: 1:☼☼╚╝●˅  ☼\n" +
                    "15: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "15: 1:\n" +
                    "15: 1:Scores: 6\n" +
                    "15: 1:Answer: UP\n" +
                    "15: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "15: 2:☼☼ ○    ☼\n" +
                    "15: 2:☼#      ☼\n" +
                    "15: 2:☼☼æ☼#○  ☼\n" +
                    "15: 2:☼☼│     ☼\n" +
                    "15: 2:☼☼│˄ ●╓○☼\n" +
                    "15: 2:☼#││ ╔╝ ☼\n" +
                    "15: 2:☼☼└┘●▼  ☼\n" +
                    "15: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "15: 2:\n" +
                    "15: 2:Scores: 12\n" +
                    "15: 2:Answer: RIGHT\n" +
                    "------------------------------------------\n" +
                    "16: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "16: 1:☼☼ ○    ☼\n" +
                    "16: 1:☼#      ☼\n" +
                    "16: 1:☼☼ ☼#○  ☼\n" +
                    "16: 1:☼☼╓▲○   ☼\n" +
                    "16: 1:☼☼║║ ● ○☼\n" +
                    "16: 1:☼#║║ ┌ö ☼\n" +
                    "16: 1:☼☼╚╝●└> ☼\n" +
                    "16: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "16: 1:\n" +
                    "16: 1:Scores: 6\n" +
                    "16: 1:Answer: RIGHT\n" +
                    "16: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "16: 2:☼☼ ○    ☼\n" +
                    "16: 2:☼#      ☼\n" +
                    "16: 2:☼☼ ☼#○  ☼\n" +
                    "16: 2:☼☼æ˄○   ☼\n" +
                    "16: 2:☼☼││ ● ○☼\n" +
                    "16: 2:☼#││ ╔╕ ☼\n" +
                    "16: 2:☼☼└┘●╚► ☼\n" +
                    "16: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "16: 2:\n" +
                    "16: 2:Scores: 12\n" +
                    "16: 2:Answer: UP\n" +
                    "16: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "17: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "17: 1:☼☼ ○    ☼\n" +
                    "17: 1:☼#      ☼\n" +
                    "17: 1:☼☼ ☼#○  ☼\n" +
                    "17: 1:☼☼╓╔►   ☼\n" +
                    "17: 1:☼☼║║ ● ○☼\n" +
                    "17: 1:☼#║║○æ˄ ☼\n" +
                    "17: 1:☼☼╚╝●└┘ ☼\n" +
                    "17: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "17: 1:\n" +
                    "17: 1:Scores: 7\n" +
                    "17: 1:Answer: DOWN, ACT\n" +
                    "17: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "17: 2:☼☼ ○    ☼\n" +
                    "17: 2:☼#      ☼\n" +
                    "17: 2:☼☼ ☼#○  ☼\n" +
                    "17: 2:☼☼æ┌>   ☼\n" +
                    "17: 2:☼☼││ ● ○☼\n" +
                    "17: 2:☼#││○╓▲ ☼\n" +
                    "17: 2:☼☼└┘●╚╝ ☼\n" +
                    "17: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "17: 2:\n" +
                    "17: 2:Scores: 12\n" +
                    "17: 2:Answer: LEFT, ACT\n" +
                    "------------------------------------------\n" +
                    "18: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "18: 1:☼☼ ○    ☼\n" +
                    "18: 1:☼#      ☼\n" +
                    "18: 1:☼☼ ☼#○  ☼\n" +
                    "18: 1:☼☼ ╔╗   ☼\n" +
                    "18: 1:☼☼╓║▼● ○☼\n" +
                    "18: 1:☼#║║○<┐ ☼\n" +
                    "18: 1:☼☼╚╝●×┘ ☼\n" +
                    "18: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "18: 1:\n" +
                    "18: 1:Scores: 7\n" +
                    "18: 1:Answer: DOWN\n" +
                    "18: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "18: 2:☼☼ ○    ☼\n" +
                    "18: 2:☼#      ☼\n" +
                    "18: 2:☼☼ ☼#○  ☼\n" +
                    "18: 2:☼☼ ┌┐   ☼\n" +
                    "18: 2:☼☼æ│˅● ○☼\n" +
                    "18: 2:☼#││○◄╗ ☼\n" +
                    "18: 2:☼☼└┘●╘╝ ☼\n" +
                    "18: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "18: 2:\n" +
                    "18: 2:Scores: 12\n" +
                    "18: 2:Answer: LEFT\n" +
                    "18: 2:Fire Event: DIE\n" +
                    "18: 1:Fire Event: EAT[4]\n" +
                    "18: 1:Fire Event: APPLE\n" +
                    "------------------------------------------\n" +
                    "19: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "19: 1:☼☼ ○    ☼\n" +
                    "19: 1:☼#      ☼\n" +
                    "19: 1:☼☼ ☼#○  ☼\n" +
                    "19: 1:☼☼ ╔╗   ☼\n" +
                    "19: 1:☼☼╓║║● ○☼\n" +
                    "19: 1:☼#║║☺─┐ ☼\n" +
                    "19: 1:☼☼╚╝● ¤ ☼\n" +
                    "19: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "19: 1:\n" +
                    "19: 1:Scores: 48\n" +
                    "19: 1:Answer:\n" +
                    "19: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "19: 2:☼☼ ○    ☼\n" +
                    "19: 2:☼#      ☼\n" +
                    "19: 2:☼☼ ☼#○  ☼\n" +
                    "19: 2:☼☼ ┌┐   ☼\n" +
                    "19: 2:☼☼æ││● ○☼\n" +
                    "19: 2:☼#││☻═╗ ☼\n" +
                    "19: 2:☼☼└┘● ╙ ☼\n" +
                    "19: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "19: 2:\n" +
                    "19: 2:Scores: 12\n" +
                    "19: 2:Answer:\n" +
                    "19: 2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                    "19: 1:Fire Event: STONE\n" +
                    "------------------------------------------\n" +
                    "20: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "20: 1:☼☼ ○    ☼\n" +
                    "20: 1:*ø      ☼\n" +
                    "20: 1:☼☼ ☼#○  ☼\n" +
                    "20: 1:☼☼      ☼\n" +
                    "20: 1:☼☼  ╓● ○☼\n" +
                    "20: 1:☼#  ║   ☼\n" +
                    "20: 1:☼☼○ ▼   ☼\n" +
                    "20: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "20: 1:\n" +
                    "20: 1:Scores: 53\n" +
                    "20: 1:Answer: LEFT\n" +
                    "20: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "20: 2:☼☼ ○    ☼\n" +
                    "20: 2:~&      ☼\n" +
                    "20: 2:☼☼ ☼#○  ☼\n" +
                    "20: 2:☼☼      ☼\n" +
                    "20: 2:☼☼  æ● ○☼\n" +
                    "20: 2:☼#  │   ☼\n" +
                    "20: 2:☼☼○ ˅   ☼\n" +
                    "20: 2:☼☼☼☼☼☼☼☼☼\n" +
                    "20: 2:\n" +
                    "20: 2:Scores: 12\n" +
                    "20: 2:Answer: RIGHT\n" +
                    "------------------------------------------",
                String.join("\n", messages));

    }
}
