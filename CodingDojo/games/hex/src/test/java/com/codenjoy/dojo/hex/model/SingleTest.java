package com.codenjoy.dojo.hex.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private Hex game;
    private Level level;
    private Dice dice;
    private int count = 2;
    private EventListener listener;
    private PrinterFactory printerFactory;

    public void givenGame() {
        level = mock(Level.class);
        printerFactory = new PrinterFactoryImpl();
        listener = mock(EventListener.class);
        when(level.getSize()).thenReturn(5);

        game = new Hex(level, dice);
        List<Game> games = new LinkedList<>();
        for (int index = 0; index < count; index++) {
            Game game = new Single(new Player(listener), printerFactory);
            game.on(this.game);
            games.add(game);
            game.newGame();
        }
    }

    // вводится 3-4-5-n игрок на поле
    @Test
    public void shouldManyPlayers() {
        count = Elements.heroesElements().size() + 1;
        givenDice(0,0, 0,2, 0,4, 1,1, 1,3, 2,0, 2,2, 2,4, 3,1, 3,3, 4,0, 4,2, 4,4);
        givenGame();

        // then
        assertF("♦ ○  " +
                " ♠ ♂ " +
                "♥ ◘ ♀" +
                " ♣ ◙ " +
                "☺ • ♀", game.getPlayers().get(0));
    }

    // нельзя ходить по другим игрокам
    @Test
    public void shouldNoWayAtHero() {
        count = 3;
        givenDice(0,0, 0,1, 0,2);
        givenGame();

        assertF("     " +
                "     " +
                "♦    " +
                "♥    " +
                "☺    ", game.getPlayers().get(0));

        assertF("     " +
                "     " +
                "♦    " +
                "☺    " +
                "☻    ", game.getPlayers().get(1));

        assertF("     " +
                "     " +
                "☺    " +
                "♥    " +
                "☻    ", game.getPlayers().get(2));

        // when
        Player player = game.getPlayers().get(1);
        Joystick joystick = player.getJoystick();
        joystick.act(0, 1); // ☻
        joystick.up();
        game.tick();

        // then
        assertF("     " +
                "     " +
                "♦    " +
                "♥    " +
                "☺    ", game.getPlayers().get(0));

        assertF("     " +
                "     " +
                "♦    " +
                "☺    " +
                "☻    ", game.getPlayers().get(1));

        assertF("     " +
                "     " +
                "☺    " +
                "♥    " +
                "☻    ", game.getPlayers().get(2));

        verifyNoMoreInteractions(listener);
    }

    private void givenDice(int... values) {
        dice = mock(Dice.class);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void assertF(String expected, Player player) {
        assertEquals(TestUtils.injectN(expected), printerFactory.getPrinter(
                game.reader(), player).print());
    }

}
