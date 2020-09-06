package com.codenjoy.dojo.football;

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
import com.codenjoy.dojo.football.client.Board;
import com.codenjoy.dojo.football.client.ai.AISolver;
import com.codenjoy.dojo.football.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
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
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

        Dice dice = LocalGameRunner.getDice(
                1, 2, 3, 0, 3, 2);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼☼☼☼" +
                        "☼☼☼┴┴┴☼☼☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼   ∙   ☼" +
                        "☼       ☼" +
                        "☼       ☼" +
                        "☼☼☼┬┬┬☼☼☼" +
                        "☼☼☼☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        assertEquals("1: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "1: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "1: 1:☼       ☼\n" +
                    "1: 1:☼       ☼\n" +
                    "1: 1:☼   ∙   ☼\n" +
                    "1: 1:☼       ☼\n" +
                    "1: 1:☼☺      ☼\n" +
                    "1: 1:☼☼☼===☼☼☼\n" +
                    "1: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "1: 1:\n" +
                    "1: 1:Scores: 0\n" +
                    "1: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------\n" +
                    "2: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "2: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "2: 1:☼       ☼\n" +
                    "2: 1:☼       ☼\n" +
                    "2: 1:☼   ∙   ☼\n" +
                    "2: 1:☼☺      ☼\n" +
                    "2: 1:☼       ☼\n" +
                    "2: 1:☼☼☼===☼☼☼\n" +
                    "2: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "2: 1:\n" +
                    "2: 1:Scores: 0\n" +
                    "2: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------\n" +
                    "3: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "3: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "3: 1:☼       ☼\n" +
                    "3: 1:☼       ☼\n" +
                    "3: 1:☼☺  ∙   ☼\n" +
                    "3: 1:☼       ☼\n" +
                    "3: 1:☼       ☼\n" +
                    "3: 1:☼☼☼===☼☼☼\n" +
                    "3: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "3: 1:\n" +
                    "3: 1:Scores: 0\n" +
                    "3: 1:Answer: RIGHT, act(2, 3)\n" +
                    "------------------------------------------\n" +
                    "4: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "4: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "4: 1:☼       ☼\n" +
                    "4: 1:☼       ☼\n" +
                    "4: 1:☼ ☺ ∙   ☼\n" +
                    "4: 1:☼       ☼\n" +
                    "4: 1:☼       ☼\n" +
                    "4: 1:☼☼☼===☼☼☼\n" +
                    "4: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "4: 1:\n" +
                    "4: 1:Scores: 0\n" +
                    "4: 1:Answer: RIGHT, act(2, 3)\n" +
                    "------------------------------------------\n" +
                    "5: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "5: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "5: 1:☼       ☼\n" +
                    "5: 1:☼       ☼\n" +
                    "5: 1:☼  ☺∙   ☼\n" +
                    "5: 1:☼       ☼\n" +
                    "5: 1:☼       ☼\n" +
                    "5: 1:☼☼☼===☼☼☼\n" +
                    "5: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "5: 1:\n" +
                    "5: 1:Scores: 0\n" +
                    "5: 1:Answer: RIGHT, act(2, 3)\n" +
                    "------------------------------------------\n" +
                    "6: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "6: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "6: 1:☼       ☼\n" +
                    "6: 1:☼       ☼\n" +
                    "6: 1:☼   ☻   ☼\n" +
                    "6: 1:☼       ☼\n" +
                    "6: 1:☼       ☼\n" +
                    "6: 1:☼☼☼===☼☼☼\n" +
                    "6: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "6: 1:\n" +
                    "6: 1:Scores: 0\n" +
                    "6: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------\n" +
                    "7: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "7: 1:☼☼☼⌂x⌂☼☼☼\n" +
                    "7: 1:☼       ☼\n" +
                    "7: 1:☼   ☺   ☼\n" +
                    "7: 1:☼       ☼\n" +
                    "7: 1:☼       ☼\n" +
                    "7: 1:☼       ☼\n" +
                    "7: 1:☼☼☼===☼☼☼\n" +
                    "7: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "7: 1:\n" +
                    "7: 1:Scores: 0\n" +
                    "7: 1:Answer: \n" +
                    "7: 1:Fire Event: WIN\n" +
                    "7: 1:Fire Event: TOP_GOAL\n" +
                    "------------------------------------------\n" +
                    "8: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "8: 1:☼☼☼⌂x⌂☼☼☼\n" +
                    "8: 1:☼       ☼\n" +
                    "8: 1:☼   ☺   ☼\n" +
                    "8: 1:☼       ☼\n" +
                    "8: 1:☼       ☼\n" +
                    "8: 1:☼       ☼\n" +
                    "8: 1:☼☼☼===☼☼☼\n" +
                    "8: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "8: 1:\n" +
                    "8: 1:Scores: 1\n" +
                    "8: 1:Answer: \n" +
                    "8: 1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                    "------------------------------------------\n" +
                    "9: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "9: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "9: 1:☼       ☼\n" +
                    "9: 1:☼       ☼\n" +
                    "9: 1:☼   ∙   ☼\n" +
                    "9: 1:☼       ☼\n" +
                    "9: 1:☼  ☺    ☼\n" +
                    "9: 1:☼☼☼===☼☼☼\n" +
                    "9: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "9: 1:\n" +
                    "9: 1:Scores: 1\n" +
                    "9: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------\n" +
                    "10: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "10: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "10: 1:☼       ☼\n" +
                    "10: 1:☼       ☼\n" +
                    "10: 1:☼   ∙   ☼\n" +
                    "10: 1:☼  ☺    ☼\n" +
                    "10: 1:☼       ☼\n" +
                    "10: 1:☼☼☼===☼☼☼\n" +
                    "10: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "10: 1:\n" +
                    "10: 1:Scores: 1\n" +
                    "10: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------\n" +
                    "11: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "11: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "11: 1:☼       ☼\n" +
                    "11: 1:☼       ☼\n" +
                    "11: 1:☼  ☺∙   ☼\n" +
                    "11: 1:☼       ☼\n" +
                    "11: 1:☼       ☼\n" +
                    "11: 1:☼☼☼===☼☼☼\n" +
                    "11: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "11: 1:\n" +
                    "11: 1:Scores: 1\n" +
                    "11: 1:Answer: RIGHT, act(2, 3)\n" +
                    "------------------------------------------\n" +
                    "12: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "12: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "12: 1:☼       ☼\n" +
                    "12: 1:☼       ☼\n" +
                    "12: 1:☼   ☻   ☼\n" +
                    "12: 1:☼       ☼\n" +
                    "12: 1:☼       ☼\n" +
                    "12: 1:☼☼☼===☼☼☼\n" +
                    "12: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "12: 1:\n" +
                    "12: 1:Scores: 1\n" +
                    "12: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------\n" +
                    "13: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "13: 1:☼☼☼⌂x⌂☼☼☼\n" +
                    "13: 1:☼       ☼\n" +
                    "13: 1:☼   ☺   ☼\n" +
                    "13: 1:☼       ☼\n" +
                    "13: 1:☼       ☼\n" +
                    "13: 1:☼       ☼\n" +
                    "13: 1:☼☼☼===☼☼☼\n" +
                    "13: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "13: 1:\n" +
                    "13: 1:Scores: 1\n" +
                    "13: 1:Answer: \n" +
                    "13: 1:Fire Event: WIN\n" +
                    "13: 1:Fire Event: TOP_GOAL\n" +
                    "------------------------------------------\n" +
                    "14: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "14: 1:☼☼☼⌂x⌂☼☼☼\n" +
                    "14: 1:☼       ☼\n" +
                    "14: 1:☼   ☺   ☼\n" +
                    "14: 1:☼       ☼\n" +
                    "14: 1:☼       ☼\n" +
                    "14: 1:☼       ☼\n" +
                    "14: 1:☼☼☼===☼☼☼\n" +
                    "14: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "14: 1:\n" +
                    "14: 1:Scores: 2\n" +
                    "14: 1:Answer: \n" +
                    "14: 1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                    "------------------------------------------\n" +
                    "15: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "15: 1:☼☼☼⌂⌂⌂☼☼☼\n" +
                    "15: 1:☼       ☼\n" +
                    "15: 1:☼       ☼\n" +
                    "15: 1:☼   ∙   ☼\n" +
                    "15: 1:☼       ☼\n" +
                    "15: 1:☼☺      ☼\n" +
                    "15: 1:☼☼☼===☼☼☼\n" +
                    "15: 1:☼☼☼☼☼☼☼☼☼\n" +
                    "15: 1:\n" +
                    "15: 1:Scores: 2\n" +
                    "15: 1:Answer: UP, act(1, 3)\n" +
                    "------------------------------------------",
                String.join("\n", messages));

    }
}
