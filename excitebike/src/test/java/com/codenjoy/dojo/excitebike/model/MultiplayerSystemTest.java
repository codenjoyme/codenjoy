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


import com.codenjoy.dojo.excitebike.model.items.bike.Bike;
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

public class MultiplayerSystemTest {

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
                "e      \n" +
                " e     \n" +
                "o      \n" +
                "■■■■■■■\n");

        asrtFl2("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "e      \n" +
                " o     \n" +
                "e      \n" +
                "■■■■■■■\n");

        asrtFl3("■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "o      \n" +
                " e     \n" +
                "e      \n" +
                "■■■■■■■\n");
    }

    @Test
    public void shouldJoystick() {
        //given
        when(dice.next(anyInt())).thenReturn(5);

        game3.getJoystick().up();
        game2.getJoystick().left();
        game1.getJoystick().down();

        //when
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "e      \n" +
                "       \n" +
                " z     \n" +
                "o      \n" +
                "■■■■■■■\n";
        assertEquals(expected, game1.getBoardAsString());
    }

    @Test
    public void shouldRemove() {
        //given
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game3.close();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                "o      \n" +
                "■■■■■■■\n";
        assertEquals(expected, game1.getBoardAsString());
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash() {
        //given
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX()+1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX()+1);

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                "_o     \n" +
                "       \n" +
                "■■■■■■■\n";
        assertEquals(expected, game1.getBoardAsString());
        assertTrue(game2.isGameOver());
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash2() {
        //given
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX()+1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX()+1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "_e     \n" +
                " o     \n" +
                "       \n" +
                "■■■■■■■\n";
        assertEquals(expected, game1.getBoardAsString());
        assertTrue(game3.isGameOver());
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash3() {
        //given
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX()+1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX()+1);

        //when
        game1.getJoystick().down();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                "       \n" +
                "_o     \n" +
                "■■■■■■■\n";
        assertEquals(expected, game2.getBoardAsString());
        assertTrue(game1.isGameOver());
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther() {
        //given
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX()+1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX()+1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "■■■■■■■\n";
        assertEquals(expected, game1.getBoardAsString());
    }

    @Test
    public void shouldMoveBikesInAnyOrderOfCall() {
        //given
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX()+1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX()+1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                " e     \n" +
                " e     \n" +
                " o     \n" +
                "       \n" +
                "■■■■■■■\n";
        assertEquals(expected, game1.getBoardAsString());
    }
}
