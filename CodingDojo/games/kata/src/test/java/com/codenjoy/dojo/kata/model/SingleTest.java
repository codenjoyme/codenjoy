package com.codenjoy.dojo.kata.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software:you can redistribute it and/or modify
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


import com.codenjoy.dojo.kata.model.levels.Level;
import com.codenjoy.dojo.kata.model.levels.LevelsPoolImpl;
import com.codenjoy.dojo.kata.services.GameRunner;
import com.codenjoy.dojo.kata.services.events.NextAlgorithmEvent;
import com.codenjoy.dojo.kata.services.events.PassTestEvent;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Game game1;
    private Game game2;
    private Game game3;
    private Dice dice;
    private Kata field;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        Level level = new SimpleQATestLevel(
                "question1=answer1",
                "question2=answer2",
                "question3=answer3");

        dice = mock(Dice.class);
        field = new Kata(dice);
        PrinterFactory factory = new GameRunner().getPrinterFactory();

        listener1 = mock(EventListener.class);
        LevelsPoolImpl levelsPool1 = new LevelsPoolImpl(Arrays.asList(level));
        Player player1 = new Player(listener1, levelsPool1);
        game1 = new Single(player1, factory);
        game1.on(field);

        listener2 = mock(EventListener.class);
        LevelsPoolImpl levelsPool2 = new LevelsPoolImpl(Arrays.asList(level));
        Player player2 = new Player(listener2, levelsPool2);
        game2 = new Single(player2, factory);
        game2.on(field);

        listener3 = mock(EventListener.class);
        LevelsPoolImpl levelsPool3 = new LevelsPoolImpl(Arrays.asList(level));
        Player player3 = new Player(listener3, levelsPool3);
        game3 = new Single(player3, factory);
        game3.on(field);

        dice(1, 4);
        game1.newGame();

        dice(2, 2);
        game2.newGame();

        dice(3, 4);
        game3.newGame();
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    private void asrtFl1(String expected) {
        assertField(expected, game1);
    }

    private void assertField(String expected, Game game1) {
        assertEquals(expected, JsonUtils.prettyPrint(game1.getBoardAsString().toString()).replace('\"', '\'').replaceAll("\\r", ""));
    }

    private void asrtFl2(String expected) {
        assertField(expected, game2);
    }

    private void asrtFl3(String expected) {
        assertField(expected, game3);
    }

    // рисуем несколько игроков
    @Test
    public void shouldPrint() {
        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
    }

    // Каждый игрок может упраыляться за тик игры независимо,
    // все их последние ходы применяются после тика любой борды
    @Test
    public void shouldJoystick() {
        // when
        game1.getJoystick().message("['wrong-message']");
        game1.getJoystick().message("['answer1']");

        game2.getJoystick().message("['answer2']");

        game3.getJoystick().message("['answer3']");

        field.tick();

        // then
        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer2',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer3',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game3.close();

        field.tick();

        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        try {
            asrtFl3("{\n" +
                    "  'description':'description',\n" +
                    "  'history':[],\n" +
                    "  'level':0,\n" +
                    "  'nextQuestion':'question1',\n" +
                    "  'questions':[\n" +
                    "    'question1'\n" +
                    "  ]\n" +
                    "}");
        } catch (IllegalStateException e) {
            assertEquals("No board for this player", e.getMessage());
        }
    }

    // игрока можно ресетнуть
    @Test
    public void shouldKill() {
        // given
        game1.getJoystick().message("['answer1']");
        game2.getJoystick().message("['answer1']");
        game3.getJoystick().message("['answer1']");

        field.tick();

        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        // when
        game1.newGame();
        field.tick();

        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");
    }

    // игрок может ответить правильно и неправильно
    @Test
    public void shouldEvents() {
        // given
        game1.getJoystick().message("['answer1']");
        game2.getJoystick().message("['wrong2']");
        game3.getJoystick().message("['wrong3']");

        field.tick();

        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong2',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong3',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        // then
        assertPassTestEvent(listener1, "PassTest{complexity=30, testCount=3}");
        verifyNoMoreInteractions(listener2);
        verifyNoMoreInteractions(listener3);

        // when
        field.tick();

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
        verifyNoMoreInteractions(listener3);
    }

    private void assertPassTestEvent(EventListener listener, String expected) {
        ArgumentCaptor<PassTestEvent> captor = ArgumentCaptor.forClass(PassTestEvent.class);
        verify(listener).event(captor.capture());
        PassTestEvent value = captor.getValue();
        assertEquals(expected, value.toString());
    }

    private void assertNextAlgorithmEvent(EventListener listener, String expected) {
        ArgumentCaptor<NextAlgorithmEvent> captor = ArgumentCaptor.forClass(NextAlgorithmEvent.class);
        verify(listener, times(4)).event(captor.capture());
        NextAlgorithmEvent value = captor.getValue();
        assertEquals(expected, value.toString());
    }

    @Test
    public void shouldGetHeroes() {
        assertEquals(
                Arrays.asList(
                    game1.getJoystick(),
                    game2.getJoystick(),
                    game3.getJoystick()),
                field.getHeroes());
    }

    private void givenUser1GoesToEnd() {
        // given
        game1.getJoystick().message("['answer1']");
        game2.getJoystick().message("['wrong']");
        game3.getJoystick().message("['wrong']");

        field.tick();

        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        // when
        game1.getJoystick().message("['answer1', 'answer2']");
        field.tick();

        // then
        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    },\n" +
                "    {\n" +
                "      'answer':'answer2',\n" +
                "      'question':'question2',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question3',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2',\n" +
                "    'question3'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        // when
        game1.getJoystick().message("['answer1', 'answer2', 'answer3']");
        field.tick();

        // then
        // wait level
        asrtFl1("{\n" +
                "  'description':'Wait for next level. Please send 'message('StartNextLevel')' command.',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    },\n" +
                "    {\n" +
                "      'answer':'answer2',\n" +
                "      'question':'question2',\n" +
                "      'valid':true\n" +
                "    },\n" +
                "    {\n" +
                "      'answer':'answer3',\n" +
                "      'question':'question3',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'questions':[]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
        
        // when
        game1.getJoystick().message(Elements.START_NEXT_LEVEL);
        field.tick();

        // then
        // win level with clean history
        asrtFl1("{\n" +
                "  'description':'No more Levels. You win!',\n" +
                "  'history':[],\n" +
                "  'level':1,\n" +
                "  'questions':[]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        // when
        field.tick();

        // then 
        // still clear history
        asrtFl1("{\n" +
                "  'description':'No more Levels. You win!',\n" +
                "  'history':[],\n" +
                "  'level':1,\n" +
                "  'questions':[]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

    }

    // игрок дошел до конца и хистори не сохраняется больше
    @Test
    public void shouldNoHistory_whenEndGame() {
        givenUser1GoesToEnd();

        // when
        game1.getJoystick().message("['blablabla']");
        field.tick();

        // then
        asrtFl1("{\n" +
                "  'description':'No more Levels. You win!',\n" +
                "  'history':[],\n" +
                "  'level':1,\n" +
                "  'questions':[]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        // when
        game1.getJoystick().message("['qweasadzxc']");
        field.tick();

        // then
        asrtFl1("{\n" +
                "  'description':'No more Levels. You win!',\n" +
                "  'history':[],\n" +
                "  'level':1,\n" +
                "  'questions':[]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
    }

    // игрок дошел до конца и там факрится определенный ивент
    @Test
    public void shouldEvent_whenUserGoesToEnd() {
        givenUser1GoesToEnd();

        // then
        assertNextAlgorithmEvent(listener1, "NextAlgorithm{complexity=30, time=0}");
    }

    @Test
    public void bug() {
        // given
        game1.getJoystick().message("['answer1']");
        game2.getJoystick().message("['wrong']");
        game3.getJoystick().message("['wrong']");

        field.tick();

        asrtFl1("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'answer1',\n" +
                "      'question':'question1',\n" +
                "      'valid':true\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question2',\n" +
                "  'questions':[\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'description':'description',\n" +
                "  'history':[\n" +
                "    {\n" +
                "      'answer':'wrong',\n" +
                "      'question':'question1',\n" +
                "      'valid':false\n" +
                "    }\n" +
                "  ],\n" +
                "  'level':0,\n" +
                "  'nextQuestion':'question1',\n" +
                "  'questions':[\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
    }
}
