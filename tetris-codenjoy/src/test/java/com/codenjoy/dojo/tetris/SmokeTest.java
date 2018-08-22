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
        LocalGameRunner.countIterations = 15;

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
                settings.addEditBox("Glass Size").type(Integer.class).update(5);
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
        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'I',\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'I',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':0\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "Fire Event: Event[figuresDropped:1:0]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'      I    I    I    I   ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'L',\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'blue'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      }\n" +
                        "1:    ]\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "Fire Event: Event[glassOverflown:1:0]\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':3\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':2\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':1\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'L',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'S',\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':0\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "Fire Event: Event[figuresDropped:1:2]\n" +
                        "DICE:3\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'           L    L    LL  ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J',\n" +
                        "1:    'L'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      }\n" +
                        "1:    ]\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'           L    L    LL  ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'S',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'O',\n" +
                        "1:    'J',\n" +
                        "1:    'J',\n" +
                        "1:    'L'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      }\n" +
                        "1:    ]\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "Fire Event: Event[figuresDropped:1:4]\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':' SS  SS    L    L    LL  ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'O',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'yellow'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'yellow'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'yellow'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'yellow'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':4\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':0\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'orange'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'green'\n" +
                        "1:        },\n" +
                        "1:        'x':2,\n" +
                        "1:        'y':4\n" +
                        "1:      }\n" +
                        "1:    ]\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "Fire Event: Event[glassOverflown:1:0]\n" +
                        "DICE:0\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':5\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':4\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'board':'                         ',\n" +
                        "1:  'currentFigurePoint':'[-1,-1]',\n" +
                        "1:  'currentFigureType':'J',\n" +
                        "1:  'futureFigures':[\n" +
                        "1:    'J',\n" +
                        "1:    'L',\n" +
                        "1:    'I',\n" +
                        "1:    'O'\n" +
                        "1:  ],\n" +
                        "1:  'layers':[\n" +
                        "1:    [\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':3\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':2\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':0,\n" +
                        "1:        'y':1\n" +
                        "1:      },\n" +
                        "1:      {\n" +
                        "1:        'color':{\n" +
                        "1:          'name':'cyan'\n" +
                        "1:        },\n" +
                        "1:        'x':1,\n" +
                        "1:        'y':1\n" +
                        "1:      }\n" +
                        "1:    ],\n" +
                        "1:    []\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Answer: message('DOWN')\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
