package com.codenjoy.dojo.sample.model;

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


import com.codenjoy.dojo.sample.model.level.Level;
import com.codenjoy.dojo.sample.model.level.LevelImpl;
import com.codenjoy.dojo.sample.services.Events;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MultiplayerTest {

    private List<EventListener> listeners = new LinkedList<>();
    private List<Game> games = new LinkedList<>();
    private Dice dice;
    private Sample field;
    private GameSettings settings;
    private PrinterFactory printerFactory;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new GameSettings();
    }

    public void givenFl(String map) {
        Level level = new LevelImpl(map);
        field = new Sample(level, dice, settings);
    }

    public void givenThreePlayers() {
        givenPlayer(1, 4);
        givenPlayer(2, 2);
        givenPlayer(3, 4);
    }

    private Game game(int index) {
        return games.get(index);
    }

    public Player givenPlayer(int x, int y) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);
        Player player = new Player(listener, settings);
        Game game = new Single(player, printerFactory);
        games.add(game);
        dice(x, y);
        game.on(field);
        game.newGame();
        return player;
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    private void asrtFl1(String expected) {
        assertEquals(expected, game(0).getBoardAsString());
    }

    private void asrtFl2(String expected) {
        assertEquals(expected, game(1).getBoardAsString());
    }

    private void asrtFl3(String expected) {
        assertEquals(expected, game(2).getBoardAsString());
    }

    // рисуем несколько игроков
    @Test
    public void shouldPrint() {
        // given
        givenFl("☼☼☼☼☼☼" +
                "☼   $☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenThreePlayers();

        // when then
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        asrtFl2(
                "☼☼☼☼☼☼\n" +
                "☼☻ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        asrtFl3(
                "☼☼☼☼☼☼\n" +
                "☼☻ ☺$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // Каждый игрок может упраыляться за тик игры независимо
    @Test
    public void shouldJoystick() {
        // given
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenThreePlayers();

        // when
        game(0).getJoystick().act();
        game(0).getJoystick().down();
        game(1).getJoystick().right();
        game(2).getJoystick().down();

        field.tick();

        // then
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼x   ☼\n" +
                "☼☺ ☻ ☼\n" +
                "☼  ☻ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        // given
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenThreePlayers();

        // when
        game(2).close();

        field.tick();

        // then
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺   ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // игрок может взорваться на бомбе
    @Test
    public void shouldKill() {
        // given
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenThreePlayers();

        game(0).getJoystick().down();
        game(0).getJoystick().act();
        game(2).getJoystick().left();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼x☻  ☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        game(2).getJoystick().left();
        field.tick();

        // then
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼X   ☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener(2)).event(Events.LOOSE);
        assertTrue(game(2).isGameOver());

        dice(4, 1);
        game(2).newGame();

        field.tick();

        asrtFl1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼   ☻☼\n" +
                "☼☼☼☼☼☼\n");
    }

    private EventListener listener(int index) {
        return listeners.get(index);
    }

    // игрок может подобрать золото
    @Test
    public void shouldGetGold() {
        // given
        givenFl("☼☼☼☼☼☼" +
                "☼   $☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenThreePlayers();

        // when
        game(2).getJoystick().right();

        dice(1, 2);

        field.tick();

        // then
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼☺  ☻☼\n" +
                "☼    ☼\n" +
                "☼$☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener(2)).event(Events.WIN);
    }

    // игрок не может пойи на другого игрока
    @Test
    public void shouldCantGoOnHero() {
        // given
        givenFl("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        givenThreePlayers();

        // when
        game(0).getJoystick().right();
        game(2).getJoystick().left();

        field.tick();

        // then
        asrtFl1("☼☼☼☼☼☼\n" +
                "☼ ☺☻ ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }
}
