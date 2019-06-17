package com.codenjoy.dojo.excitebike.model;

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


import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Game game1;
    private Game game2;
    private Game game3;
    private Dice dice;
    private GameFieldImpl field;

    @Before
    public void setup() {
        MapParser mapParser = new MapParserImpl("■■■■■■■" +
                        "       " +
                        "       " +
                        "       " +
                        "       " +
                        "       " +
                        "■■■■■■■");

        dice = mock(Dice.class);
        field = new GameFieldImpl(mapParser, dice);
        PrinterFactory factory = new PrinterFactoryImpl();

        listener1 = mock(EventListener.class);
        game1 = new Single(new Player(listener1), factory);
        game1.on(field);

        listener2 = mock(EventListener.class);
        game2 = new Single(new Player(listener2), factory);
        game2.on(field);

        listener3 = mock(EventListener.class);
        game3 = new Single(new Player(listener3), factory);
        game3.on(field);

        game1.newGame();

        game2.newGame();

        game3.newGame();
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    private void asrtFl1(String expected) {
        assertEquals(expected, game1.getBoardAsString());
    }

    private void asrtFl2(String expected) {
        assertEquals(expected, game2.getBoardAsString());
    }

    private void asrtFl3(String expected) {
        assertEquals(expected, game3.getBoardAsString());
    }

    @Test
    public void shouldPrint() {
        asrtFl1("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "■■■■■■■\n");

        asrtFl2("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                " o     \n" +
                " e     \n" +
                "■■■■■■■\n");

        asrtFl3("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " o     \n" +
                " e     \n" +
                " e     \n" +
                "■■■■■■■\n");
    }

    @Test
    public void shouldJoystick() {
        when(dice.next(anyInt())).thenReturn(5);

        game3.getJoystick().up();
        game2.getJoystick().left();
        game1.getJoystick().down();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                " e     \n" +
                "       \n" +
                " z     \n" +
                " o     \n" +
                "■■■■■■■\n");
    }

    @Test
    public void shouldRemove() {
        when(dice.next(anyInt())).thenReturn(5);

        game3.close();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                " o     \n" +
                "■■■■■■■\n");
    }

//    // игрок может взорваться на бомбе
//    @Test
//    public void shouldKill() {
//        game1.getJoystick().down();
//        game1.getJoystick().act();
//        game3.getJoystick().left();
//
//        field.tick();
//
//        asrtFl1("☼☼☼☼☼☼\n" +
//                "☼x☻ $☼\n" +
//                "☼☺   ☼\n" +
//                "☼ ☻  ☼\n" +
//                "☼    ☼\n" +
//                "☼☼☼☼☼☼\n");
//
//        game3.getJoystick().left();
//        field.tick();
//
//        asrtFl1("☼☼☼☼☼☼\n" +
//                "☼X  $☼\n" +
//                "☼☺   ☼\n" +
//                "☼ ☻  ☼\n" +
//                "☼    ☼\n" +
//                "☼☼☼☼☼☼\n");
//
//        verify(listener3).event(Events.LOOSE);
//        assertTrue(game3.isGameOver());
//
//        dice(4, 1);
//        game3.newGame();
//
//        field.tick();
//
//        asrtFl1("☼☼☼☼☼☼\n" +
//                "☼   $☼\n" +
//                "☼☺   ☼\n" +
//                "☼ ☻  ☼\n" +
//                "☼   ☻☼\n" +
//                "☼☼☼☼☼☼\n");
//    }
//
//    // игрок может подобрать золото
//    @Test
//    public void shouldGetGold() {
//        game3.getJoystick().right();
//
//        dice(1, 2);
//
//        field.tick();
//
//        asrtFl1("☼☼☼☼☼☼\n" +
//                "☼☺  ☻☼\n" +
//                "☼    ☼\n" +
//                "☼$☻  ☼\n" +
//                "☼    ☼\n" +
//                "☼☼☼☼☼☼\n");
//
//        verify(listener3).event(Events.WIN);
//    }
//
//    // игрок не может пойи на другого игрока
//    @Test
//    public void shouldCantGoOnHero() {
//        game1.getJoystick().right();
//        game3.getJoystick().left();
//
//        field.tick();
//
//        asrtFl1("☼☼☼☼☼☼\n" +
//                "☼ ☺☻$☼\n" +
//                "☼    ☼\n" +
//                "☼ ☻  ☼\n" +
//                "☼    ☼\n" +
//                "☼☼☼☼☼☼\n");
//    }

    @Test
    public void shouldCrushEnemyBikeAfterClash() {
        when(dice.next(anyInt())).thenReturn(5);

        game1.getJoystick().up();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                "_      \n" +
                " o     \n" +
                "■■■■■■■\n");

        assertTrue(game2.isGameOver());
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther() {
        when(dice.next(anyInt())).thenReturn(5);

        game1.getJoystick().up();
        game2.getJoystick().down();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "■■■■■■■\n");

        game3.getJoystick().up();
        game2.getJoystick().up();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                "       \n" +
                " o     \n" +
                "■■■■■■■\n");

//        TODO
//        game1.getJoystick().up();
//        game2.getJoystick().down();
//
//        field.tick();
//
//        asrtFl1("■■■■■■■\n" +
//                "       \n" +
//                " e     \n" +
//                " e     \n" +
//                "       \n" +
//                " o     \n" +
//                "■■■■■■■\n");

    }

    @Test
    public void shouldMoveBikesInAnyOrderOfCall() {
        when(dice.next(anyInt())).thenReturn(5);

        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().up();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "       \n" +
                "■■■■■■■\n");

        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().up();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "       \n" +
                "       \n" +
                "■■■■■■■\n");

        game2.getJoystick().down();
        game3.getJoystick().down();
        game1.getJoystick().down();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "       \n" +
                "■■■■■■■\n");

        game3.getJoystick().down();
        game2.getJoystick().down();
        game1.getJoystick().down();

        field.tick();

        asrtFl1("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "■■■■■■■\n");
    }

}
