package com.codenjoy.dojo.pong;

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
import com.codenjoy.dojo.pong.client.Board;
import com.codenjoy.dojo.pong.client.YourSolver;
import com.codenjoy.dojo.pong.client.ai.AISolver;
import com.codenjoy.dojo.pong.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SmokeTest {

    private Dice dice;

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
                0, 1, // ball horizontal and vertical init speed vector
                0, 0, // solvers rateCoefficients
                0, 0, // -- " --
                0, 0,
                0, 0,
                0, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "           " +
                        "-----------" +
                        "           " +
                        "           " +
                        "           " +
                        "     o     " +
                        "           " +
                        "           " +
                        "           " +
                        "-----------" +
                        "           ";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ArrayList<Solver>() {{
                    add(new AISolver(dice));
                    add(new YourSolver(dice));
                }},
                new ArrayList<ClientBoard>() {{
                    add(new Board());
                    add(new Board());
                }});

        // then
        assertEquals("1: 1:           \n" +
                    "1: 1:-----------\n" +
                    "1: 1:           \n" +
                    "1: 1:           \n" +
                    "1: 1: #       H \n" +
                    "1: 1: #   o   H \n" +
                    "1: 1: #       H \n" +
                    "1: 1:           \n" +
                    "1: 1:           \n" +
                    "1: 1:-----------\n" +
                    "1: 1:           \n" +
                    "1: 1:\n" +
                    "1: 1:Scores: 0\n" +
                    "1: 1:Answer: DOWN\n" +
                    "1: 2:           \n" +
                    "1: 2:-----------\n" +
                    "1: 2:           \n" +
                    "1: 2:           \n" +
                    "1: 2: H       # \n" +
                    "1: 2: H   o   # \n" +
                    "1: 2: H       # \n" +
                    "1: 2:           \n" +
                    "1: 2:           \n" +
                    "1: 2:-----------\n" +
                    "1: 2:           \n" +
                    "1: 2:\n" +
                    "1: 2:Scores: 0\n" +
                    "1: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "2: 1:           \n" +
                    "2: 1:-----------\n" +
                    "2: 1:           \n" +
                    "2: 1:           \n" +
                    "2: 1: #         \n" +
                    "2: 1: #       H \n" +
                    "2: 1: #  o    H \n" +
                    "2: 1:         H \n" +
                    "2: 1:           \n" +
                    "2: 1:-----------\n" +
                    "2: 1:           \n" +
                    "2: 1:\n" +
                    "2: 1:Scores: 0\n" +
                    "2: 1:Answer: DOWN\n" +
                    "2: 2:           \n" +
                    "2: 2:-----------\n" +
                    "2: 2:           \n" +
                    "2: 2:           \n" +
                    "2: 2: H         \n" +
                    "2: 2: H       # \n" +
                    "2: 2: H  o    # \n" +
                    "2: 2:         # \n" +
                    "2: 2:           \n" +
                    "2: 2:-----------\n" +
                    "2: 2:           \n" +
                    "2: 2:\n" +
                    "2: 2:Scores: 0\n" +
                    "2: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "3: 1:           \n" +
                    "3: 1:-----------\n" +
                    "3: 1:           \n" +
                    "3: 1:           \n" +
                    "3: 1: #         \n" +
                    "3: 1: #         \n" +
                    "3: 1: #       H \n" +
                    "3: 1:   o     H \n" +
                    "3: 1:         H \n" +
                    "3: 1:-----------\n" +
                    "3: 1:           \n" +
                    "3: 1:\n" +
                    "3: 1:Scores: 0\n" +
                    "3: 1:Answer: DOWN\n" +
                    "3: 2:           \n" +
                    "3: 2:-----------\n" +
                    "3: 2:           \n" +
                    "3: 2:           \n" +
                    "3: 2: H         \n" +
                    "3: 2: H         \n" +
                    "3: 2: H       # \n" +
                    "3: 2:   o     # \n" +
                    "3: 2:         # \n" +
                    "3: 2:-----------\n" +
                    "3: 2:           \n" +
                    "3: 2:\n" +
                    "3: 2:Scores: 0\n" +
                    "3: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "4: 1:           \n" +
                    "4: 1:-----------\n" +
                    "4: 1:           \n" +
                    "4: 1:           \n" +
                    "4: 1: #         \n" +
                    "4: 1: #         \n" +
                    "4: 1: #       H \n" +
                    "4: 1:         H \n" +
                    "4: 1:  o      H \n" +
                    "4: 1:-----------\n" +
                    "4: 1:           \n" +
                    "4: 1:\n" +
                    "4: 1:Scores: 0\n" +
                    "4: 1:Answer: DOWN\n" +
                    "4: 2:           \n" +
                    "4: 2:-----------\n" +
                    "4: 2:           \n" +
                    "4: 2:           \n" +
                    "4: 2: H         \n" +
                    "4: 2: H         \n" +
                    "4: 2: H       # \n" +
                    "4: 2:         # \n" +
                    "4: 2:  o      # \n" +
                    "4: 2:-----------\n" +
                    "4: 2:           \n" +
                    "4: 2:\n" +
                    "4: 2:Scores: 0\n" +
                    "4: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "5: 1:           \n" +
                    "5: 1:-----------\n" +
                    "5: 1:           \n" +
                    "5: 1:           \n" +
                    "5: 1: #         \n" +
                    "5: 1: #         \n" +
                    "5: 1: #       H \n" +
                    "5: 1: o       H \n" +
                    "5: 1:         H \n" +
                    "5: 1:-----------\n" +
                    "5: 1:           \n" +
                    "5: 1:\n" +
                    "5: 1:Scores: 0\n" +
                    "5: 1:Answer: UP\n" +
                    "5: 2:           \n" +
                    "5: 2:-----------\n" +
                    "5: 2:           \n" +
                    "5: 2:           \n" +
                    "5: 2: H         \n" +
                    "5: 2: H         \n" +
                    "5: 2: H       # \n" +
                    "5: 2: o       # \n" +
                    "5: 2:         # \n" +
                    "5: 2:-----------\n" +
                    "5: 2:           \n" +
                    "5: 2:\n" +
                    "5: 2:Scores: 0\n" +
                    "5: 2:Answer: STOP\n" +
                    "5: 2:Fire Event: LOOSE\n" +
                    "5: 1:Fire Event: WIN\n" +
                    "------------------------------------------\n" +
                    "6: 1:           \n" +
                    "6: 1:-----------\n" +
                    "6: 1:           \n" +
                    "6: 1:           \n" +
                    "6: 1: #         \n" +
                    "6: 1: #   o   H \n" +
                    "6: 1: #       H \n" +
                    "6: 1:         H \n" +
                    "6: 1:           \n" +
                    "6: 1:-----------\n" +
                    "6: 1:           \n" +
                    "6: 1:\n" +
                    "6: 1:Scores: 1\n" +
                    "6: 1:Answer: UP\n" +
                    "6: 2:           \n" +
                    "6: 2:-----------\n" +
                    "6: 2:           \n" +
                    "6: 2:           \n" +
                    "6: 2: H         \n" +
                    "6: 2: H   o   # \n" +
                    "6: 2: H       # \n" +
                    "6: 2:         # \n" +
                    "6: 2:           \n" +
                    "6: 2:-----------\n" +
                    "6: 2:           \n" +
                    "6: 2:\n" +
                    "6: 2:Scores: 0\n" +
                    "6: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "7: 1:           \n" +
                    "7: 1:-----------\n" +
                    "7: 1:           \n" +
                    "7: 1:           \n" +
                    "7: 1: #       H \n" +
                    "7: 1: #       H \n" +
                    "7: 1: #  o    H \n" +
                    "7: 1:           \n" +
                    "7: 1:           \n" +
                    "7: 1:-----------\n" +
                    "7: 1:           \n" +
                    "7: 1:\n" +
                    "7: 1:Scores: 1\n" +
                    "7: 1:Answer: DOWN\n" +
                    "7: 2:           \n" +
                    "7: 2:-----------\n" +
                    "7: 2:           \n" +
                    "7: 2:           \n" +
                    "7: 2: H       # \n" +
                    "7: 2: H       # \n" +
                    "7: 2: H  o    # \n" +
                    "7: 2:           \n" +
                    "7: 2:           \n" +
                    "7: 2:-----------\n" +
                    "7: 2:           \n" +
                    "7: 2:\n" +
                    "7: 2:Scores: 0\n" +
                    "7: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "8: 1:           \n" +
                    "8: 1:-----------\n" +
                    "8: 1:           \n" +
                    "8: 1:           \n" +
                    "8: 1: #         \n" +
                    "8: 1: #       H \n" +
                    "8: 1: #       H \n" +
                    "8: 1:   o     H \n" +
                    "8: 1:           \n" +
                    "8: 1:-----------\n" +
                    "8: 1:           \n" +
                    "8: 1:\n" +
                    "8: 1:Scores: 1\n" +
                    "8: 1:Answer: DOWN\n" +
                    "8: 2:           \n" +
                    "8: 2:-----------\n" +
                    "8: 2:           \n" +
                    "8: 2:           \n" +
                    "8: 2: H         \n" +
                    "8: 2: H       # \n" +
                    "8: 2: H       # \n" +
                    "8: 2:   o     # \n" +
                    "8: 2:           \n" +
                    "8: 2:-----------\n" +
                    "8: 2:           \n" +
                    "8: 2:\n" +
                    "8: 2:Scores: 0\n" +
                    "8: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "9: 1:           \n" +
                    "9: 1:-----------\n" +
                    "9: 1:           \n" +
                    "9: 1:           \n" +
                    "9: 1: #         \n" +
                    "9: 1: #         \n" +
                    "9: 1: #       H \n" +
                    "9: 1:         H \n" +
                    "9: 1:  o      H \n" +
                    "9: 1:-----------\n" +
                    "9: 1:           \n" +
                    "9: 1:\n" +
                    "9: 1:Scores: 1\n" +
                    "9: 1:Answer: DOWN\n" +
                    "9: 2:           \n" +
                    "9: 2:-----------\n" +
                    "9: 2:           \n" +
                    "9: 2:           \n" +
                    "9: 2: H         \n" +
                    "9: 2: H         \n" +
                    "9: 2: H       # \n" +
                    "9: 2:         # \n" +
                    "9: 2:  o      # \n" +
                    "9: 2:-----------\n" +
                    "9: 2:           \n" +
                    "9: 2:\n" +
                    "9: 2:Scores: 0\n" +
                    "9: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "10: 1:           \n" +
                    "10: 1:-----------\n" +
                    "10: 1:           \n" +
                    "10: 1:           \n" +
                    "10: 1: #         \n" +
                    "10: 1: #         \n" +
                    "10: 1: #       H \n" +
                    "10: 1: o       H \n" +
                    "10: 1:         H \n" +
                    "10: 1:-----------\n" +
                    "10: 1:           \n" +
                    "10: 1:\n" +
                    "10: 1:Scores: 1\n" +
                    "10: 1:Answer: UP\n" +
                    "10: 2:           \n" +
                    "10: 2:-----------\n" +
                    "10: 2:           \n" +
                    "10: 2:           \n" +
                    "10: 2: H         \n" +
                    "10: 2: H         \n" +
                    "10: 2: H       # \n" +
                    "10: 2: o       # \n" +
                    "10: 2:         # \n" +
                    "10: 2:-----------\n" +
                    "10: 2:           \n" +
                    "10: 2:\n" +
                    "10: 2:Scores: 0\n" +
                    "10: 2:Answer: STOP\n" +
                    "10: 2:Fire Event: LOOSE\n" +
                    "10: 1:Fire Event: WIN\n" +
                    "------------------------------------------\n" +
                    "11: 1:           \n" +
                    "11: 1:-----------\n" +
                    "11: 1:           \n" +
                    "11: 1:           \n" +
                    "11: 1: #         \n" +
                    "11: 1: #   o   H \n" +
                    "11: 1: #       H \n" +
                    "11: 1:         H \n" +
                    "11: 1:           \n" +
                    "11: 1:-----------\n" +
                    "11: 1:           \n" +
                    "11: 1:\n" +
                    "11: 1:Scores: 2\n" +
                    "11: 1:Answer: UP\n" +
                    "11: 2:           \n" +
                    "11: 2:-----------\n" +
                    "11: 2:           \n" +
                    "11: 2:           \n" +
                    "11: 2: H         \n" +
                    "11: 2: H   o   # \n" +
                    "11: 2: H       # \n" +
                    "11: 2:         # \n" +
                    "11: 2:           \n" +
                    "11: 2:-----------\n" +
                    "11: 2:           \n" +
                    "11: 2:\n" +
                    "11: 2:Scores: 0\n" +
                    "11: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "12: 1:           \n" +
                    "12: 1:-----------\n" +
                    "12: 1:           \n" +
                    "12: 1:           \n" +
                    "12: 1: #       H \n" +
                    "12: 1: #       H \n" +
                    "12: 1: #  o    H \n" +
                    "12: 1:           \n" +
                    "12: 1:           \n" +
                    "12: 1:-----------\n" +
                    "12: 1:           \n" +
                    "12: 1:\n" +
                    "12: 1:Scores: 2\n" +
                    "12: 1:Answer: DOWN\n" +
                    "12: 2:           \n" +
                    "12: 2:-----------\n" +
                    "12: 2:           \n" +
                    "12: 2:           \n" +
                    "12: 2: H       # \n" +
                    "12: 2: H       # \n" +
                    "12: 2: H  o    # \n" +
                    "12: 2:           \n" +
                    "12: 2:           \n" +
                    "12: 2:-----------\n" +
                    "12: 2:           \n" +
                    "12: 2:\n" +
                    "12: 2:Scores: 0\n" +
                    "12: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "13: 1:           \n" +
                    "13: 1:-----------\n" +
                    "13: 1:           \n" +
                    "13: 1:           \n" +
                    "13: 1: #         \n" +
                    "13: 1: #       H \n" +
                    "13: 1: #       H \n" +
                    "13: 1:   o     H \n" +
                    "13: 1:           \n" +
                    "13: 1:-----------\n" +
                    "13: 1:           \n" +
                    "13: 1:\n" +
                    "13: 1:Scores: 2\n" +
                    "13: 1:Answer: DOWN\n" +
                    "13: 2:           \n" +
                    "13: 2:-----------\n" +
                    "13: 2:           \n" +
                    "13: 2:           \n" +
                    "13: 2: H         \n" +
                    "13: 2: H       # \n" +
                    "13: 2: H       # \n" +
                    "13: 2:   o     # \n" +
                    "13: 2:           \n" +
                    "13: 2:-----------\n" +
                    "13: 2:           \n" +
                    "13: 2:\n" +
                    "13: 2:Scores: 0\n" +
                    "13: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "14: 1:           \n" +
                    "14: 1:-----------\n" +
                    "14: 1:           \n" +
                    "14: 1:           \n" +
                    "14: 1: #         \n" +
                    "14: 1: #         \n" +
                    "14: 1: #       H \n" +
                    "14: 1:         H \n" +
                    "14: 1:  o      H \n" +
                    "14: 1:-----------\n" +
                    "14: 1:           \n" +
                    "14: 1:\n" +
                    "14: 1:Scores: 2\n" +
                    "14: 1:Answer: DOWN\n" +
                    "14: 2:           \n" +
                    "14: 2:-----------\n" +
                    "14: 2:           \n" +
                    "14: 2:           \n" +
                    "14: 2: H         \n" +
                    "14: 2: H         \n" +
                    "14: 2: H       # \n" +
                    "14: 2:         # \n" +
                    "14: 2:  o      # \n" +
                    "14: 2:-----------\n" +
                    "14: 2:           \n" +
                    "14: 2:\n" +
                    "14: 2:Scores: 0\n" +
                    "14: 2:Answer: STOP\n" +
                    "------------------------------------------\n" +
                    "15: 1:           \n" +
                    "15: 1:-----------\n" +
                    "15: 1:           \n" +
                    "15: 1:           \n" +
                    "15: 1: #         \n" +
                    "15: 1: #         \n" +
                    "15: 1: #       H \n" +
                    "15: 1: o       H \n" +
                    "15: 1:         H \n" +
                    "15: 1:-----------\n" +
                    "15: 1:           \n" +
                    "15: 1:\n" +
                    "15: 1:Scores: 2\n" +
                    "15: 1:Answer: UP\n" +
                    "15: 2:           \n" +
                    "15: 2:-----------\n" +
                    "15: 2:           \n" +
                    "15: 2:           \n" +
                    "15: 2: H         \n" +
                    "15: 2: H         \n" +
                    "15: 2: H       # \n" +
                    "15: 2: o       # \n" +
                    "15: 2:         # \n" +
                    "15: 2:-----------\n" +
                    "15: 2:           \n" +
                    "15: 2:\n" +
                    "15: 2:Scores: 0\n" +
                    "15: 2:Answer: STOP\n" +
                    "15: 2:Fire Event: LOOSE\n" +
                    "15: 1:Fire Event: WIN\n" +
                    "------------------------------------------",
                String.join("\n", messages));

    }
}
