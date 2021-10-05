package com.codenjoy.dojo.sample.model.experimental;

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


import com.codenjoy.dojo.games.sample.Element;
import com.codenjoy.dojo.sample.TestGameSettings;
import com.codenjoy.dojo.sample.model.Hero;
import com.codenjoy.dojo.sample.model.Level;
import com.codenjoy.dojo.sample.model.Player;
import com.codenjoy.dojo.sample.model.Sample;
import com.codenjoy.dojo.sample.services.Events;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.*;
import org.junit.rules.TestName;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractGameTest {

    private List<EventListener> listeners;
    private List<Game> games;
    private List<Player> players;

    private Dice dice;
    private PrinterFactory<Element, Player> printer;
    protected Sample field;
    protected GameSettings settings;
    protected EventsListenersAssert events;

    @Rule
    public TestName name = new TestName();
    private List<String> messages;

    @Before
    public void setup() {
        messages = new LinkedList<>();
        log("setup");

        listeners = new LinkedList<>();
        players = new LinkedList<>();
        games = new LinkedList<>();

        dice = mock(Dice.class);
        settings = settings();
        printer = new PrinterFactoryImpl<>();
        events = new EventsListenersAssert(() -> listeners, Events.class);
    }

    @After
    public void after() {
        events.verifyNoEvents();
        TestUtils.assertSmokeFile("GameTest/" + name.getMethodName() +  ".txt", messages);
    }

    public void dice(int... ints) {
        log("dice", ints);

        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void log(String method, Object... parameters) {
        messages.add(String.format("%s(%s)\n",
                method,
                Arrays.deepToString(parameters)
                        .replaceAll("^\\[", "")
                        .replaceAll("\\]$", "")));
    }

    public void givenFl(String... maps) {
        log("givenFl", maps);

        int levelNumber = LevelProgress.levelsStartsFrom1;
        settings.setLevelMaps(levelNumber, maps);
        Level level = settings.level(levelNumber, dice);

        field = new Sample(dice, level, settings);
        level.heroes().forEach(this::givenPlayer);

        // other field preparation stuff
    }

    private void givenPlayer(Hero hero) {
        log("givenPlayer", hero);

        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        Player player = new Player(listener, settings);
        players.add(player);

        Game game = new Single(player, printer);
        games.add(game);

        dice(hero.getX(), hero.getY());
        game.on(field);
        game.newGame();
    }

    protected GameSettings settings() {
        return new TestGameSettings();
    }

    public void tick() {
        log("tick");

        field.tick();
    }

    // getters & asserts

    public void assertF(String expected, int index) {
        log("assertF", expected, index);

        assertEquals(expected, game(index).getBoardAsString());
    }

    public Game game(int index) {
        log("game", index);

        return games.get(index);
    }

    public Player player(int index) {
        log("player", index);

        return players.get(index);
    }

    public Hero hero(int index) {
        log("hero", index);

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