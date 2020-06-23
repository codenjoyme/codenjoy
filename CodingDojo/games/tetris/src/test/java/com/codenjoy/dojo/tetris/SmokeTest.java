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


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.tetris.client.Board;
import com.codenjoy.dojo.tetris.client.ai.AISolver;
import com.codenjoy.dojo.tetris.model.levels.level.AllFigureLevels;
import com.codenjoy.dojo.tetris.services.GameRunner;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.util.Arrays;
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
                settings.getParameter("Glass Size").update(10);
                settings.getParameter("Game Levels").update(AllFigureLevels.class.getSimpleName());
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
        List<String> result = Arrays.asList(join.split("\n"));
        for (int i = 0; i < result.size(); i++) {
            String line = result.get(i);
            if (line.contains("1:  'layers'")) {
                i++;
                line = result.get(i);
                int beginIndex = "1:    '".length();
                int toIndex = line.length() - "'".length();
                String board = line.substring(beginIndex, toIndex);
                line = line.substring(0, beginIndex)
                        + '\n'
                        + TestUtils.injectN(board)
                        + line.substring(toIndex);
                result.set(i, line);
            }
        }

        join = String.join("\n", result);

        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "1:BoardData {\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: \n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':10\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....I.....\n" +
                        "....I.....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: ACT(1),LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:2]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....J....\n" +
                        "....JJ....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "IIII......\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 2\n" +
                        "1:Answer: ACT(1),DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:3]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....L.....\n" +
                        "....LL....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "....J.....\n" +
                        "IIIIJJJ...\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 5\n" +
                        "1:Answer: RIGHT,RIGHT,RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:4]\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':8\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J',\n" +
                        "1:    'L'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....SS...\n" +
                        "....SS....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".......L..\n" +
                        "....J..L..\n" +
                        "IIIIJJJLL.\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 9\n" +
                        "1:Answer: ACT(1),RIGHT,RIGHT,RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:5]\n" +
                        "1:Fire Event: Event[linesRemoved:1:1]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'O',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....OO....\n" +
                        "....OO....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".......LS.\n" +
                        "....J..LSS\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 24\n" +
                        "1:Answer: LEFT,LEFT,LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:1]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....J....\n" +
                        "....JJ....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "OO.....LS.\n" +
                        "OO..J..LSS\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 25\n" +
                        "1:Answer: LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:3]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'O',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....J....\n" +
                        "....JJ....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "...J......\n" +
                        "OO.J...LS.\n" +
                        "OOJJJ..LSS\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 28\n" +
                        "1:Answer: RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:3]\n" +
                        "1:Fire Event: Event[linesRemoved:1:1]\n" +
                        "DICE:4\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'O',\n" +
                        "1:    'I',\n" +
                        "1:    'S'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....L.....\n" +
                        "....LL....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "...J..J...\n" +
                        "OO.J..JLS.\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 41\n" +
                        "1:Answer: DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:4]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':10\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'I',\n" +
                        "1:    'S',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....I.....\n" +
                        "....I.....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "....L.....\n" +
                        "...JL.J...\n" +
                        "OO.JLLJLS.\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 45\n" +
                        "1:Answer: LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:2]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'O',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....OO....\n" +
                        "....OO....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..I.......\n" +
                        "..I.L.....\n" +
                        "..IJL.J...\n" +
                        "OOIJLLJLS.\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 47\n" +
                        "1:Answer: LEFT,LEFT,LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:1]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':10\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'J',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....I.....\n" +
                        "....I.....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..I.......\n" +
                        "OOI.L.....\n" +
                        "OOIJL.J...\n" +
                        "OOIJLLJLS.\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 48\n" +
                        "1:Answer: RIGHT,RIGHT,RIGHT,RIGHT,RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:2]\n" +
                        "1:Fire Event: Event[linesRemoved:1:1]\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':8\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'J',\n" +
                        "1:    'I',\n" +
                        "1:    'L'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....SS...\n" +
                        "....SS....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..I......I\n" +
                        "OOI.L....I\n" +
                        "OOIJL.J..I\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 60\n" +
                        "1:Answer: ACT(1),RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:5]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':10\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'I',\n" +
                        "1:    'L',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....I.....\n" +
                        "....I.....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..I...S..I\n" +
                        "OOI.L.SS.I\n" +
                        "OOIJL.JS.I\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 65\n" +
                        "1:Answer: RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:2]\n" +
                        "DICE:4\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'L',\n" +
                        "1:    'J',\n" +
                        "1:    'S'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....J....\n" +
                        "....JJ....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".....I....\n" +
                        "..I..IS..I\n" +
                        "OOI.LISS.I\n" +
                        "OOIJLIJS.I\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 67\n" +
                        "1:Answer: LEFT,LEFT,LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:3]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':10\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'J',\n" +
                        "1:    'S',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....I.....\n" +
                        "....I.....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".J........\n" +
                        ".J...I....\n" +
                        "JJI..IS..I\n" +
                        "OOI.LISS.I\n" +
                        "OOIJLIJS.I\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 70\n" +
                        "1:Answer: RIGHT,RIGHT,RIGHT,RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:2]\n" +
                        "1:Fire Event: Event[linesRemoved:1:1]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....L.....\n" +
                        "....LL....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".J........\n" +
                        ".J...I..I.\n" +
                        "JJI..IS.II\n" +
                        "OOI.LISSII\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 82\n" +
                        "1:Answer: ACT(2),LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:4]\n" +
                        "1:Fire Event: Event[linesRemoved:1:1]\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':9\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'I',\n" +
                        "1:    'I',\n" +
                        "1:    'L'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....J....\n" +
                        "....JJ....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".J........\n" +
                        ".JLL.I..I.\n" +
                        "JJIL.IS.II\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 96\n" +
                        "1:Answer: ACT(2),LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:3]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':5,\n" +
                        "1:    'y':8\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'I',\n" +
                        "1:    'L',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        ".....SS...\n" +
                        "....SS....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".J..JJ....\n" +
                        ".JLLJI..I.\n" +
                        "JJILJIS.II\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 99\n" +
                        "1:Answer: ACT(1),RIGHT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:5]\n" +
                        "1:Fire Event: Event[linesRemoved:1:1]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'currentFigurePoint':{\n" +
                        "1:    'x':4,\n" +
                        "1:    'y':10\n" +
                        "1:  },\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'L',\n" +
                        "1:    'O',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    '\n" +
                        "....I.....\n" +
                        "....I.....\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        "..........\n" +
                        ".J..JJS...\n" +
                        ".JLLJISSI.\n" +
                        "'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 114\n" +
                        "1:Answer: LEFT,LEFT,LEFT,LEFT,DOWN\n" +
                        "1:Fire Event: Event[figuresDropped:1:2]\n" +
                        "DICE:1\n" +
                        "------------------------------------------",
                join);

    }
}
