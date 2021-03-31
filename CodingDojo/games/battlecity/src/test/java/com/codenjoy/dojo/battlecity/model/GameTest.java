package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.TestGameSettings;
import com.codenjoy.dojo.battlecity.model.items.AITank;
import com.codenjoy.dojo.battlecity.model.items.Bullet;
import com.codenjoy.dojo.battlecity.model.items.Wall;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.battlecity.services.GameRunner;
import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.battlecity.TestGameSettings.*;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    protected Dice dice;
    private Battlecity game;
    private List<Player> players = new LinkedList<>();
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    private GameSettings settings;

    private List<Tank> heroes = new LinkedList<>();
    private List<EventListener> listeners = new LinkedList<>();
    private EventsListenersAssert events = new EventsListenersAssert(listeners);

    private Dice dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
        return dice;
    }
    
    @Before
    public void setup() {
        settings = new TestGameSettings();
        dice = mock(Dice.class);
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    private Tank hero(int index) {
        return heroes.get(index);
    }

    private void givenFl(String board) {
        settings.string(LEVEL_MAP, board);

        GameRunner runner = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public GameSettings getSettings() {
                return settings;
            }
        };
        game = (Battlecity) runner.createGame(0, settings);

        Level level = settings.level(dice);
        level.getTanks().forEach(tank ->
            game.newGame(initPlayer(game, tank)));

        heroes = game.tanks();
    }

    private Player initPlayer(Battlecity game, Tank tank) {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        Player player = new Player(listener, settings){
            @Override
            public void newHero(Field field) {
                hero = tank;
                hero.setPlayer(this);
                hero.setActive(true);
                hero.setAlive(true);
            }
        };

        players.add(player);

        tank.init(game);
        return player;
    }

    private String getPrizesCount() {
        List<Tank> tanks = game.allTanks();
        long prizes = tanks.stream().filter(Tank::withPrize).count();

        return String.format("%s prizes with %s tanks", prizes, tanks.size());
    }

    @Test
    public void shouldDrawField() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void assertD(String field) {
        assertEquals(field, getPrinter().print());
    }

    private Printer<String> getPrinter() {
        return printerFactory.getPrinter(
                game.reader(), players.get(0));
    }

    // рисуем стенку
    @Test
    public void shouldBeWall_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, game.walls().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // рисуем мой танк
    @Test
    public void shouldBeTankOnFieldWhenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertNotNull(game.allTanks());
    }

    @Test
    public void shouldTankCanGoIfIceAtWayWithoutSliding_whenTankTakePrize() {
        settings.integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 6)
                .integer(SLIPPERINESS, 1);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ?    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    Ѡ    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_NO_SLIDING);
        game.tick();
        events.verifyNoEvents();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_NO_SLIDING]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[5]]\n");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заезжаем на лед
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        //  DOWN -> UP но так как игрок взял приз скольжение не происходит, по этому DOWN -> DOWN
        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanGoIfAceAtWay_whenPrizeWorkingEnd() {
        settings.integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2)
                .integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ?    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    Ѡ    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_NO_SLIDING);
        game.tick();
        events.verifyNoEvents();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_NO_SLIDING]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[5]]\n");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заезжаем на лед
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // так как игрок взял приз скольжение не происходит, по этому UP -> UP
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // так как игрок взял приз скольжение не происходит, по этому UP -> UP
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // действие приза окончилось
        assertPrize(hero(0), "[]");

        // мы снова на льду, начинаем занос запоминаем команду
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP занос
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #►   ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanGoIfAceAtWay_whenTankTackPrizeSlidingEnd() {
        settings.integer(PRIZE_ON_FIELD, 4)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 6)
                .integer(SLIPPERINESS, 5);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ¿    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        ai(0).dontShoot = true;
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ¿    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");


        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ¿    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    Ѡ    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_NO_SLIDING);
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    !    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP выполняется занос
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    5    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        // RIGHT -> UP выполняется занос
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_NO_SLIDING]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[5]]\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #►   ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankMove() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼◄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankStayAtPreviousPositionWhenIsNearBorder() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(0).up();
        hero(1).down();
        hero(1).down();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▲☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˅    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(0).right();
        hero(1).left();
        hero(1).left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ►☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˂    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletHasSameDirectionAsTank() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertEquals(hero(0).getBullets().iterator().next().getDirection(),
                hero(0).getDirection());
    }

    @Test
    public void shouldBulletGoInertiaWhenTankChangeDirection() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  •  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ► ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallUp() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallRight() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallLeft() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•   ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDisappear_whenHittingTheWallDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - снизу
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - слева
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - справа
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - сверху
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - снизу но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_overWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - слева но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_overWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╠☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  ╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •╞☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    // снарядом уничтожается стенка за три присеста - справа но через стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_overWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡  ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╡• ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ◄ ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // снарядом уничтожается стенка за три присеста - сверху но сквозь стену
    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_overWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╬    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╦    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼╥    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallRight_whenTwoWalls() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╬╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╠╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►  ╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► •╞╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼► • ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼►   ╠☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallLeft_whenTwoWalls() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╬• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╣• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡  ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬╡• ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ • ◄☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╣   ◄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWall_whenHittingTheWallDown_whenTwoWalls() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╬☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╦☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼    ╥☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼    •☼\n" +
                "☼     ☼\n" +
                "☼    ╬☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ╦☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если я иду а спереди стена, то я не могу двигаться дальше
    @Test
    public void shouldDoNotMove_whenWallTaWay_goDownOrLeft() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬►╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▲╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬◄╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼ ╬▼╬ ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        removeAllNear();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣►╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▲╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣◄╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╩  ☼\n" +
                "☼ ╣▼╠ ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        removeAllNear();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ╨  ☼\n" +
                "☼ ╡▼╞ ☼\n" +
                "☼  ╥  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void removeAllNear() {
        hero(0).up();
        game.tick();
        hero(0).act();
        game.tick();

        hero(0).left();
        game.tick();
        hero(0).act();
        game.tick();

        hero(0).right();
        game.tick();
        hero(0).act();
        game.tick();

        hero(0).down();
        game.tick();
        hero(0).act();
        game.tick();
    }

    // если я стреляю дважды, то выпускается два снаряда
    // при этом я проверяю, что они уничтожаются в порядке очереди
    @Test
    public void shouldShotWithSeveralBullets_whenHittingTheWallDown() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╬☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╦☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼      ╥☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╬☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╦☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      ╥☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼      •☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ▼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // стоит проверить, как будут себя вести полуразрушенные конструкции, если их растреливать со всех других сторон
    @Test
    public void shouldDestroyFromUpAndDownTwice() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▼  ☼\n" +
                "☼  ╦  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        hero(0).down();
        game.tick();

        hero(0).down();
        game.tick();

        hero(0).left();
        game.tick();

        hero(0).up();
        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ─  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // стоять, если спереди другой танк
    @Test
    public void shouldStopWhenBeforeOtherTank() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // геймовер, если убили не бот-танк
    @Test
    public void shouldDieWhenOtherTankKillMe() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˅    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(true, hero(1).isAlive());
        assertEquals(true, hero(2).isAlive());

        hero(0).act();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n" +
                "listener(2) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(true, hero(2).isAlive());

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[2]]\n" +
                "listener(1) => []\n" +
                "listener(2) => [KILL_YOUR_TANK]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(false, hero(2).isAlive());

        game.tick();

        assertEquals(true, hero(0).isAlive());
        assertEquals(false, hero(1).isAlive());
        assertEquals(false, hero(2).isAlive());
    }

    // стоять, если меня убили
    @Test
    public void shouldStopWhenKill() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n");
    }

    @Test
    public void shouldNoConcurrentException() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1], KILL_YOUR_TANK]\n" +
                "listener(1) => [KILL_YOUR_TANK, KILL_OTHER_HERO_TANK[1]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDestroyBullet2() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldRemoveAIWhenKillIt() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");


        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n" +
                "listener(2) => []\n");

        hero(0).right();
        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ► •˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        game.tick();

        assertW("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►  Ѡ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[2]]\n" +
                "listener(1) => []\n" +
                "listener(2) => [KILL_YOUR_TANK]\n");

        game.tick();

        assertW("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();
    }

    private void assertW(String expected) {
        Printer<String> printer = getPrinter();
        assertEquals(expected, printer.print().replaceAll("[«¿»?•]", " "));
    }

    @Test
    public void shouldRegenerateDestroyedWall() {
        shouldBulletDestroyWall_whenHittingTheWallUp_whenTwoWalls();

        hero(0).act();
        game.tick();
        hero(0).act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 7; i <= Wall.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCantGoIfWallAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletCantGoIfWallAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(0).act();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►  •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼    ☼\n" +
                "☼ ►   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyOneBulletPerTick() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(0).act();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(0).act();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanFireIfAtWayEnemyBullet() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanGoIfDestroyWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        hero(0).act();
        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldWallCantRegenerateOnTank() {
        shouldTankCanGoIfDestroyWall();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= Wall.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        for (int i = 2; i <= Wall.REGENERATE_TIME; i++) {
            assertD("☼☼☼☼☼☼☼\n" +
                    "☼     ☼\n" +
                    "☼     ☼\n" +
                    "☼╬    ☼\n" +
                    "☼ ►   ☼\n" +
                    "☼     ☼\n" +
                    "☼☼☼☼☼☼☼\n");

            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼╬►   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    @Test
    public void shouldWallCantRegenerateOnBullet() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        hero(0).act();
        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        for (int i = 3; i <= Wall.REGENERATE_TIME; i++) {
            game.tick();
        }

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNTicksPerBullet() {
        settings.integer(TANK_TICKS_PER_SHOOT, 4);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        String field =
                "☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n";
        assertD(field);

        for (int i = 1; i < settings.integer(TANK_TICKS_PER_SHOOT); i++) {
            hero(0).act();
            game.tick();

            assertD(field);
        }

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldNewAIWhenKillOther() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼   ▼ ☼\n" +
                "☼    ?☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        events.verifyAllEvents("listener(0) => [KILL_OTHER_AI_TANK]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼   ▼ ☼\n" +
                "☼   Ѡ ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(5, 5);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼¿    ☼\n" +
                "☼•  ▼ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfNoBarrier() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOnlyRotateIfBarrier() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ►╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillTankOnWall() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼╬    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        game.tick();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼╨    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        hero(1).up();  // команда поигнорится потому что вначале ходят все танки, а потом летят все снаряды
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet2() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDieWhenMoveOnBullet3() {
        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼˄      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼▼      ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

    }

    // если стенка недорушенная, снаряд летит, и ресетнули игру, то все конструкции восстанавливаются
    @Test
    public void shouldRemoveBulletsAndResetWalls_whenReset() {
        settings.integer(TANK_TICKS_PER_SHOOT, 3);
        
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act(); // не выйдет
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╨        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // допустим за игру он прибил 5 танков
        Player player = players.iterator().next();
        player.setKilled(5);

        // when
        game.clearScore();

        // смогу стрельнуть, пушка ресетнется
        hero(0).act();
        game.tick();

        // then
        // но после рисета это поле чистится
        assertEquals(0, player.score());

        // и стенки тоже ресетнулись
        // и снаряд полетел
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╬        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╬        ☼\n" +
                "☼╩        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // первый выстрел иногда получается сделать дважды
    @Test
    public void shouldCantFireTwice() {
        settings.integer(TANK_TICKS_PER_SHOOT, 4);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.clearScore();

        game.tick(); // внутри там тикает так же gun, но первого выстрела еще небыло
        game.tick();

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // дерево
    @Test
    public void shouldBeWallTree_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, game.trees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBeWallTwoTree_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲   %☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(2, game.trees().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  %  ☼\n" +
                "☼     ☼\n" +
                "☼▲   %☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // При выстреле пуля должна пролетать сквозь дерево
    @Test
    public void shouldBulletFlyUnderTree_right() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼►    %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(0).act();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼►    %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►•  %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►  •%   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   %•  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   %  •☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ►   %   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletDestroyWallUnderTree_whenHittingTheWallUp_whenTwoWalls() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╩    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╨    ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private List<Wall> walls(Wall... walls) {
        return new LinkedList<>(Arrays.asList(walls));
    }

    // Когда пуля и дерево находятся в одной координате
    // и я не вижу свой танк под деревом, то и пулю все равно не вижу
    @Test
    public void shouldBulletFlyUnderTwoTree_up_caseShowMyTankUnderTree() {
        // given
        settings.bool(SHOW_MY_TANK_UNDER_TREE, true);

        // when then
        shouldBulletFlyUnderTwoTree_up();
    }

    // Когда пуля и дерево находятся в одной координате
    // я не вижу ее, дерево скрывает
    @Test
    public void shouldBulletFlyUnderTwoTree_up() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    •    ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼    •    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    %    ☼\n" +
                "☼    %    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // дерево - когда игрок заходит под него, там видно дерево и больше никакого движения
    @Test
    public void shouldTankMove_underTree_case2() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼% ►  ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // дерево - когда игрок заходит под него,
    // и в настройках сказано, что свой танк виден под деревом
    // я вижу свой танк
    @Test
    public void shouldTankMove_underTree_caseShowMyTankUnderTree_case2() {
        settings.bool(SHOW_MY_TANK_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼% ►  ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly_shouldTankMoveUnderTree() {
        settings.bool(SHOW_MY_TANK_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        •☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        •☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼       ◄%☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ◄  %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldBulletFlyUnderTree_jointly() {

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        ▲☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        •☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼        ▲☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        •☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼       ◄%☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼     ◄  %☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼        %☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // так же не видно меня под деревом
    @Test
    public void shouldTankMove_underTree() {
		givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼▼        ☼\n" +
				"☼         ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼▼        ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		hero(0).down();
		game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

		hero(0).down();
		game.tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");

		hero(0).down();
		game.tick();

		assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼%        ☼\n" +
				"☼%        ☼\n" +
				"☼▼        ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼         ☼\n" +
				"☼˄        ☼\n" +
				"☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // но если в сеттингах сказано что меня видно под деревьями, я - вижу
    @Test
    public void shouldTankMove_underTree_caseShowMyTankUnderTree() {
        settings.bool(SHOW_MY_TANK_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼▼        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▼        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // другого игрока не видно под деревом
    @Test
    public void shouldOtherTankMove_underTree() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼˄        ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // даже если в сеттингах сказано что меня видно под деревьями,
    // другого танка я не вижу все равно
    @Test
    public void shouldOtherTankMove_underTree_caseShowMyTankUnderTree() {
        settings.bool(SHOW_MY_TANK_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼˄        ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // под деревом не видно так же и ботов белых
    @Test
    public void shouldAITankMove_underTree() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼?        ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼◘        ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼¿        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼◘        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼▲        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEnemyCanKillTankUnderTree() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼˄        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();// герой запрятался в кустах

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼•        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals(true, hero(1).isAlive());
        game.tick();// герой должен погибнуть

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n");

        assertEquals(false, hero(1).isAlive());
        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼▼        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼%        ☼\n" +
                "☼%        ☼\n" +
                "☼Ѡ        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // два танка не могут проехать друг через друга под деревьями
    // но мой танк видно - так сказано в настройках
    @Test
    public void shouldTwoTankCanPassThroughEachOtherUnderTree_caseShowMyTankUnderTree() {
        settings.bool(SHOW_MY_TANK_UNDER_TREE, true);
        
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        game.tick();

        hero(0).down();
        game.tick();

        hero(1).up();
        // Два танка не могут проехать через друг друга
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%˃   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // два танка не могут проехать друг через друга под деревьями
    // но мой танк видно - так сказано в настройках
    @Test
    public void shouldTwoTankCanPassThroughEachOtherUnderTree() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▼    ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(1).up();
        game.tick();

        hero(0).down();
        game.tick();

        hero(1).up();
        // Два танка не могут проехать через друг друга
        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        hero(1).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%►   ☼\n" +
                "☼%˃   ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// Лёд
    @Test
    public void shouldBeWallIce_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  #  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, game.ice().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  #  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда герой двигается по льду, происходит скольжение
    // (он проскальзывает одну команду).
    // Если только заезжаем - то сразу же начинается занос,
    // то есть запоминается команда которой заезжали на лед
    // Если съезжаем на землю, то любой занос прекращается тут же
    @Test
    public void shouldTankMoveUp_onIce_afterBeforeGround() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заежаем на лёд
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // находимся на льду
        // выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        // двигается дальше с предедущей командой up()
        // RIGHT -> UP (скольжение)
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // двигаемся дальше в направлении up()
        // UP -> UP (выполнилась)
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // выполнили команаду right(), но танк не реагирует, так как происходит скольжение
        // двигается дальше с предедущей командой up()
        // RIGHT -> UP (скольжение)
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // выехали со льда
        // двигается дальше в направлении up()
        // UP -> UP (выполнилась)
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankMoveLeftThenUpThenDown_onIce() {
        settings.integer(SLIPPERINESS, 1);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // заежаем на лёд
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> UP (скольжение)
        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> DOWN (выполнилась)
        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // DOWN -> DOWN (скольжение)
        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // также когда на нем двигается враг он проскальзывает команду на два тика
    @Test
    public void shouldOtherTankMoveLeftThenUpThenDown_onIce() {
        settings.integer(SLIPPERINESS, 1);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // враг заежает на лёд
        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // LEFT -> DOWN(скольжение)
        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▼    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP (выполнилась)
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP (скольжение)
        // сьезд со льда
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼˄        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // Река
    @Test
    public void shouldBeWallWater_whenGameCreated() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(1, game.rivers().size());

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	// река - через нее герою нельзя пройти. но можно стрелять
	@Test
	public void shouldTankCanGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

	@Test
	public void shouldBulletCanGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero(0).up();
		game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero(0).act();
		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼•    ☼\n" +
				"☼~    ☼\n" +
				"☼▲    ☼\n" +
				"☼☼☼☼☼☼☼\n");

		hero(0).right();
		hero(0).act();
		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼•    ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼~    ☼\n" +
				"☼ ►•  ☼\n" +
				"☼☼☼☼☼☼☼\n");

		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼     ☼\n" +
				"☼~    ☼\n" +
				"☼ ►  •☼\n" +
				"☼☼☼☼☼☼☼\n");
	}

    @Test
    public void shouldDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~►~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◄~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

		hero(0).down();
		game.tick();

		assertD("☼☼☼☼☼☼☼\n" +
				"☼     ☼\n" +
				"☼  ~  ☼\n" +
				"☼ ~▼~ ☼\n" +
				"☼  ~  ☼\n" +
				"☼     ☼\n" +
				"☼☼☼☼☼☼☼\n");
    }

    // река - через нее врагу нельзя пройти. но можно стрелять
    @Test
    public void shouldOtherTankBullet_canGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼˄    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).right();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ ˃•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ ˃  •☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTankDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~►~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▲~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◄~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~▼~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ˄☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // река - через нее боту нельзя пройти. но можно стрелять
    @Test
    public void shouldAITankBullet_canGoIfRiverAtWay() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        ai(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).right();
        ai(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼ »•  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼  ◘ •☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~ ▲  ☼\n" +
                "☼   » ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private AITank ai(int index) {
        return (AITank) game.aiTanks().get(index);
    }

    @Test
    public void shouldAITankDoNotMove_whenRiverToWay_goRightOrUpOrLeftOrDown() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~?~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~◘~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~?~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼•~«~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ~  ☼\n" +
                "☼ ~¿~ ☼\n" +
                "☼  ~  ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // создаем АИтанк с призами
    @Test
    public void shouldCreatedAiPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ?☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 2 tanks", getPrizesCount());
    }

    // У АИтанка с призами после 4-го хода должен смениться Element
    @Test
    public void shouldSwapElementAfterFourTicks() {
        settings.integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ?☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼    •« ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼     ? ☼\n" +
                "☼  •    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼      ◘☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼      ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 2 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3, а спаунится сразу 2 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenTwoAi() {
        settings.integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘    ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("1 prizes with 3 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3 и спаунится сразу 3 АИтанка, то 2-й должен быть АИтанком с призами
    @Test
    public void shouldSpawnAiPrizeWhenThreeAi() {
        settings.integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘  ¿ ¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("1 prizes with 4 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3, а спаунятся сразу 6 АИтанков, то должно быть 2 АИтанка с призами
    @Test
    public void shouldSpawnTwoAiPrizeWhenSixAi() {
        settings.integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿¿◘¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("2 prizes with 7 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 3, а 3 АИтанка спаунятся по 1-му за каждый ход,
    // то АИтанк с призами спаунится после 2-го хода
    // так же проверяем что призовой танк меняет свой символ каждые 4 тика
    @Test
    public void shouldSpawnAiPrize_whenAddOneByOneAI() {
        settings.integer(SPAWN_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(2, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(5, 7));

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿     ☼\n" +
                "☼ •     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(6, 7));
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿  •  ☼\n" +
                "☼       ☼\n" +
                "☼ •     ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 4 tanks", getPrizesCount());

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿• ☼\n" +
                "☼ ◘     ☼\n" +
                "☼    •  ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼ ¿   • ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼     ¿ ☼\n" +
                "☼    ¿  ☼\n" +
                "☼▲¿     ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    // в AI-танк с призами надо попасть 3 раза, чтобы убить
    @Test
    public void shouldKillAiPrizeInThreeHits() {
        settings.integer(KILL_HITS_AI_PRIZE, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
        ai(0).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        events.verifyAllEvents("listener(0) => [KILL_OTHER_AI_TANK]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldMyBulletsRemovesWhenKillMe() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˃   ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼˃   ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO_TANK[1]]\n" +
                "listener(1) => [KILL_YOUR_TANK]\n" +
                "listener(2) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ • ˃☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertEquals(false, hero(1).isAlive());
        game.tick();

        events.verifyNoEvents();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▼    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˃☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_onlyInPointKilledAiPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_inPointKilledAiPrize_underTree() {
        settings.integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldDropPrize_InPointKilledAiPrize_onIce() {
        settings.integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз должен експайриться и исчезнуть через 2 тика, если его не подобрали
    @Test
    public void shouldExpirePrizeOnField_disappearTwoTicks() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз должен експайриться и исчезнуть через 3 тика, если его не подобрали
    @Test
    public void shouldExpirePrizeOnField_disappearThreeTicks() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз должен експайриться и исчезнуть через 4 тика, если его не подобрали
    @Test
    public void shouldExpirePrizeOnField_disappearFourTicks() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 4);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз над деревом должен исчезнуть через 2 тика, если его не подобрали
    // после исчезновения приза видим дерево
    @Test
    public void shouldExpirePrizeOnField_disappearOnTree() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼%    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // приз на льду должен исчезнуть через 2 тика, если его не подобрали
    // после исчезновения приза видим лед
    @Test
    public void shouldExpirePrizeOnField_disappearOnIce() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼!    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTookPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    Ѡ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // герой берет приз под деревом - когда его видно под деревьями 
    @Test
    public void shouldHeroTookPrize_underTree_caseShowMyTankUnderTree() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3)
                .bool(SHOW_MY_TANK_UNDER_TREE, true);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // герой берет приз под деревом - когда его не видно под деревьями 
    @Test
    public void shouldHeroTookPrize_underTree() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1%   ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼%%   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize_underTree() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    %☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼    %☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTookPrize_onIce() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");


        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");
        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldOtherTookPrize_onIce() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    #☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ¿☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    1☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼    ˄☼\n" +
                "☼    #☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldAiDontTookPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;
        ai(1).up();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼?    ☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если я подстрелил танк, а в следующий тик в эту ячейку въезжаю сам,
    // то приз считается подобраным и не отбражается на филде
    @Test
    public void shouldHeroTookPrize_inPointKillAi() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        dice(DICE_IMMORTALITY);
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

    }

    private void assertPrize(Tank hero, String expected) {
        assertEquals(expected, hero.prizes().toString());
    }

    // если в момент подбора приза прилетает снаряд, то умирает танк, а приз остается
    @Test
    public void shouldKillHero_whenHeroTookPrizeAndComesBullet() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 6);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿   ˂☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ   ˂☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(0).up();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1 • ˂☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        events.verifyAllEvents(
                "listener(0) => [KILL_YOUR_TANK]\n" +
                "listener(1) => [KILL_OTHER_HERO_TANK[1]]\n");

        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1 ˂  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼!˂   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼˂    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [CATCH_PRIZE[1]]\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼˄    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(1), "[PRIZE_IMMORTALITY]");
    }

    @Test
    public void shouldHeroKillPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    // если я подстрелил приз, а в следующий тик в эту ячейку въезжаю сам,
    // то приз не подбирается
    @Test
    public void shouldHeroKillPrize_dontTakeNextTick() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
        events.verifyAllEvents("listener(0) => []\n");
    }

    @Test
    public void shouldKilAITankWithPrize_whenHitKillsIs2() {
        settings.integer(KILL_HITS_AI_PRIZE, 2)
                .integer(PRIZE_ON_FIELD, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        ai(0).down();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_AI_TANK]\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();
    }

    @Test
    public void shouldHeroTakePrize_breakingWalls() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 10);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬¿  ╬☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬Ѡ  ╬☼\n" +
                "☼     ☼\n" +
                "☼ ▲╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬2  ╬☼\n" +
                "☼ ▲   ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬▲  ╬☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[2]]\n");

        hero(0).right();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼╬ ►•╬☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼  ▲  ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).down();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼╬ ▼  ☼\n" +
                "☼  •  ☼\n" +
                "☼  ╬  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).left();
        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼ ◄   ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeEnemyWithoutPrize_breakingWalls() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[2]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroNotBreakingBorder_breakingWalls() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        dice(DICE_BREAKING_WALLS);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[2]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorking_breakingWalls() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        dice(DICE_BREAKING_WALLS);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[2]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬    ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╩    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeEnemyShootsHero_immortality() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");


        dice(DICE_IMMORTALITY);
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1   ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).left();
        game.tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[1]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();
    }

    @Test
    public void shouldEndPrizeWorkingEnemyShootsHero_immortality() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");


        dice(DICE_IMMORTALITY);
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼1   ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).left();
        game.tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[1]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        events.verifyNoEvents();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        assertPrize(hero(0), "[]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲• ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ  ˂ ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_YOUR_TANK]\n" +
                "listener(1) => [KILL_OTHER_HERO_TANK[1]]\n");
    }

    @Test
    public void shouldHeroTakePrizeAiShootsHero_immortality() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼«    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        ai(0).down();
        game.tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        ai(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

    }

    @Test
    public void shouldEndPrizeWorkingAiShootsHero_immortality() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼1    ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼«    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼!    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        ai(0).down();
        game.tick();

        assertPrize(hero(0), "[PRIZE_IMMORTALITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[1]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        ai(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        ai(0).up();
        ai(0).act();
        game.tick();

        assertPrize(hero(0), "[]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼?    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_YOUR_TANK]\n");
    }

    // если герой заехал на лед, а в следующий тик не указал никакой команды,
    // то продолжается движение вперед по старой команде на 1 тик.
    @Test
    public void shouldTankSlidingOneTicks() {
        settings.integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // если герой заехал на лед, а в следующий тик указал какую-то команду,
    // то продолжается движение по старой команде N тиков.
    @Test
    public void shouldTankSlidingNTicks() {
        settings.integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #    ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    ▲    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼    #►   ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼    #    ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // если герой заехал на лед, то продолжается движение по старой команде N тиков
    // слушается команда N + 1 и опять занос N тиков
    @Test
    public void shouldTankSlidingNTicks_andAgainSliding() {
        settings.integer(SLIPPERINESS, 3);

        givenFl("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼▲        ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼▲        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼▲        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#######  ☼\n" +
                "☼▲        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼▲######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼#►#####  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> RIGHT
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼##►####  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> RIGHT
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼###►###  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        // UP -> UP
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼   ▲     ☼\n" +
                "☼#######  ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼#        ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    // если герой в ходе заноса уперся в стену, то занос прекращается
    @Test
    public void shouldTankAndSliding_ifBumpedWall() {
        settings.integer(SLIPPERINESS, 5);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼#    ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼###  ☼\n" +
                "☼▲    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> UP
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲##  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> UP -> wall -> Canceled sliding
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲##  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        // RIGHT -> RIGHT
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#►#  ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldTankCanGoIfRiverAtWay_whenTankTakePrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼?    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼3    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[3]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼~    ☼\n" +
                "☼  ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrize_walkOnWater() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼¿    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        hero(0).up();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼3    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[3]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼~~~  ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼▲~~  ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼▲    ☼\n" +
                "☼~~~  ☼\n" +
                "☼  ˄  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда заканчивается действие приза движение по воде отключается
    @Test
    public void shouldTankCanGoIfRiverAtWay_whenPrizeIsOver() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼?    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼3    ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[3]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼▲    ☼\n" +
                "☼~ ╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если во время окончание приза танк оказался на воде, он получает штраф.
    // N тиков он не может ходить по клеточкам но может менять направление движение и стрелять,
    // на N+1 тик он может сместится на позицию указанной команды и продолжать движение.
    // За исключением - если после смещения он оказался снова на воде, то процедура повторяется до тех пор,
    // пока танк не выйдет полностью из воды.
    @Test
    public void shouldTankCanGoIfRiverAtWay_whenPrizeIsOver_butTankOnWater() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PENALTY_WALKING_ON_WATER, 4)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  ?  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  Ѡ  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_WALKING_ON_WATER);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  3  ☼\n" +
                "☼  ▲  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        assertPrize(hero(0), "[]");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼  ▲  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[3]]\n");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[PRIZE_WALKING_ON_WATER]");

        // действие приза закончилось
        // герой получает штраф 4 тика
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▲~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~▼~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~◄~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~►~~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф 4 тика закончился. Возможно перемещение
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~►~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф еще 4 тика, так как герой снова на воде
        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~◄~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~▲~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~▼~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~►~☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф 4 тика закончился. Возможно перемещение
        hero(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~►☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // штраф еще 4 тика, так как герой снова на воде
        hero(0).down();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▼☼\n" +
                "☼~~~~~☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // мы все так же на воде, а потому не можем двигаться 4 тика
        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~▲☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        hero(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~◄☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // но можем выехать на сушу, хоть штраф не закончился
        hero(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼    ▼☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");

        // обратно заехать уже не можем как ни старайся
        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        hero(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼~~~~~☼\n" +
                "☼    ▲☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    @Test
    public void shouldHeroTakePrizeAndShootsEveryTick_breakingWalls() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(PRIZE_WORKING, 3)
                .integer(TANK_TICKS_PER_SHOOT, 3);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼¿    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼Ѡ    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_BREAKING_WALLS);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼2    ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_BREAKING_WALLS]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[2]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼╬╬╬  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertPrize(hero(0), "[]");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╩  ☼\n" +
                "☼• •  ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).act();
        hero(1).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼ ╬╨  ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼▲ ˄  ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // когда бот упирается в водоем -> он останавливается на 5 тиков и отстреливается
    // после этого меняет направление и уезжает
    @Test
    public void shouldAiMoveAfterFiveTicks() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼◘    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(1);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼•    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼•    ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼?    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼~    ☼\n" +
                "☼~   ▲☼\n" +
                "☼ »   ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если дропнуть танк на лед, то случался NPE
    // теперь все нормально
    @Test
    public void shouldDropAiOnIce() {
        settings.integer(SLIPPERINESS, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(1, 5));
        ai(0).dontShoot = true;

        assertD("☼☼☼☼☼☼☼\n" +
                "☼◘    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼¿    ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).right();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼#    ☼\n" +
                "☼#    ☼\n" +
                "☼#»   ☼\n" +
                "☼#    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // мы не можем дропнуть танк на воду
    @Test
    public void shouldCantDropAiInRiver() {
        givenFl("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(2);
        game.getAiGenerator().drop(pt(1, 5));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~◘   ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼~    ☼\n" +
                "☼~¿   ☼\n" +
                "☼~    ☼\n" +
                "☼~    ☼\n" +
                "☼    ▲☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    // если spawnAiPrize = 2, то должно быть 3 АИтанка с призами
    // если aiPrizeLimit = 2, то будет на поле 2 АИтанка с призами
    @Test
    public void shouldSpawnTwoAiPrize() {
        settings.integer(SPAWN_AI_PRIZE, 2)
                .integer(AI_PRIZE_LIMIT, 2);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿¿¿¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼ ◘¿◘¿¿¿☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertEquals("2 prizes with 7 tanks", getPrizesCount());
    }

    // если spawnAiPrize = 2, то каждый второй АИтанк будет с призами
    // если на поле уже лежит приз и есть один АИтанк с призом, то при aiPrizeLimit = 2,
    // АИтанков с призами больше появляться не будет
    @Test
    public void shouldNotSpawnAiPrize_ifPrizeOnField() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(SPAWN_AI_PRIZE, 2)
                .integer(AI_PRIZE_LIMIT, 2);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼◘¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_AI_TANK]\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ¿¿¿¿¿ ☼\n" +
                "☼ ••••• ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼1      ☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼ ••••• ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(4, 7));
        game.getAiGenerator().drop(pt(5, 7));
        game.getAiGenerator().drop(pt(6, 7));
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼   ¿¿¿ ☼\n" +
                "☼!      ☼\n" +
                "☼       ☼\n" +
                "☼ ¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("1 prizes with 9 tanks", getPrizesCount());
    }

    @Test
    public void shouldSpawnAiPrize_ifKillPrize() {
        settings.integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_ON_FIELD, 5)
                .integer(SPAWN_AI_PRIZE, 2)
                .integer(AI_PRIZE_LIMIT, 2);

        givenFl("☼☼☼☼☼☼☼☼☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        hero(0).act();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼◘¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        ai(0).dontShoot = true;
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼¿¿¿¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼•      ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_AI_TANK]\n");

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ¿¿¿¿¿ ☼\n" +
                "☼ ••••• ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        dice(DICE_IMMORTALITY);

        hero(0).act();
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼1      ☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼•      ☼\n" +
                "☼ ••••• ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼Ѡ      ☼\n" +
                "☼       ☼\n" +
                "☼ ¿◘¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼ ¿¿¿¿¿ ☼\n" +
                "☼▲      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        game.getAiGenerator().drop(pt(4, 7));
        game.getAiGenerator().drop(pt(5, 7));
        game.getAiGenerator().drop(pt(6, 7));
        game.tick();

        assertD("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼   ¿¿¿ ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼▲¿¿¿¿¿ ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertEquals("2 prizes with 9 tanks", getPrizesCount());
    }

    @Test
    public void shouldHeroTakePrizeAndSeeAiUnderTree_visibility() {
        settings.integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %%¿☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %%«☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        ai(0).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[4]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %? ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldHeroTakePrizeAndSeeEnemyUnderTree_visibility() {
        settings.integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %%˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[4]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeAiUnderTree_visibility() {
        settings.integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %%¿☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));
        ai(1).left();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %%«☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        ai(0).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        ai(0).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        events.verifyAllEvents("listener(0) => [CATCH_PRIZE[4]]\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %? ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  «% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  ¿% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        assertPrize(hero(0), "[]");
    }

    @Test
    public void shouldEndPrizeWorkingDontSeeEnemyUnderTree_visibility() {
        settings.integer(PRIZE_ON_FIELD, 5)
                .integer(KILL_HITS_AI_PRIZE, 1)
                .integer(PRIZE_WORKING, 2);

        givenFl("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼¿ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        ai(0).kill(mock(Bullet.class));

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼Ѡ %% ☼\n" +
                "☼     ☼\n" +
                "☼▲   ˄☼\n" +
                "☼☼☼☼☼☼☼\n");

        dice(DICE_VISIBILITY);
        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼    ˄☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼! %%˄☼\n" +
                "☼     ☼\n" +
                "☼▲    ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(0).up();
        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼4 %% ☼\n" +
                "☼▲    ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        events.verifyNoEvents();

        hero(0).up();
        hero(1).up();
        game.tick();

        assertPrize(hero(0), "[PRIZE_VISIBILITY]");
        events.verifyAllEvents(
                "listener(0) => [CATCH_PRIZE[4]]\n" +
                "listener(1) => []\n");

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %˄ ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).up();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %˄ ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).left();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  ˂% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");

        hero(1).down();
        game.tick();

        assertD("☼☼☼☼☼☼☼\n" +
                "☼  %% ☼\n" +
                "☼  %% ☼\n" +
                "☼▲ %% ☼\n" +
                "☼     ☼\n" +
                "☼     ☼\n" +
                "☼☼☼☼☼☼☼\n");
    }
}
