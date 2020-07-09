package com.codenjoy.dojo.sampletext;

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
import com.codenjoy.dojo.sampletext.client.Board;
import com.codenjoy.dojo.sampletext.client.ai.AISolver;
import com.codenjoy.dojo.sampletext.services.GameRunner;
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
        LocalGameRunner.countIterations = 10;

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
        assertEquals("1:BoardData {\n" +
                        "1:  'history':[],\n" +
                        "1:  'nextQuestion':'question1'\n" +
                        "1:}\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: message('answer1')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question2'\n" +
                        "1:}\n" +
                        "1:Scores: 30\n" +
                        "1:Answer: message('answer2')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question3'\n" +
                        "1:}\n" +
                        "1:Scores: 60\n" +
                        "1:Answer: message('answer3')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question4'\n" +
                        "1:}\n" +
                        "1:Scores: 90\n" +
                        "1:Answer: message('answer4')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer4',\n" +
                        "1:      'question':'question4',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question5'\n" +
                        "1:}\n" +
                        "1:Scores: 120\n" +
                        "1:Answer: message('answer5')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer4',\n" +
                        "1:      'question':'question4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer5',\n" +
                        "1:      'question':'question5',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question6'\n" +
                        "1:}\n" +
                        "1:Scores: 150\n" +
                        "1:Answer: message('answer6')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer4',\n" +
                        "1:      'question':'question4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer5',\n" +
                        "1:      'question':'question5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer6',\n" +
                        "1:      'question':'question6',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question7'\n" +
                        "1:}\n" +
                        "1:Scores: 180\n" +
                        "1:Answer: message('answer7')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer4',\n" +
                        "1:      'question':'question4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer5',\n" +
                        "1:      'question':'question5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer6',\n" +
                        "1:      'question':'question6',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer7',\n" +
                        "1:      'question':'question7',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question8'\n" +
                        "1:}\n" +
                        "1:Scores: 210\n" +
                        "1:Answer: message('answer8')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer4',\n" +
                        "1:      'question':'question4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer5',\n" +
                        "1:      'question':'question5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer6',\n" +
                        "1:      'question':'question6',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer7',\n" +
                        "1:      'question':'question7',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer8',\n" +
                        "1:      'question':'question8',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question9'\n" +
                        "1:}\n" +
                        "1:Scores: 240\n" +
                        "1:Answer: message('answer9')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer1',\n" +
                        "1:      'question':'question1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer2',\n" +
                        "1:      'question':'question2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer3',\n" +
                        "1:      'question':'question3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer4',\n" +
                        "1:      'question':'question4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer5',\n" +
                        "1:      'question':'question5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer6',\n" +
                        "1:      'question':'question6',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer7',\n" +
                        "1:      'question':'question7',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer8',\n" +
                        "1:      'question':'question8',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'answer9',\n" +
                        "1:      'question':'question9',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'nextQuestion':'question10'\n" +
                        "1:}\n" +
                        "1:Scores: 270\n" +
                        "1:Answer: message('answer10')\n" +
                        "1:Fire Event: WIN\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
