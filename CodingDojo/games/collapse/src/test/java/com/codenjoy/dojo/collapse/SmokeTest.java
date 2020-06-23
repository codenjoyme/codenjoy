package com.codenjoy.dojo.collapse;

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


import com.codenjoy.dojo.collapse.client.Board;
import com.codenjoy.dojo.collapse.client.ai.AISolver;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.collapse.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.multiplayer.GameField;
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
                0, 1, 3, 3, 3, 1, 2, 3, 0, 1, 2, 3, 0,
                1, 2, 2, 1, 0, 2, 0, 3, 3, 1, 3, 3, 0,
                3, 2, 2, 1, 0, 3, 0, 3, 2, 1, 2, 3, 0,
                2, 1, 1, 2, 1, 1, 2, 0, 0, 1, 2, 2, 1,
                1, 1, 2, 2, 3, 2, 2, 0, 0, 3, 1, 2, 2,
                0, 2, 1, 3, 0, 3, 2, 3, 1, 1, 2, 3, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public GameField createGame(int levelNumber) {
                settings.getParameter("Field size").type(Integer.class).update(5);
                return super.createGame(levelNumber);
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼682☼\n" +
                        "1:☼371☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(1,2),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼382☼\n" +
                        "1:☼671☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(0,1),UP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼382☼\n" +
                        "1:☼671☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,1),LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼382☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,0),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼382☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(3,1),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼382☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(3,0),DOWN\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼382☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,2),RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼328☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(0,3),LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼328☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(3,2),RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼135☼\n" +
                        "1:☼328☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(2,3),LEFT\n" +
                        "1:Fire Event: SUCCESS\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼ 15☼\n" +
                        "1:☼ 28☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: ACT(2,1),RIGHT\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼215☼\n" +
                        "1:☼328☼\n" +
                        "1:☼716☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: ACT(1,2),LEFT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼215☼\n" +
                        "1:☼328☼\n" +
                        "1:☼716☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: ACT(0,1),UP\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼215☼\n" +
                        "1:☼328☼\n" +
                        "1:☼716☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: ACT(2,1),RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:☼215☼\n" +
                        "1:☼328☼\n" +
                        "1:☼761☼\n" +
                        "1:☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: ACT(1,2),UP\n" +
                        "1:Fire Event: SUCCESS\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
