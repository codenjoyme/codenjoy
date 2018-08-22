package com.codenjoy.dojo.tetris;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.ai.AISolver;
import com.codenjoy.dojo.tetris.services.GameRunner;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;
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
                0, 2, 2, 3, 1,
                0, 1, 4, 1, 2,
                1, 3, 2, 4, 1,
                1, 3, 0, 0, 1,
                0, 0, 1, 0, 2,
                1, 3, 2, 4, 1,
                0, 1, 1, 3, 3,
                0, 2, 0, 0, 2,
                0, 1, 2, 2, 1,
                1, 3, 4, 3, 4,
                1, 3, 2, 4, 1,
                0, 1, 3, 1, 1);

        GameRunner gameType = new GameRunner() {
            {
                settings.addEditBox("Glass Size").type(Integer.class).update(10);
            }

            @Override
            public Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        String join = String.join("\n", messages);
        List<String> result = Arrays.stream(join.split("\n"))
                .map(s -> {
                    if (s.indexOf("board':'") == -1) {
                        return s;
                    }
                    int beginIndex = "1:  'board':'".length();
                    int toIndex = s.length() - "',".length();
                    String board = s.substring(beginIndex, toIndex);
                    return s.substring(0, beginIndex)
                            + '\n'
                            + TestUtils.injectN(board)
                            + s.substring(toIndex);
                })
                .collect(toList());
        join = String.join("\n", result);

        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    I     \n" +
                        "    I     \n" +
                        "    I     \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'J'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:2\n" +
                        "1:Answer: RIGHT\n" +
                        "Fire Event: Event[figuresDropped:1:0]\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    J     \n" +
                        "   JJ     \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "     I    \n" +
                        "     I    \n" +
                        "     I    \n" +
                        "     I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'J',\n" +
                        "1:    'L'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   J      \n" +
                        "   J      \n" +
                        "  JJ      \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "     I    \n" +
                        "     I    \n" +
                        "     I    \n" +
                        "     I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[3,8]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'J',\n" +
                        "1:    'L'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: Event[figuresDropped:1:1]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    L     \n" +
                        "    LL    \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "     I    \n" +
                        "   J I    \n" +
                        "   J I    \n" +
                        "  JJ I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: Event[figuresDropped:1:2]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   SS     \n" +
                        "          \n" +
                        "          \n" +
                        "    L     \n" +
                        "    L     \n" +
                        "    LL    \n" +
                        "     I    \n" +
                        "   J I    \n" +
                        "   J I    \n" +
                        "  JJ I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'I'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:2\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "     SS   \n" +
                        "    SS    \n" +
                        "          \n" +
                        "    L     \n" +
                        "    L     \n" +
                        "    LL    \n" +
                        "     I    \n" +
                        "   J I    \n" +
                        "   J I    \n" +
                        "  JJ I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[5,8]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'I'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "          \n" +
                        "    SS    \n" +
                        "   SS     \n" +
                        "    L     \n" +
                        "    L     \n" +
                        "    LL    \n" +
                        "     I    \n" +
                        "   J I    \n" +
                        "   J I    \n" +
                        "  JJ I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,7]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'I'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "1:Answer: ACT(2)\n" +
                        "Fire Event: Event[figuresDropped:1:4]\n" +
                        "DICE:4\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    J     \n" +
                        "   JJS    \n" +
                        "   SS     \n" +
                        "    L     \n" +
                        "    L     \n" +
                        "    LL    \n" +
                        "     I    \n" +
                        "   J I    \n" +
                        "   J I    \n" +
                        "  JJ I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'I',\n" +
                        "1:    'S'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "Fire Event: Event[figuresDropped:1:1]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   JL     \n" +
                        "  JJLL    \n" +
                        "   SS     \n" +
                        "    L     \n" +
                        "    L     \n" +
                        "    LL    \n" +
                        "     I    \n" +
                        "   J I    \n" +
                        "   J I    \n" +
                        "  JJ I    \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'I',\n" +
                        "1:    'S',\n" +
                        "1:    'I'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "1:Answer: ACT(0)\n" +
                        "Fire Event: Event[glassOverflown:1:0]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    I     \n" +
                        "    I     \n" +
                        "    I     \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[3,8]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: Event[figuresDropped:1:0]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    I     \n" +
                        "    I     \n" +
                        "    I     \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'O',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "          \n" +
                        "          \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[3,8]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'O',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:0\n" +
                        "1:Answer: DOWN\n" +
                        "Fire Event: Event[figuresDropped:1:0]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   SS     \n" +
                        "          \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'O',\n" +
                        "1:    'O',\n" +
                        "1:    'J'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   SS     \n" +
                        "  SS      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[3,8]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'O',\n" +
                        "1:    'O',\n" +
                        "1:    'J'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "1:Answer: ACT(2)\n" +
                        "Fire Event: Event[figuresDropped:1:4]\n" +
                        "DICE:4\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   SI     \n" +
                        "  SSI     \n" +
                        "   II     \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "   I      \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,9]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'S'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "Fire Event: Event[glassOverflown:1:0]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "    OO    \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[4,10]',\n" +
                        "1:  'currentFigureType':'O',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "   OO     \n" +
                        "   OO     \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[3,9]',\n" +
                        "1:  'currentFigureType':'O',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:1\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'\n" +
                        "          \n" +
                        "  OO      \n" +
                        "  OO      \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "          \n" +
                        "',\n" +
                        "1:  'currentFigurePoint':'[2,8]',\n" +
                        "1:  'currentFigureType':'O',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "1:Answer: ACT(3)\n" +
                        "------------------------------------------",
                join);

    }
}
