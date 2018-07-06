package com.codenjoy.dojo.kata;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.kata.client.Board;
import com.codenjoy.dojo.kata.client.ai.ApofigSolver;
import com.codenjoy.dojo.kata.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SmokeTest {
    private int index;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(0);
        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new ApofigSolver(),
                new Board());

        // then
        assertEquals("BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "  'history':[],\n" +
                        "  'level':0,\n" +
                        "  'nextQuestion':'hello',\n" +
                        "  'questions':[\n" +
                        "    'hello'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['world']')\n" +
                        "Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'world',\n" +
                        "      'question':'hello',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':0,\n" +
                        "  'nextQuestion':'world',\n" +
                        "  'questions':[\n" +
                        "    'hello',\n" +
                        "    'world'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['world', 'hello']')\n" +
                        "Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'world',\n" +
                        "      'question':'hello',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'hello',\n" +
                        "      'question':'world',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':0,\n" +
                        "  'nextQuestion':'qwe',\n" +
                        "  'questions':[\n" +
                        "    'hello',\n" +
                        "    'world',\n" +
                        "    'qwe'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['world', 'hello', 'qwe']')\n" +
                        "Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'world',\n" +
                        "      'question':'hello',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'hello',\n" +
                        "      'question':'world',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'qwe',\n" +
                        "      'question':'qwe',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':0,\n" +
                        "  'nextQuestion':'asd',\n" +
                        "  'questions':[\n" +
                        "    'hello',\n" +
                        "    'world',\n" +
                        "    'qwe',\n" +
                        "    'asd'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['world', 'hello', 'qwe', 'asd']')\n" +
                        "Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один String аргумент и возвращающий строку 'world' если на вход пришло 'hello',и 'hello' - если пришло 'world', в противном случае алгоритм должен вернуть ту же строчку, что пришла на вход. \\nАлгоритм реализован для проверки конневшена клиента к серверу.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'world',\n" +
                        "      'question':'hello',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'hello',\n" +
                        "      'question':'world',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'qwe',\n" +
                        "      'question':'qwe',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'asd',\n" +
                        "      'question':'asd',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':0,\n" +
                        "  'nextQuestion':'zxc',\n" +
                        "  'questions':[\n" +
                        "    'hello',\n" +
                        "    'world',\n" +
                        "    'qwe',\n" +
                        "    'asd',\n" +
                        "    'zxc'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['world', 'hello', 'qwe', 'asd', 'zxc']')\n" +
                        "Fire Event: PassTest{complexity=0, testCount=5}\n" +
                        "Fire Event: NextAlgorithm{complexity=0, time=0}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Wait for next level. Please send 'message('StartNextLevel')' command.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'world',\n" +
                        "      'question':'hello',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'hello',\n" +
                        "      'question':'world',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'qwe',\n" +
                        "      'question':'qwe',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'asd',\n" +
                        "      'question':'asd',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'zxc',\n" +
                        "      'question':'zxc',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':0,\n" +
                        "  'questions':[]\n" +
                        "}\n" +
                        "Answer: message('StartNextLevel')\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'1',\n" +
                        "  'questions':[\n" +
                        "    '1'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'2',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'3',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'3',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'4',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3',\n" +
                        "    '4'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz', '4']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'3',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'4',\n" +
                        "      'question':'4',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'5',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3',\n" +
                        "    '4',\n" +
                        "    '5'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz', '4', 'Buzz']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'3',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'4',\n" +
                        "      'question':'4',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Buzz',\n" +
                        "      'question':'5',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'6',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3',\n" +
                        "    '4',\n" +
                        "    '5',\n" +
                        "    '6'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'3',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'4',\n" +
                        "      'question':'4',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Buzz',\n" +
                        "      'question':'5',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'6',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'7',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3',\n" +
                        "    '4',\n" +
                        "    '5',\n" +
                        "    '6',\n" +
                        "    '7'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'3',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'4',\n" +
                        "      'question':'4',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Buzz',\n" +
                        "      'question':'5',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'6',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'7',\n" +
                        "      'question':'7',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'8',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3',\n" +
                        "    '4',\n" +
                        "    '5',\n" +
                        "    '6',\n" +
                        "    '7',\n" +
                        "    '8'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7', '8']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------\n" +
                        "BoardData {\n" +
                        "  'description':'Напиши метод, принимающий один int аргумент и возвращающий String. Для тех чисел, которые делятся нацело на 3 метод должен вернуть “Fizz”, для тех, что делятся на 5 - “Buzz”, для тех же, что делятся и на 3 и на 5 - “FizzBuzz”, ну а для всех остальных - само число.',\n" +
                        "  'history':[\n" +
                        "    {\n" +
                        "      'answer':'1',\n" +
                        "      'question':'1',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'2',\n" +
                        "      'question':'2',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'3',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'4',\n" +
                        "      'question':'4',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Buzz',\n" +
                        "      'question':'5',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'Fizz',\n" +
                        "      'question':'6',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'7',\n" +
                        "      'question':'7',\n" +
                        "      'valid':true\n" +
                        "    },\n" +
                        "    {\n" +
                        "      'answer':'8',\n" +
                        "      'question':'8',\n" +
                        "      'valid':true\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  'level':1,\n" +
                        "  'nextQuestion':'9',\n" +
                        "  'questions':[\n" +
                        "    '1',\n" +
                        "    '2',\n" +
                        "    '3',\n" +
                        "    '4',\n" +
                        "    '5',\n" +
                        "    '6',\n" +
                        "    '7',\n" +
                        "    '8',\n" +
                        "    '9'\n" +
                        "  ]\n" +
                        "}\n" +
                        "Answer: message('['1', '2', 'Fizz', '4', 'Buzz', 'Fizz', '7', '8', 'Fizz']')\n" +
                        "Fire Event: PassTest{complexity=1, testCount=25}\n" +
                        "------------------------------------------------------------------------------------",
                String.join("\n", messages));

    }
}
