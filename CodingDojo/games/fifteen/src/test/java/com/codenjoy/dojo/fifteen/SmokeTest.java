package com.codenjoy.dojo.fifteen;

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


import com.codenjoy.dojo.fifteen.client.Board;
import com.codenjoy.dojo.fifteen.client.ai.AISolver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.fifteen.services.GameRunner;
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
                1, 2, 3, 0, 3, 2, 2, 0,
                0, 3, 2, 1, 3, 1, 3, 3,
                0, 3, 2, 1, 3, 2, 3, 0,
                1, 1, 2, 1, 3, 2, 1, 2,
                0, 2, 1, 2, 1, 3, 3, 1,
                1, 3, 1, 1, 3, 2, 3, 0,
                1, 0, 2, 1, 3, 2, 1, 2,
                0, 2, 1, 2, 1, 3, 3, 1,
                3, 3, 2, 1, 2, 3, 2, 1,
                2, 3, 2, 1, 2, 3, 1, 2,
                3, 1, 2, 3, 1, 1, 2, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // thend
        assertEquals("DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
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
                        "DICE_CORRECTED < 2 :1\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+jli*\n" +
                        "1:*mkon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*mjli*\n" +
                        "1:*+kon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*mjli*\n" +
                        "1:*k+on*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: UP\n" +
                        "1:Fire Event: moveCount: 4, number: 0\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*m+li*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+mli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*m+li*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+mli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*kmli*\n" +
                        "1:*+jon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: UP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*hfeg*\n" +
                        "1:*+mli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: UP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*+feg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*f+eg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*+feg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*f+eg*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*fe+g*\n" +
                        "1:*hmli*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:******\n" +
                        "1:*felg*\n" +
                        "1:*hm+i*\n" +
                        "1:*kjon*\n" +
                        "1:*adcb*\n" +
                        "1:******\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
