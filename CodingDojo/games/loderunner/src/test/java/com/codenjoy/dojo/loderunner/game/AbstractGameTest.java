package com.codenjoy.dojo.loderunner.game;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.games.loderunner.Element;
import com.codenjoy.dojo.loderunner.TestSettings;
import com.codenjoy.dojo.loderunner.model.Hero;
import com.codenjoy.dojo.loderunner.model.Loderunner;
import com.codenjoy.dojo.loderunner.model.Player;
import com.codenjoy.dojo.loderunner.model.items.Brick;
import com.codenjoy.dojo.loderunner.model.items.robber.RobberJoystick;
import com.codenjoy.dojo.loderunner.model.levels.Level;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractGameTest {

    protected List<EventListener> listeners = new LinkedList<>();
    protected List<Player> players = new LinkedList<>();
    protected List<Game> games = new LinkedList<>();

    protected Dice dice = mock(Dice.class);
    protected PrinterFactory<Element, Player> printer = new PrinterFactoryImpl<>();
    protected Loderunner field;
    protected GameSettings settings = settings();
    protected EventsListenersAssert events = new EventsListenersAssert(() -> listeners, Events.class);

    protected List<RobberJoystick> robbers = new LinkedList<>();

    @Before
    public void setup() {
        Brick.DRILL_TIMER = 13;
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    protected void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    protected void givenFl(String map) {
        settings.string(LEVEL_MAP, map);

        Level level = settings.level();
        settings.integer(GOLD_COUNT_YELLOW, level.yellowGold().size())
                .integer(GOLD_COUNT_GREEN, level.greenGold().size())
                .integer(GOLD_COUNT_RED, level.redGold().size())
                .integer(SHADOW_PILLS_COUNT, level.pills().size())
                .integer(BACKWAYS_COUNT, level.backways().size())
                .integer(ROBBERS_COUNT, level.robbers().size());

        field = new Loderunner(dice, settings);

        for (Hero hero : level.heroes()) {
            Player player = givenPlayer(hero.getX(), hero.getY());
            player.getHero().setDirection(hero.getDirection());
        }
        reloadAllRobbers();

        dice(0); // всегда дальше выбираем нулевой индекс
    }

    protected Player givenPlayer(int x, int y) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);
        Player player = new Player(listener, settings);
        players.add(player);
        Single game = new Single(player, printer);
        games.add(game);
        dice(x, y);
        game.on(field);
        game.newGame();
        return player;
    }

    protected GameSettings settings() {
        return spy(new TestSettings());
    }

    protected void tick() {
        field.tick();
    }

    protected void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(field.reader(), player()).print());
    }

    public void assertF(String expected) {
        assertF(expected, 0);
    }

    /**
     * Проверяет одну борду с заданным индексом
     *
     * @param expected ожидаемое значение
     * @param index    индекс
     */
    public void assertF(String expected, int index) {
        assertEquals(expected, game(index).getBoardAsString());
    }

    protected void assertScores(int score1, int score2) {
        assertEquals(score1, hero(0).scores());
        assertEquals(score2, hero(1).scores());
    }

    protected Game game() {
        return games.get(0);
    }

    protected Game game(int index) {
        return games.get(index);
    }

    protected EventListener listener() {
        return listeners.get(0);
    }

    protected EventListener listener(int index) {
        return listeners.get(index);
    }

    protected Hero hero() {
        return hero(0);
    }

    protected Hero hero(int index) {
        return (Hero) game(index).getPlayer().getHero();
    }

    protected Player player() {
        return player(0);
    }

    protected Player player(int index) {
        return players.get(index);
    }

    protected RobberJoystick robber() {
        return robbers.get(0);
    }

    protected RobberJoystick robber(int index) {
        return robbers.get(index);
    }

    protected void reloadAllRobbers() {
        robbers = field.robbers().stream()
                .map(RobberJoystick::new)
                .collect(Collectors.toList());
    }
}
