package com.codenjoy.dojo.a2048;

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


import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.a2048.client.ai.AISolver;
import com.codenjoy.dojo.a2048.services.GameRunner;
import com.codenjoy.dojo.client.local.LocalGameRunner;
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

        Dice dice = LocalGameRunner.getDice(0);

        // when
        GameRunner gameType = new GameRunner(){
            @Override
            public Dice getDice() {
                return dice;
            }
        };
        LocalGameRunner.run(gameType,
                new AISolver(null),
                new Board());

        // then
        assertEquals("1:Board:\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(8)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:2   2\n" +
                        "1:\n" +
                        "1:Scores: 8\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(12)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:2   4\n" +
                        "1:\n" +
                        "1:Scores: 12\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: SUM(16)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:4   8\n" +
                        "1:\n" +
                        "1:Scores: 16\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(20)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:2  48\n" +
                        "1:\n" +
                        "1:Scores: 20\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: SUM(24)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:4  48\n" +
                        "1:\n" +
                        "1:Scores: 24\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(28)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:2  88\n" +
                        "1:\n" +
                        "1:Scores: 28\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: SUM(32)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    8\n" +
                        "1:4  88\n" +
                        "1:\n" +
                        "1:Scores: 32\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(36)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    8\n" +
                        "1:2  4A\n" +
                        "1:\n" +
                        "1:Scores: 36\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: SUM(40)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:    8\n" +
                        "1:4  4A\n" +
                        "1:\n" +
                        "1:Scores: 40\n" +
                        "1:Answer: LEFT\n" +
                        "1:Fire Event: SUM(44)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:4   2\n" +
                        "1:     \n" +
                        "1:4    \n" +
                        "1:8    \n" +
                        "1:8A  2\n" +
                        "1:\n" +
                        "1:Scores: 44\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: SUM(48)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:8    \n" +
                        "1:AA  4\n" +
                        "1:\n" +
                        "1:Scores: 48\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(52)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:     \n" +
                        "1:    8\n" +
                        "1:2  B4\n" +
                        "1:\n" +
                        "1:Scores: 52\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: SUM(56)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   2\n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:    8\n" +
                        "1:4  B4\n" +
                        "1:\n" +
                        "1:Scores: 56\n" +
                        "1:Answer: RIGHT\n" +
                        "1:Fire Event: SUM(60)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:2   4\n" +
                        "1:     \n" +
                        "1:    4\n" +
                        "1:    8\n" +
                        "1:2 4B4\n" +
                        "1:\n" +
                        "1:Scores: 60\n" +
                        "1:Answer: UP\n" +
                        "1:Fire Event: SUM(64)\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
