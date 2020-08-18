package com.codenjoy.dojo.moebius;

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
import com.codenjoy.dojo.moebius.client.Board;
import com.codenjoy.dojo.moebius.client.ai.AISolver;
import com.codenjoy.dojo.moebius.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.SettingsImpl;
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
        LocalGameRunner.countIterations = 11;
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

        Dice dice = LocalGameRunner.getDice(
                0, 0, 1, 1, 1,
                1, 1, 1, 2, 2,
                1, 2, 1, 3, 3,
                1, 3, 2, 1, 2,
                2, 1, 2, 2, 5,
                2, 2, 2, 3, 6,
                2, 3, 3, 1, 0,
                3, 1, 3, 2, 1,
                3, 2, 3, 3, 3);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected SettingsImpl createSettings() {
                SettingsImpl settings = super.createSettings();
                settings.addEditBox("Size").type(Integer.class).update(5);
                return settings;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        assertEquals("1: 1:╔═══╗\n" +
                    "1: 1:║   ║\n" +
                    "1: 1:║   ║\n" +
                    "1: 1:║   ║\n" +
                    "1: 1:╚═══╝\n" +
                    "1: 1:\n" +
                    "1: 1:Scores: 0\n" +
                    "1: 1:Answer: ACT(0,0)\n" +
                    "------------------------------------------\n" +
                    "2: 1:╔═══╗\n" +
                    "2: 1:║   ║\n" +
                    "2: 1:║   ║\n" +
                    "2: 1:║╚  ║\n" +
                    "2: 1:╔═══╝\n" +
                    "2: 1:\n" +
                    "2: 1:Scores: 0\n" +
                    "2: 1:Answer: ACT(1,1)\n" +
                    "------------------------------------------\n" +
                    "3: 1:╔═══╗\n" +
                    "3: 1:║   ║\n" +
                    "3: 1:║╔  ║\n" +
                    "3: 1:║╔  ║\n" +
                    "3: 1:╔═══╝\n" +
                    "3: 1:\n" +
                    "3: 1:Scores: 0\n" +
                    "3: 1:Answer: ACT(1,2)\n" +
                    "------------------------------------------\n" +
                    "4: 1:╔═══╗\n" +
                    "4: 1:║╗  ║\n" +
                    "4: 1:║╗  ║\n" +
                    "4: 1:║╔  ║\n" +
                    "4: 1:╔═══╝\n" +
                    "4: 1:\n" +
                    "4: 1:Scores: 0\n" +
                    "4: 1:Answer: ACT(1,3)\n" +
                    "------------------------------------------\n" +
                    "5: 1:╔═══╗\n" +
                    "5: 1:║╝  ║\n" +
                    "5: 1:║╗  ║\n" +
                    "5: 1:║╔╔ ║\n" +
                    "5: 1:╔═══╝\n" +
                    "5: 1:\n" +
                    "5: 1:Scores: 0\n" +
                    "5: 1:Answer: ACT(2,1)\n" +
                    "------------------------------------------\n" +
                    "6: 1:╔═══╗\n" +
                    "6: 1:║╝  ║\n" +
                    "6: 1:║╗║ ║\n" +
                    "6: 1:║╔╗ ║\n" +
                    "6: 1:╔═══╝\n" +
                    "6: 1:\n" +
                    "6: 1:Scores: 0\n" +
                    "6: 1:Answer: ACT(2,2)\n" +
                    "------------------------------------------\n" +
                    "7: 1:╔═══╗\n" +
                    "7: 1:║╝╬ ║\n" +
                    "7: 1:║╗═ ║\n" +
                    "7: 1:║╔╗ ║\n" +
                    "7: 1:╔═══╝\n" +
                    "7: 1:\n" +
                    "7: 1:Scores: 0\n" +
                    "7: 1:Answer: ACT(2,3)\n" +
                    "------------------------------------------\n" +
                    "8: 1:╔═══╗\n" +
                    "8: 1:║╝╬ ║\n" +
                    "8: 1:║╗═ ║\n" +
                    "8: 1:║╔╗╝║\n" +
                    "8: 1:╔═══╝\n" +
                    "8: 1:\n" +
                    "8: 1:Scores: 0\n" +
                    "8: 1:Answer: ACT(3,1)\n" +
                    "------------------------------------------\n" +
                    "9: 1:╔═══╗\n" +
                    "9: 1:║╝╬ ║\n" +
                    "9: 1:║╗═╚║\n" +
                    "9: 1:║╔╗╚║\n" +
                    "9: 1:╔═══╝\n" +
                    "9: 1:\n" +
                    "9: 1:Scores: 0\n" +
                    "9: 1:Answer: ACT(3,2)\n" +
                    "------------------------------------------\n" +
                    "10: 1:╔═══╗\n" +
                    "10: 1:║╝╬╗║\n" +
                    "10: 1:║╗═╔║\n" +
                    "10: 1:║╔╗╚║\n" +
                    "10: 1:╔═══╝\n" +
                    "10: 1:\n" +
                    "10: 1:Scores: 0\n" +
                    "10: 1:Answer: \n" +
                    "10: 1:Fire Event: GAME_OVER\n" +
                    "------------------------------------------\n" +
                    "11: 1:╔═══╗\n" +
                    "11: 1:║╝╬╗║\n" +
                    "11: 1:║╗═╗║\n" +
                    "11: 1:║╔╗╚║\n" +
                    "11: 1:╔═══╝\n" +
                    "11: 1:\n" +
                    "11: 1:Scores: 0\n" +
                    "11: 1:Answer: \n" +
                    "11: 1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                    "------------------------------------------",
                String.join("\n", messages));

    }
}
