package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.bomberman.TestGameSettings;
import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.bomberman.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.EventsListenersAssert.assertAll;
import static com.codenjoy.dojo.bomberman.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractMultiplayerTest {

    public static final int SIZE = 5;
    protected final Walls walls = new WallsImpl();
    protected List<Hero> heroes = new LinkedList<>();
    protected List<Game> games = new LinkedList<>();
    private List<EventListener> listeners = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    protected GameSettings settings = settings();
    protected Bomberman field;
    protected Dice dice = mock(Dice.class);
    private PrinterFactory printerFactory = new PrinterFactoryImpl();
    protected PerksSettingsWrapper perks;

    protected EventsListenersAssert events = new EventsListenersAssert(listeners);

    public void setup() {
        perks = settings.perksSettings();
        givenWalls();

        when(settings.getHero(any(Level.class))).thenAnswer(inv -> {
            Level level = settings.getLevel();
            Hero hero = new Hero(level);
            heroes.add(hero);
            return hero;
        });

        when(settings.getWalls(dice)).thenReturn(walls);

        field = new Bomberman(dice, settings);
    }

    protected GameSettings settings() {
        return spy(new TestGameSettings())
                .integer(BOARD_SIZE, SIZE)
                .integer(BOMB_POWER, 1);
    }

    public void givenBoard(int count) {
        for (int i = 0; i < count; i++) {
            listeners.add(mock(EventListener.class));
            players.add(new Player(listener(i), settings));
            games.add(new Single(player(i), printerFactory));
        }

        games.forEach(g -> {
            g.on(field);
            g.newGame();
        });
    }

    protected DestroyWall destroyWallAt(int x, int y) {
        DestroyWall wall = new DestroyWall(x, y);
        walls.add(wall);
        return wall;
    }

    protected MeatChopper meatChopperAt(int x, int y) {
        MeatChopper chopper = new MeatChopper(pt(x, y), field, dice);
        chopper.stop();
        walls.add(chopper);
        return chopper;
    }

    protected void asrtBrd(String board, Game game) {
        assertEquals(board, game.getBoardAsString());
    }

    protected Hero hero(int index) {
        return heroes.get(index);
    }

    protected Game game(int index) {
        return games.get(index);
    }

    protected Player player(int index) {
        return players.get(index);
    }

    protected EventListener listener(int index) {
        return listeners.get(index);
    }

    protected void tick() {
        field.tick();
    }

    protected void newGameForAllDied() {
        players.forEach(player -> {
            if (!player.isAlive()) {
                field.newGame(player(players.indexOf(player)));
            }
        });
        resetHeroes();
    }

    protected void dice(Dice dice, int... values) {
        reset(dice);
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void givenWalls(Wall... input) {
        Arrays.asList(input).forEach(walls::add);
    }

    protected void resetHeroes() {
        heroes.clear();
        players.forEach(player -> heroes.add(player.getHero()));
    }

    protected void assertBoards(String expected, Integer... indexes) {
        assertAll(expected, games.size(), indexes, index -> {
            Object actual = game(index).getBoardAsString();
            return String.format("game(%s)\n%s\n", index, actual);
        });
    }

    protected void resetListeners() {
        listeners.forEach(Mockito::reset);
    }
}
