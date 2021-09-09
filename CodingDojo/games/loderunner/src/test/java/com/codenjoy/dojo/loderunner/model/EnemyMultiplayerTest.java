package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.TestSettings;
import com.codenjoy.dojo.loderunner.model.levels.LevelImpl;
import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.loderunner.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.loderunner.model.GameTest.getLevel;
import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.ENEMIES_COUNT;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnemyMultiplayerTest {

    private Dice dice;
    private List<EventListener> listeners = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private Game game;
    private Loderunner field;
    private PrinterFactory printerFactory;
    private GameSettings settings;
    private EventsListenersAssert events;

    @Before
    public void setUp() {
        dice = mock(Dice.class);
        printerFactory = new PrinterFactoryImpl();
        settings = new TestSettings();
        events = new EventsListenersAssert(() -> listeners, Events.class);
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void atBoard(String expected) {
        assertEquals(expected, game.getBoardAsString());
    }

    private void setupPlayer(int x, int y) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);
        Player player = new Player(listener, settings);
        players.add(player);
        game = new Single(player, printerFactory);
        game.on(field);
        dice(x, y);
        game.newGame();
    }

    private void setupGm(String board) {
        LevelImpl level = getLevel(board, settings);
        field = new Loderunner(level, dice, settings);

        for (Hero hero : level.getHeroes()) {
            setupPlayer(hero.getX(), hero.getY());
        }
    }

    // чертик идет за тобой
    @Test
    public void shouldEnemyGoToHero() {
        settings.integer(ENEMIES_COUNT, 1);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼     »☼" +
                "☼H#####☼" +
                "☼H     ☼" +
                "☼###H  ☼" +
                "☼►  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    « ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼«     ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼Q     ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H  »  ☼\n" +
                "☼###H  ☼\n" +
                "☼►  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼► «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H     ☼\n" +
                "☼###H  ☼\n" +
                "☼Ѡ  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        events.verifyAllEvents("[KILL_HERO]");
        assertEquals(true, game.isGameOver());

        dice(1, 4);
        game.newGame();

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H#####☼\n" +
                "☼H  ►  ☼\n" +
                "☼###H  ☼\n" +
                "☼ » H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    // чертик стоит на месте, если ко мне нет пути
    @Test
    public void shouldEnemyStop_whenNoPathToHero() {
        settings.integer(ENEMIES_COUNT, 1);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼     ►☼" +
                "☼     #☼" +
                "☼      ☼" +
                "☼###H  ☼" +
                "☼»  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼     ►☼\n" +
                "☼     #☼\n" +
                "☼      ☼\n" +
                "☼###H  ☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼     ►☼\n" +
                "☼     #☼\n" +
                "☼      ☼\n" +
                "☼###H  ☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    // чертик идет за тобой по более короткому маршруту
    @Test
    public void shouldEnemyGoToHeroShortestWay() {
        settings.integer(ENEMIES_COUNT, 1);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼     »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldEnemyGoToHeroShortestWay2() {
        settings.integer(ENEMIES_COUNT, 1);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼»     ☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼Q####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    // другой чертик чертику не помеха
    @Test
    public void shouldEnemyGoToHeroShortestWayGetRoundOther() {
        settings.integer(ENEMIES_COUNT, 2);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼»    »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼Q####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldEnemyGoToHeroShortestWayGetRoundOther2() {
        settings.integer(ENEMIES_COUNT, 2);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼» »   ☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼ «    ☼\n" +
                "☼Q####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    // если чертику не достать одного он бежит за другим а не зависает
    @Test
    public void shouldEnemyGoToNewHeroIfOneIsHidden() {
        settings.integer(ENEMIES_COUNT, 1);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼   ►  ☼" +
                "☼######☼" +
                "☼      ☼" +
                "☼###H##☼" +
                "☼»  H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();
        field.tick();
        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   ►  ☼\n" +
                "☼######☼\n" +
                "☼      ☼\n" +
                "☼###H##☼\n" +
                "☼»  H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        setupPlayer(1, 4);
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   (  ☼\n" +
                "☼######☼\n" +
                "☼►     ☼\n" +
                "☼###H##☼\n" +
                "☼ » H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();
        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   (  ☼\n" +
                "☼######☼\n" +
                "☼Ѡ     ☼\n" +
                "☼###H##☼\n" +
                "☼   H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [KILL_HERO]\n");
    }

    // каждый чертик бежит за своим героем, даже если к нему занятый уже герой ближе
    @Test
    public void shouldEveryEnemyRunsAfterHisHero_evenIfThereIsAnotherHeroNearbyWhoIsAlreadyBeingHunted() {
        settings.integer(ENEMIES_COUNT, 2);
        setupGm("☼☼☼☼☼☼☼☼" +
                "☼»  ► »☼" +
                "☼H####H☼" +
                "☼H    H☼" +
                "☼###H##☼" +
                "☼  ►H  ☼" +
                "☼######☼" +
                "☼☼☼☼☼☼☼☼");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼ » (  ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼  »(  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_HERO]\n" +
                "listener(1) => []\n");

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H   «H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z» ☼\n" +
                "☼H####H☼\n" +
                "☼H  « H☼\n" +
                "☼###H##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z »☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###Q##☼\n" +
                "☼  ►H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  ►Q  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [KILL_HERO]\n");

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ѠH  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // больше не за кем охотитья - охотники стоят на месте
        field.tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n");

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ѠH  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼   Z  ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  ѠH  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // даже если на поле никого нет, чертики стоят на месте
        removePlayer(1);
        removePlayer(0);

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼  «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // но стоит двоим ребятам появиться на поле
        // как вдруг охотники начнут охотиться каждый за своим
        setupPlayer(1, 2);
        setupPlayer(5, 6);

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    ► ☼\n" +
                "☼H####H☼\n" +
                "☼H    Q☼\n" +
                "☼###H##☼\n" +
                "☼( «H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    ► ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼(« H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // если один вдруг пропадет, то его охотник переключится
        removePlayer(0);

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    ► ☼\n" +
                "☼H####Q☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼ « H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    ►»☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  »H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // и после того как нагонят оставшегося, снова зависнут
        field.tick();

        events.verifyAllEvents("[KILL_HERO]");

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    Ѡ ☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  »H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        field.tick();

        events.verifyAllEvents("[]");

        atBoard("☼☼☼☼☼☼☼☼\n" +
                "☼    Ѡ ☼\n" +
                "☼H####H☼\n" +
                "☼H    H☼\n" +
                "☼###H##☼\n" +
                "☼  »H  ☼\n" +
                "☼######☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    private void removePlayer(int index) {
        field.remove(players.get(index));
        players.remove(index);
        listeners.remove(index);
    }
}
