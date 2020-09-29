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

import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.EventsListenersAssert.assertAll;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractSingleTest {

    public static final int SIZE = 5;
    protected final Walls walls = new WallsImpl();
    protected List<Hero> heroes = new LinkedList<>();
    protected List<Game> games = new LinkedList<>();
    private List<EventListener> listeners = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    protected GameSettings settings = mock(GameSettings.class);
    protected Level level;
    protected Bomberman field;
    protected int bombsCount = 1;
    protected int bombsPower = 1;
    protected Parameter<Integer> playersPerRoom = v(Integer.MAX_VALUE);
    protected Dice heroDice = mock(Dice.class);
    protected Dice chopperDice = mock(Dice.class);
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    protected EventsListenersAssert events = new EventsListenersAssert(listeners);

    public void setup() {
        PerksSettingsWrapper.reset();

        givenWalls();

        level = mock(Level.class);
        when(level.bombsCount()).thenAnswer(inv -> bombsCount);
        when(level.bombsPower()).thenAnswer(inv -> bombsPower);

        when(settings.getHero(any(Level.class))).thenAnswer(inv -> {
            Hero hero = new Hero(level, heroDice);
            heroes.add(hero);
            return hero;
        });

        when(settings.getLevel()).thenReturn(level);
        when(settings.isBigBadaboom()).thenReturn(new SimpleParameter<>(false));
        when(settings.getDice()).thenReturn(heroDice);
        when(settings.getBoardSize()).thenReturn(v(SIZE));
        when(settings.getWalls()).thenReturn(walls);
        when(settings.getRoundSettings()).thenReturn(getRoundSettings());
        when(settings.getPlayersPerRoom()).thenAnswer(inv -> playersPerRoom);
        when(settings.killOtherHeroScore()).thenReturn(v(200));
        when(settings.killMeatChopperScore()).thenReturn(v(100));
        when(settings.killWallScore()).thenReturn(v(10));
        when(settings.catchPerkScore()).thenReturn(v(5));

        field = new Bomberman(settings);
    }

    public void givenBoard(int count) {
        for (int i = 0; i < count; i++) {
            listeners.add(mock(EventListener.class));
            players.add(new Player(listener(i), getRoundSettings().roundsEnabled()));
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
        MeatChopper chopper = new MeatChopper(pt(x, y), field, chopperDice);
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

    protected abstract RoundSettingsWrapper getRoundSettings();

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
