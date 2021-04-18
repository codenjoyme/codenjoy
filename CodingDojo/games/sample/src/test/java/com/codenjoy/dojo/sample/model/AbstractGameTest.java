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
import com.codenjoy.dojo.sample.services.Events;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.LEVEL_MAP;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractGameTest {

    private List<EventListener> listeners;
    private List<Game> games;
    private List<Player> players;
    private Dice dice;
    protected Sample field;
    protected GameSettings settings;
    private PrinterFactory printer;
    protected EventsListenersAssert events;

    @Before
    public void setup() {
        listeners = new LinkedList<>();
        players = new LinkedList<>();
        games = new LinkedList<>();

        dice = mock(Dice.class);
        settings = new TestGameSettings();
        printer = new PrinterFactoryImpl();
        events = new EventsListenersAssert(() -> listeners, Events.class);
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    public void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    public void givenFl(String map) {
        settings.string(LEVEL_MAP, map);

        Level level = settings.level();

        field = new Sample(level, dice, settings);
        level.heroes().forEach(hero -> givenPlayer(hero));
    }

    public Player givenPlayer(Point pt) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);
        Player player = new Player(listener, settings);
        players.add(player);
        Game game = new Single(player, printer);
        games.add(game);
        dice(pt.getX(), pt.getY());
        game.on(field);
        game.newGame();
        return player;
    }

    public void tick() {
        field.tick();
    }

    public void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    // getters & asserts

    public void assertF(String expected, int index) {
        assertEquals(expected, game(index).getBoardAsString());
    }

    public Game game(int index) {
        return games.get(index);
    }

    public Player player(int index) {
        return players.get(index);
    }

    public Hero hero(int index) {
        return (Hero) game(index).getPlayer().getHero();
    }

    // getters, if only one player

    public void assertF(String expected) {
        assertF(expected, 0);
    }

    public Game game() {
        return game(0);
    }

    public Player player() {
        return player(0);
    }

    public Hero hero() {
        return hero(0);
    }
}