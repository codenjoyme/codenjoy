package com.codenjoy.dojo.sampletext.model;

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


import com.codenjoy.dojo.sampletext.services.Events;
import com.codenjoy.dojo.sampletext.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;

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
    private SampleText field;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        Level level = new LevelImpl(
                "question1=answer1",
                "question2=answer2",
                "question3=answer3");

        dice = mock(Dice.class);
        field = new SampleText(level, dice);
        PrinterFactory factory = new GameRunner().getPrinterFactory();

        listener1 = mock(EventListener.class);
        game1 = new Single(new Player(listener1), factory);
        game1.on(field);

        listener2 = mock(EventListener.class);
        game2 = new Single(new Player(listener2), factory);
        game2.on(field);

        listener3 = mock(EventListener.class);
        game3 = new Single(new Player(listener3), factory);
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

    private void assertField(String expected, Game game) {
        assertEquals(expected, JsonUtils.toStringSorted(game.getBoardAsString().toString()).replace('\"', '\''));
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
        asrtFl1("{'history':[],'nextQuestion':'question1'}");
        asrtFl2("{'history':[],'nextQuestion':'question1'}");
        asrtFl3("{'history':[],'nextQuestion':'question1'}");
    }

    // Каждый игрок может упраыляться за тик игры независимо,
    // все их последние ходы применяются после тика любой борды
    @Test
    public void shouldJoystick() {
        // when
        game1.getJoystick().message("wrong-message");
        game1.getJoystick().message("answer1");

        game2.getJoystick().message("answer2");

        game3.getJoystick().message("answer3");

        field.tick();

        // then
        asrtFl1("{'history':[{'answer':'answer1','question':'question1','valid':true}]," +
                "'nextQuestion':'question2'}");

        asrtFl2("{'history':[{'answer':'answer2','question':'question1','valid':false}]," +
                "'nextQuestion':'question1'}");

        asrtFl3("{'history':[{'answer':'answer3','question':'question1','valid':false}]," +
                "'nextQuestion':'question1'}");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game3.close();

        field.tick();

        asrtFl1("{'history':[],'nextQuestion':'question1'}");
        asrtFl2("{'history':[],'nextQuestion':'question1'}");
        try {
            asrtFl3("{'history':[],'nextQuestion':'question1'}");
        } catch (IllegalStateException e) {
            assertEquals("No board for this player", e.getMessage());
        }
    }

    // игрока можно ресетнуть
    @Test
    public void shouldKill() {
        // given
        game1.getJoystick().message("answer1");
        game2.getJoystick().message("answer1");
        game3.getJoystick().message("answer1");

        field.tick();

        asrtFl1("{'history':[{'answer':'answer1','question':'question1','valid':true}],'nextQuestion':'question2'}");
        asrtFl2("{'history':[{'answer':'answer1','question':'question1','valid':true}],'nextQuestion':'question2'}");
        asrtFl3("{'history':[{'answer':'answer1','question':'question1','valid':true}],'nextQuestion':'question2'}");

        // when
        game1.newGame();
        field.tick();

        asrtFl1("{'history':[],'nextQuestion':'question1'}");
        asrtFl2("{'history':[{'answer':'answer1','question':'question1','valid':true}],'nextQuestion':'question2'}");
        asrtFl3("{'history':[{'answer':'answer1','question':'question1','valid':true}],'nextQuestion':'question2'}");
    }

    // игрок может ответить правильно и неправильно
    @Test
    public void shouldEvents() {
        // given
        game1.getJoystick().message("answer1");
        game2.getJoystick().message("answer2");
        game3.getJoystick().message("answer3");

        field.tick();

        asrtFl1("{'history':[{'answer':'answer1','question':'question1','valid':true}],'nextQuestion':'question2'}");
        asrtFl2("{'history':[{'answer':'answer2','question':'question1','valid':false}],'nextQuestion':'question1'}");
        asrtFl3("{'history':[{'answer':'answer3','question':'question1','valid':false}],'nextQuestion':'question1'}");

        // then
        verify(listener1).event(Events.WIN);
        verify(listener2).event(Events.LOOSE);
        verify(listener3).event(Events.LOOSE);

        // when
        field.tick();

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
        verifyNoMoreInteractions(listener3);
    }
}
