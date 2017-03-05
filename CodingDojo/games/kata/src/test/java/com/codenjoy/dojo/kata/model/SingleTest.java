package com.codenjoy.dojo.kata.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.kata.model.levels.Level;
import com.codenjoy.dojo.kata.model.levels.QuestionAnswerLevelImpl;
import com.codenjoy.dojo.kata.services.Events;
import com.codenjoy.dojo.services.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 5:22
 */
public class SingleTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Single game1;
    private Single game2;
    private Single game3;
    private Dice dice;
    private Kata kata;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        Level level = new QuestionAnswerLevelImpl(
                "question1=answer1",
                "question2=answer2",
                "question3=answer3")
        {
            @Override
            public int complexity() {
                return 0;
            }
        };

        dice = mock(Dice.class);
        kata = new Kata(dice);
        PrinterFactory factory = new PrinterFactoryImpl();

        listener1 = mock(EventListener.class);
        game1 = new Single(kata, listener1, factory, Arrays.asList(level));

        listener2 = mock(EventListener.class);
        game2 = new Single(kata, listener2, factory, Arrays.asList(level));

        listener3 = mock(EventListener.class);
        game3 = new Single(kata, listener3, factory, Arrays.asList(level));

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

    private void assertField(String expected, Single game1) {
        assertEquals(expected, JsonUtils.prettyPrint(game1.getBoardAsString().toString()).replace('\"', '\''));
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
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
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

        game1.tick();

        // then
        asrtFl1("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': false,\n" +
                "      'answer': 'answer2',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': false,\n" +
                "      'answer': 'answer3',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game3.destroy();

        game1.tick();

        asrtFl1("{\n" +
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");
    }

    // игрока можно ресетнуть
    @Test
    public void shouldKill() {
        // given
        game1.getJoystick().message("['answer1']");
        game2.getJoystick().message("['answer1']");
        game3.getJoystick().message("['answer1']");

        game1.tick();

        asrtFl1("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        // when
        game1.newGame();
        game1.tick();

        asrtFl1("{\n" +
                "  'history': [],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
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
        game2.getJoystick().message("['answer2']");
        game3.getJoystick().message("['answer3']");

        game1.tick();

        asrtFl1("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': true,\n" +
                "      'answer': 'answer1',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1',\n" +
                "    'question2'\n" +
                "  ]\n" +
                "}");

        asrtFl2("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': false,\n" +
                "      'answer': 'answer2',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        asrtFl3("{\n" +
                "  'history': [\n" +
                "    {\n" +
                "      'valid': false,\n" +
                "      'answer': 'answer3',\n" +
                "      'question': 'question1'\n" +
                "    }\n" +
                "  ],\n" +
                "  'nextQuestions': [\n" +
                "    'question1'\n" +
                "  ]\n" +
                "}");

        // then
        verify(listener1).event(Events.WIN);
        verify(listener2).event(Events.LOOSE);
        verify(listener3).event(Events.LOOSE);

        // when
        game1.tick();

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
        verifyNoMoreInteractions(listener3);
    }

    @Test
    public void shouldGetHeroes() {
        assertEquals(
                Arrays.asList(
                    game1.getJoystick(),
                    game2.getJoystick(),
                    game3.getJoystick()),
                kata.getHeroes());
    }
}
