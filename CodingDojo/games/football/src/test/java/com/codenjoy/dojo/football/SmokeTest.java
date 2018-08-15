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


import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.football.client.Board;
import com.codenjoy.dojo.football.client.ai.AISolver;
import com.codenjoy.dojo.football.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SmokeTest {
    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

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
        assertEquals("DICE:1\n" +
                        "DICE:2\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☺      ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼☺      ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☺  ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺ ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂x⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: \n" +
                        "Fire Event: WIN\n" +
                        "Fire Event: TOP_GOAL\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺    ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼  ☺    ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼  ☺∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☻   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂x⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ☺   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: \n" +
                        "Fire Event: WIN\n" +
                        "Fire Event: TOP_GOAL\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼   ∙   ☼\n" +
                        "1:☼ ☺     ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: UP, act(1, 3)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼☼⌂⌂⌂☼☼☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼ ☺ ∙   ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼       ☼\n" +
                        "1:☼☼☼===☼☼☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "1:Answer: RIGHT, act(2, 3)\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
