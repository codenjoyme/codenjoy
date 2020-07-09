package com.codenjoy.dojo.kata;

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
import com.codenjoy.dojo.kata.client.Board;
import com.codenjoy.dojo.kata.client.ai.AISolver;
import com.codenjoy.dojo.kata.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SmokeTest {

    @Ignore // TODO разобраться почему при билде из Maven слетает с кодировкой
    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(0);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(),
                new Board());

        // then
        assertEquals("1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "1:  'history':[],\n" +
                        "1:  'level':0,\n" +
                        "1:  'nextQuestion':'hello',\n" +
                        "1:  'questions':[\n" +
                        "1:    'hello'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 0\n" +
                        "1:Answer: message('['world']')\n" +
                        "1:Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'world',\n" +
                        "1:      'question':'hello',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':0,\n" +
                        "1:  'nextQuestion':'world',\n" +
                        "1:  'questions':[\n" +
                        "1:    'hello',\n" +
                        "1:    'world'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 1\n" +
                        "1:Answer: message('['world', 'hello']')\n" +
                        "1:Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'world',\n" +
                        "1:      'question':'hello',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'hello',\n" +
                        "1:      'question':'world',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':0,\n" +
                        "1:  'nextQuestion':'qwe',\n" +
                        "1:  'questions':[\n" +
                        "1:    'hello',\n" +
                        "1:    'world',\n" +
                        "1:    'qwe'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 2\n" +
                        "1:Answer: message('['world', 'hello', 'qwe']')\n" +
                        "1:Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'world',\n" +
                        "1:      'question':'hello',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'hello',\n" +
                        "1:      'question':'world',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'qwe',\n" +
                        "1:      'question':'qwe',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':0,\n" +
                        "1:  'nextQuestion':'asd',\n" +
                        "1:  'questions':[\n" +
                        "1:    'hello',\n" +
                        "1:    'world',\n" +
                        "1:    'qwe',\n" +
                        "1:    'asd'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 3\n" +
                        "1:Answer: message('['world', 'hello', 'qwe', 'asd']')\n" +
                        "1:Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'world',\n" +
                        "1:      'question':'hello',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'hello',\n" +
                        "1:      'question':'world',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'qwe',\n" +
                        "1:      'question':'qwe',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'asd',\n" +
                        "1:      'question':'asd',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':0,\n" +
                        "1:  'nextQuestion':'zxc',\n" +
                        "1:  'questions':[\n" +
                        "1:    'hello',\n" +
                        "1:    'world',\n" +
                        "1:    'qwe',\n" +
                        "1:    'asd',\n" +
                        "1:    'zxc'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 4\n" +
                        "1:Answer: message('['world', 'hello', 'qwe', 'asd', 'zxc']')\n" +
                        "1:Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "1:Fire Event: NextAlgorithm{complexity=0, time=0}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Wait for next level. Please send 'message('StartNextLevel')' command.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'world',\n" +
                        "1:      'question':'hello',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'hello',\n" +
                        "1:      'question':'world',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'qwe',\n" +
                        "1:      'question':'qwe',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'asd',\n" +
                        "1:      'question':'asd',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'zxc',\n" +
                        "1:      'question':'zxc',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':0,\n" +
                        "1:  'questions':[]\n" +
                        "1:}\n" +
                        "1:Scores: 5\n" +
                        "1:Answer: message('StartNextLevel')\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'1',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 5\n" +
                        "1:Answer: message('['1']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'2',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 7\n" +
                        "1:Answer: message('['1', '2']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'3',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 9\n" +
                        "1:Answer: message('['1', '2', 'Fizz']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'3',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'4',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3',\n" +
                        "1:    '4'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 11\n" +
                        "1:Answer: message('['1', '2', 'Fizz', '4']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'4',\n" +
                        "1:      'question':'4',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'5',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3',\n" +
                        "1:    '4',\n" +
                        "1:    '5'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 13\n" +
                        "1:Answer: message('['1', '2', 'Fizz', '4', 'Buzz']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'4',\n" +
                        "1:      'question':'4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Buzz',\n" +
                        "1:      'question':'5',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'6',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3',\n" +
                        "1:    '4',\n" +
                        "1:    '5',\n" +
                        "1:    '6'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 15\n" +
                        "1:Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'4',\n" +
                        "1:      'question':'4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Buzz',\n" +
                        "1:      'question':'5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'6',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'7',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3',\n" +
                        "1:    '4',\n" +
                        "1:    '5',\n" +
                        "1:    '6',\n" +
                        "1:    '7'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 17\n" +
                        "1:Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'4',\n" +
                        "1:      'question':'4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Buzz',\n" +
                        "1:      'question':'5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'6',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'7',\n" +
                        "1:      'question':'7',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'8',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3',\n" +
                        "1:    '4',\n" +
                        "1:    '5',\n" +
                        "1:    '6',\n" +
                        "1:    '7',\n" +
                        "1:    '8'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 19\n" +
                        "1:Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7', '8']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------\n" +
                        "1:BoardData {\n" +
                        "1:  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "1:  'history':[\n" +
                        "1:    {\n" +
                        "1:      'answer':'1',\n" +
                        "1:      'question':'1',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'2',\n" +
                        "1:      'question':'2',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'3',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'4',\n" +
                        "1:      'question':'4',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Buzz',\n" +
                        "1:      'question':'5',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'Fizz',\n" +
                        "1:      'question':'6',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'7',\n" +
                        "1:      'question':'7',\n" +
                        "1:      'valid':true\n" +
                        "1:    },\n" +
                        "1:    {\n" +
                        "1:      'answer':'8',\n" +
                        "1:      'question':'8',\n" +
                        "1:      'valid':true\n" +
                        "1:    }\n" +
                        "1:  ],\n" +
                        "1:  'level':1,\n" +
                        "1:  'nextQuestion':'9',\n" +
                        "1:  'questions':[\n" +
                        "1:    '1',\n" +
                        "1:    '2',\n" +
                        "1:    '3',\n" +
                        "1:    '4',\n" +
                        "1:    '5',\n" +
                        "1:    '6',\n" +
                        "1:    '7',\n" +
                        "1:    '8',\n" +
                        "1:    '9'\n" +
                        "1:  ]\n" +
                        "1:}\n" +
                        "1:Scores: 21\n" +
                        "1:Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7', '8', 'Fizz']')\n" +
                        "1:Fire Event: PassTest{complexity=5, testCount=25}\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
