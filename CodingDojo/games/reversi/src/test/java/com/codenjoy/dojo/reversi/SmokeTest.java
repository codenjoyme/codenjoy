package com.codenjoy.dojo.reversi;

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
import com.codenjoy.dojo.reversi.client.Board;
import com.codenjoy.dojo.reversi.client.ai.AISolver;
import com.codenjoy.dojo.reversi.services.GameRunner;
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

        Dice dice = LocalGameRunner.getDice(
                0, 2, 3, 1, 3, 3,
                0, 2, 2, 3, 1,
                0, 1, 3, 1, 2,
                1, 3, 2, 1, 1,
                1, 3, 0, 0, 1,
                0, 0, 1, 0, 2,
                1, 3, 2, 2, 1,
                0, 1, 1, 3, 3,
                0, 2, 0, 0, 2,
                0, 1, 2, 2, 1,
                1, 3, 1, 3, 3,
                1, 3, 2, 2, 1,
                0, 1, 3, 1, 1);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "    " +
                        " xO " +
                        " Ox " +
                        "    ";
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
        assertEquals("1:Board:\n" +
                        "1:    \n" +
                        "1: xO \n" +
                        "1: Ox \n" +
                        "1:    \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(0,2)\n" +
                        "2:Board:\n" +
                        "2:    \n" +
                        "2: +O \n" +
                        "2: O+ \n" +
                        "2:    \n" +
                        "2:\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:    \n" +
                        "1:... \n" +
                        "1: .X \n" +
                        "1:    \n" +
                        "1:\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:    \n" +
                        "2:ooo \n" +
                        "2: oX \n" +
                        "2:    \n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "2:Scores: 0\n" +
                        "2:Answer: ACT(2,3)\n" +
                        "2:Fire Event: FLIP:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  x \n" +
                        "1:OOx \n" +
                        "1: Ox \n" +
                        "1:    \n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: ACT(3,3)\n" +
                        "2:Board:\n" +
                        "2:  + \n" +
                        "2:OO+ \n" +
                        "2: O+ \n" +
                        "2:    \n" +
                        "2:\n" +
                        "2:Scores: 1\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  X.\n" +
                        "1:... \n" +
                        "1: .X \n" +
                        "1:    \n" +
                        "1:\n" +
                        "1:Scores: 2\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:  Xo\n" +
                        "2:ooo \n" +
                        "2: oX \n" +
                        "2:    \n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "2:Scores: 1\n" +
                        "2:Answer: ACT(0,1)\n" +
                        "2:Fire Event: FLIP:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  xO\n" +
                        "1:OxO \n" +
                        "1:xxx \n" +
                        "1:    \n" +
                        "1:\n" +
                        "DICE:3\n" +
                        "1:Scores: 2\n" +
                        "1:Answer: ACT(2,0)\n" +
                        "2:Board:\n" +
                        "2:  +O\n" +
                        "2:O+O \n" +
                        "2:+++ \n" +
                        "2:    \n" +
                        "2:\n" +
                        "2:Scores: 3\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  X.\n" +
                        "1:.X. \n" +
                        "1:X.. \n" +
                        "1:  . \n" +
                        "1:\n" +
                        "1:Scores: 4\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:  Xo\n" +
                        "2:oXo \n" +
                        "2:Xoo \n" +
                        "2:  o \n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "2:Scores: 3\n" +
                        "2:Answer: ACT(3,1)\n" +
                        "2:Fire Event: FLIP:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  xO\n" +
                        "1:OxO \n" +
                        "1:xxxx\n" +
                        "1:  O \n" +
                        "1:\n" +
                        "DICE:0\n" +
                        "1:Scores: 4\n" +
                        "1:Answer: ACT(0,0)\n" +
                        "2:Board:\n" +
                        "2:  +O\n" +
                        "2:O+O \n" +
                        "2:++++\n" +
                        "2:  O \n" +
                        "2:\n" +
                        "2:Scores: 5\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  X.\n" +
                        "1:.X. \n" +
                        "1:..XX\n" +
                        "1:. . \n" +
                        "1:\n" +
                        "1:Scores: 6\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:  Xo\n" +
                        "2:oXo \n" +
                        "2:ooXX\n" +
                        "2:o o \n" +
                        "2:\n" +
                        "DICE:2\n" +
                        "2:Scores: 5\n" +
                        "2:Answer: ACT(3,2)\n" +
                        "2:Fire Event: FLIP:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  xO\n" +
                        "1:Oxxx\n" +
                        "1:OOxx\n" +
                        "1:O O \n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "1:Scores: 6\n" +
                        "1:Answer: ACT(3,0)\n" +
                        "2:Board:\n" +
                        "2:  +O\n" +
                        "2:O+++\n" +
                        "2:OO++\n" +
                        "2:O O \n" +
                        "2:\n" +
                        "2:Scores: 6\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  X.\n" +
                        "1:.XX.\n" +
                        "1:..X.\n" +
                        "1:. ..\n" +
                        "1:\n" +
                        "1:Scores: 8\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:  Xo\n" +
                        "2:oXXo\n" +
                        "2:ooXo\n" +
                        "2:o oo\n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 1 :0\n" +
                        "2:Scores: 6\n" +
                        "2:Answer: ACT(1,0)\n" +
                        "2:Fire Event: FLIP:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:  xO\n" +
                        "1:OxxO\n" +
                        "1:OxxO\n" +
                        "1:OxOO\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Scores: 8\n" +
                        "1:Answer: ACT(0,3)\n" +
                        "2:Board:\n" +
                        "2:  +O\n" +
                        "2:O++O\n" +
                        "2:O++O\n" +
                        "2:O+OO\n" +
                        "2:\n" +
                        "2:Scores: 7\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:. X.\n" +
                        "1:..X.\n" +
                        "1:.X..\n" +
                        "1:.X..\n" +
                        "1:\n" +
                        "1:Scores: 10\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:o Xo\n" +
                        "2:ooXo\n" +
                        "2:oXoo\n" +
                        "2:oXoo\n" +
                        "2:\n" +
                        "DICE:0\n" +
                        "2:Scores: 7\n" +
                        "2:Answer: ACT(1,3)\n" +
                        "2:Fire Event: FLIP:1\n" +
                        "1:Fire Event: WIN:1\n" +
                        "2:Fire Event: LOOSE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:OxxO\n" +
                        "1:OxxO\n" +
                        "1:OxOO\n" +
                        "1:OxOO\n" +
                        "1:\n" +
                        "1:Scores: 110\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2:O++O\n" +
                        "2:O++O\n" +
                        "2:O+OO\n" +
                        "2:O+OO\n" +
                        "2:\n" +
                        "2:Scores: 8\n" +
                        "2:Answer: \n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:    \n" +
                        "1: xO \n" +
                        "1: Ox \n" +
                        "1:    \n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "1:Scores: 110\n" +
                        "1:Answer: ACT(1,3)\n" +
                        "2:Board:\n" +
                        "2:    \n" +
                        "2: +O \n" +
                        "2: O+ \n" +
                        "2:    \n" +
                        "2:\n" +
                        "2:Scores: 8\n" +
                        "2:Answer: \n" +
                        "1:Fire Event: FLIP:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1: .  \n" +
                        "1: .. \n" +
                        "1: .X \n" +
                        "1:    \n" +
                        "1:\n" +
                        "1:Scores: 111\n" +
                        "1:Answer: \n" +
                        "2:Board:\n" +
                        "2: o  \n" +
                        "2: oo \n" +
                        "2: oX \n" +
                        "2:    \n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "2:Scores: 8\n" +
                        "2:Answer: ACT(0,1)\n" +
                        "2:Fire Event: FLIP:1\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
