package com.codenjoy.dojo.hex;

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
import com.codenjoy.dojo.hex.client.Board;
import com.codenjoy.dojo.hex.client.ai.AISolver;
import com.codenjoy.dojo.hex.services.GameRunner;
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
                2, 2, // hero pos
                0, // where to go, direction
                0, // index of hero to move
                1, // jump 0 or clone 1
                0, 1, 1,
                1, 1, 1,
                2, 1, 1,
                3, 1, 1,
                0, 2, 1,
                1, 2, 1,
                2, 2, 0,
                3, 2, 1,
                0, 3, 0,
                1, 3, 0,
                2, 3, 0,
                3, 3, 1,
                0, 4, 1,
                1, 4, 1,
                2, 4, 0,
                3, 4, 1,
                0, 5, 0,
                1, 5, 0,
                2, 5, 1,
                3, 5, 0,
                0, 6, 0,
                1, 6, 0,
                2, 6, 1,
                3, 6, 0,
                0, 7, 1,
                1, 7, 1,
                2, 7, 1,
                3, 7, 0,
                0, 8, 1,
                1, 8, 0,
                2, 8, 1,
                3, 8, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼    ☼" +
                        "☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        assertEquals("DICE:2\n" +
                        "DICE:2\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,3),LEFT\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 30\n" +
                        "1:Answer: ACT(2,3),RIGHT\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☺☺☺ ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 60\n" +
                        "1:Answer: ACT(2,3),UP\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺ ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 90\n" +
                        "1:Answer: ACT(2,2),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺ ☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "1:Scores: 90\n" +
                        "1:Answer: ACT(3,3),DOWN\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺ ☼\n" +
                        "1:☼  ☺ ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "1:Scores: 120\n" +
                        "1:Answer: ACT(3,4),LEFT\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺ ☼\n" +
                        "1:☼ ☺☺ ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "1:Scores: 150\n" +
                        "1:Answer: ACT(3,3),RIGHT\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼ ☺☺ ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "1:Scores: 180\n" +
                        "1:Answer: ACT(3,3),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼ ☺☺ ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:5\n" +
                        "DICE:0\n" +
                        "1:Scores: 180\n" +
                        "1:Answer: ACT(3,4,1),LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺  ☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:5\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:5\n" +
                        "DICE:1\n" +
                        "1:Scores: 180\n" +
                        "1:Answer: ACT(3,3),UP\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺☺ ☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:5\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:6\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:6\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:6\n" +
                        "DICE:1\n" +
                        "1:Scores: 210\n" +
                        "1:Answer: ACT(3,3),UP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺☺ ☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:6\n" +
                        "DICE:0\n" +
                        "1:Scores: 210\n" +
                        "1:Answer: ACT(3,3,1),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺☺ ☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:7\n" +
                        "DICE:1\n" +
                        "1:Scores: 210\n" +
                        "1:Answer: ACT(4,3),UP\n" +
                        "1:Fire Event: WIN(1)\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺☺☺☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:7\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:8\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:8\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:8\n" +
                        "DICE:1\n" +
                        "1:Scores: 240\n" +
                        "1:Answer: ACT(4,3),UP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:☼    ☼\n" +
                        "1:☼ ☺☺☺☼\n" +
                        "1:☼☺☺☺☺☼\n" +
                        "1:☼☺☺  ☼\n" +
                        "1:☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:8\n" +
                        "DICE:0\n" +
                        "1:Scores: 240\n" +
                        "1:Answer: ACT(4,3,1),DOWN\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
