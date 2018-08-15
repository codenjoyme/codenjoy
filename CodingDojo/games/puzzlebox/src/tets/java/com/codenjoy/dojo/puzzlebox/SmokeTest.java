package com.codenjoy.dojo.puzzlebox;

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
import com.codenjoy.dojo.puzzlebox.client.Board;
import com.codenjoy.dojo.puzzlebox.client.ai.AISolver;
import com.codenjoy.dojo.puzzlebox.services.GameRunner;
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
                0, 1, 2, 3, 4, // random numbers
                0, 1, 2, 3, 4,
                0, 1, 2, 3, 4,
                0, 1, 2, 3, 4,
                0, 1, 2, 3, 4);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼☼☼☼☼☼" +
                        "☼    ☼    ☼" +
                        "☼☼☼       ☼" +
                        "☼   0    ☼☼" +
                        "☼        0☼" +
                        "☼         ☼" +
                        "☼         ☼" +
                        "☼  #      ☼" +
                        "☼         ☼" +
                        "☼#   0  # ☼" +
                        "☼☼☼☼☼☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        assertEquals("1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼  #      ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#   0  # ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼ #       ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼1   0  # ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#        ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼ #  0  1 ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Answer: UP, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#        ☼\n" +
                        "1:☼       # ☼\n" +
                        "1:☼  # 0    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼       # ☼\n" +
                        "1:☼#        ☼\n" +
                        "1:☼   #0    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "Fire Event: FILL\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼       # ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#   @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼       # ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#   @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼       #0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼ #  @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Answer: UP, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0   #☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼  # @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼     # ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼   #@    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼  # ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼   #@    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼  # ☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼  # @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼   #☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼ #  @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Answer: UP, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼   #☼\n" +
                        "1:☼☼☼       ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#   @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼      #☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#   @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼      #☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼#   @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼     # ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼1   @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼    #  ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼ #  @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Answer: UP, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼   #   ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼  # @    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼    ☼    ☼\n" +
                        "1:☼☼☼  #    ☼\n" +
                        "1:☼   0    ☼☼\n" +
                        "1:☼        0☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼         ☼\n" +
                        "1:☼   #@    ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Answer: LEFT, ACT\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
