package com.codenjoy.dojo.snakebattle.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.snakebattle.TestGameSettings;
import com.codenjoy.dojo.snakebattle.model.board.SnakeBoard;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.services.Event;
import com.codenjoy.dojo.snakebattle.services.GameSettings;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MultiplayerTest {
    private SnakeBoard game;
    private Dice dice;

    private Hero hero;
    private EventListener heroEvents;
    private Player heroPlayer;

    private Hero enemy;
    private EventListener enemyEvents;
    private Player enemyPlayer;

    private PrinterFactory printer;
    private EventsListenersAssert events;

    private GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        settings = new TestGameSettings()
                .integer(ROUNDS_MIN_TICKS_FOR_WIN, 1);

        printer = new PrinterFactoryImpl();
        events = new EventsListenersAssert(() -> Arrays.asList(heroEvents, enemyEvents), Event.class);
    }

    @After
    public void after() {
        verifyAllEvents("");
    }

    public void verifyAllEvents(String expected) {
        assertEquals(expected, events.getEvents());
    }

    private void dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    private void givenFl(String board) {
        Level level = new Level(board.replaceAll("\n", ""));

        game = new SnakeBoard(level, dice,  settings);

        hero = level.hero(game);
        hero.setActive(true);
        heroEvents = mock(EventListener.class);
        heroPlayer = new Player(heroEvents, settings);
        heroPlayer.setHero(hero);
        game.newGame(heroPlayer);

        enemy = level.enemy(game);
        enemy.setActive(true);
        enemyEvents = mock(EventListener.class);
        enemyPlayer = new Player(enemyEvents, settings);
        enemyPlayer.setHero(enemy);
        game.newGame(enemyPlayer);
    }

    // проверяем что соперник отображается на карте
    @Test
    public void shouldHeroWithEnemyOnField() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // спящие змеи
    @Test
    public void shouldSleepingSnake_whenSetNotActive() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.setActive(false);
        enemy.setActive(false);

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~&  ☼" +
                "☼     ☼" +
                "☼ *ø  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");


        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ *ø  ☼" +
                "☼     ☼" +
                "☼ ~&  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ~&  ☼" +
                "☼     ☼" +
                "☼ *ø  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // если режим игры не rounds а multiple то змеи активны сразу
    @Test
    public void shouldNotSleepingSnake_whenGameIsMultiplayer() {
        // given
        settings.bool(ROUNDS_ENABLED, false);

        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        // then
        hero.setActive(true);
        enemy.setActive(true);

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        // when
        game.tick();

        // then
        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼  ×> ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что обе змейки умирают, когда врезаются в равного соперника
    // и получаем оповещение о смерти
    @Test
    public void shouldDie_whenHeadCrashToOtherSnake_bothDie() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        enemy.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ☻  ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  æ  ☼" +
                "☼  ☺  ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что меньшая змейка умирает, когда врезаются голова к голове
    // большая змейка уменьшается на размер маленькой
    // змейка перестаёт уменьшаться на следующий ход
    @Test
    public void shouldDie_whenHeadCrashToOtherSnake_heroDie() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼ ×──>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼ ×──>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ×>☼" +
                "☼     ☼" +
                "☼ ╘══►☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        enemy.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[2], WIN]\n");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ╓☼" +
                "☼    ☻☼" +
                "☼  ×─┘☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    æ☼" +
                "☼    ☺☼" +
                "☼  ╘═╝☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ˄☼" +
                "☼    ¤☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ▲☼" +
                "☼    ╙☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼    ˄☼" +
                "☼    ¤☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что змейка умирает, когда врезается в тело другой змейки
    @Test
    public void shouldDie_whenBodyCrashToOtherSnake_enemyDie() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼×─>  ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘☺☻ ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×☻☺ ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldDie_whenBodyCrashToOtherSnake_heroDie() {
        // такой же тест, но врезается игрок в противника
        // (последовательность героев в списке может оказывать значение на результат)
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼×─>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼×─>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼╘═►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼ ×☻☺ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  æ  ☼" +
                "☼ ╘☺☻ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что змейка умирает, когда врезается в хвост другой змейки
    @Test
    public void shouldDie_whenTailCrashToOtherSnake_enemyDie() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ╘═►  ☼" +
                "☼×─>   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ╘═►  ☼" +
                "☼×─>   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ×─>  ☼" +
                "☼╘═►   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[3], WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺═► ☼" +
                "☼ ×┘   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☻─> ☼" +
                "☼ ╘╝   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   ╘═►☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   ×─>☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    // а если лобовое столкновение
    @Test
    public void shouldDie_whenTailCrashToOtherSnake_bothDie() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼×─>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼×─>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼×─>  ☼" +
                "☼╘═►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        enemy.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘☺☻ ☼" +
                "☼ ×┘  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×☻☺ ☼" +
                "☼ ╘╝  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // такой же тест, но врезается игрок в противника
    @Test
    public void shouldDie_whenTailCrashToOtherSnake_heroDie() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼ ×─>  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼ ×─>  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼ ╘═►  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ╘╗   ☼" +
                "☼  ☻─> ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ×┐   ☼" +
                "☼  ☺═► ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[3], WIN]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   ×─>☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   ╘═►☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // а тут лобовое столкновение
    @Test
    public void shouldDie_whenTailCrashToOtherSnake_bothDie2() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼×─>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘═►  ☼" +
                "☼×─>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼×─>  ☼" +
                "☼╘═►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘╗  ☼" +
                "☼ ×☻☺ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×┐  ☼" +
                "☼ ╘☺☻ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // TODO продолжить дальше улучшать

    // в полёте змейки не вредят друг-другу (летишь сам)
    @Test
    public void flyOverEnemy() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►©  ☼" +
                "☼     ☼" +
                "☼×>   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [FLYING]\n");

        hero.down();
        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ♠  ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ˄  ☼" +
                "☼  ╓  ☼" +
                "☼  ♠  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼  ˄  ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ♠  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // в полёте змейки не вредят друг-другу (летит враг)
    @Test
    public void flyOverHero() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼×>©  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(1) => [FLYING]\n");

        hero.down();
        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ♦  ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ♦  ☼" +
                "☼  ¤  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼  ♦  ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // в случае ярости, змея может съесть другую
    @Test
    public void eatEnemy() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►® ☼" +
                "☼     ☼" +
                "☼ ×>○○☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();
        game.tick();

        verifyAllEvents(
                "listener(0) => [FURY]\n" +
                "listener(1) => [APPLE, APPLE]\n");

        hero.down();
        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ╓☼" +
                "☼    ☺☼" +
                "☼  ×─┘☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[4], WIN]\n" +
                "listener(1) => [DIE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ╓☼" +
                "☼    ♥☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    // в случае ярости, можно откусить кусок змейки соперника
    @Test
    public void eatEnemyTail() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►○○ ☼" +
                "☼     ☼" +
                "☼×>®  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();
        verifyAllEvents(
                "listener(0) => [APPLE]\n" +
                "listener(1) => [FURY]\n");

        enemy.up();
        game.tick();
        verifyAllEvents(
                "listener(0) => [APPLE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ♣╘►☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        // такой же тест, но врезается игрок в противника
        // (последовательность героев в списке может оказывать значение на результат)
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►®  ☼" +
                "☼     ☼" +
                "☼×>○  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [FURY]\n" +
                "listener(1) => [APPLE]\n");

        hero.down();
        game.tick();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ♥×>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[1]]\n");
    }

    private void assertH(String expected) {
        assertBoard(expected, heroPlayer);
    }

    private void assertBoard(String expected, Player player) {
        assertEquals(TestUtils.injectN(expected.replaceAll("\n", "")),
                printer.getPrinter(game.reader(), player).print());
    }

    private void assertE(String expected) {
        assertBoard(expected, enemyPlayer);
    }

    // если твоя змейка осталась на поле сама, а все противники погибли - тебе WIN
    @Test
    public void shouldWin_whenOneOnBoardAfterEnemyDie() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼  ×> ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼   ×>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼    ×☺" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ×> ☼" +
                "☼     ☼" +
                "☼    ╘☻" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyNoMoreInteractions(enemyEvents);
        verifyNoMoreInteractions(heroEvents);

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ×>☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

    }

    // если твоя змейка осталась на поле сама после того
    // как противник удалился (не через die) - тебе WIN
    @Test
    public void shouldWin_whenOneOnBoardAfterEnemyLeaveRoom() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼×>   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.remove(heroPlayer);
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [WIN]\n");


        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ×> ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        verifyNoMoreInteractions(enemyEvents);
        verifyNoMoreInteractions(heroEvents);

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ×>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // змейка не стартует сразу если стоит таймер
    @Test
    public void shouldWaitTillTimer_thenStart() {
        settings.integer(ROUNDS_TIME_BEFORE_START, 4);

        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼×>   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        // ждем 4 тика
        game.tick();

        verifyAllEvents(
                "listener(0) => [[...3...]]\n" +
                "listener(1) => [[...3...]]\n");

        game.tick();

        verifyAllEvents(
                "listener(0) => [[..2..]]\n" +
                "listener(1) => [[..2..]]\n");

        game.tick();

        verifyAllEvents(
                "listener(0) => [[.1.]]\n" +
                "listener(1) => [[.1.]]\n");

        game.tick();

        verifyAllEvents(
                "listener(0) => [START, [Round 1]]\n" +
                "listener(1) => [START, [Round 1]]\n");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼×>   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╘► ☼" +
                "☼     ☼" +
                "☼  ×> ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
                "☼     ☼" +
                "☼   ×>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // если одна змейка погибает, стартует новый раунд
    @Test
    public void shouldStartNewGame_whenGameOver() {
        settings.integer(ROUNDS_TIME_BEFORE_START, 1)
                .integer(ROUNDS_PER_MATCH, 3);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼#   ╘►☼" +
                "☼#×>   ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        // ждем перехода на первый уровнь
        game.tick();

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        verifyAllEvents(
                "listener(0) => [START, [Round 1]]\n" +
                "listener(1) => [START, [Round 1]]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼#   ╘►☼" +
                "☼#×>   ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼#    ╘☻" +
                "☼# ×>  ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [WIN]\n");
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        assertEquals(false, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        game.newGame(heroPlayer); // это делает автоматом фреймворк потому что heroPlayer.!isAlive()

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(false, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        game.tick();

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(false, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼☼     ☼\n" +
                "☼☼     ☼\n" +
                "~&     ☼\n" +
                "☼#  ×> ☼\n" +
                "☼☼     ☼\n" +
                "☼☼     ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // последний победный тик героя!
        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼☼     ☼\n" +
                "☼☼     ☼\n" +
                "~&     ☼\n" +
                "*ø     ☼\n" +
                "☼☼     ☼\n" +
                "☼☼     ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        // ждем перехода на второй уровень
        game.tick();

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        verifyAllEvents(
                "listener(0) => [START, [Round 2]]\n" +
                "listener(1) => [START, [Round 2]]\n");

        assertEquals(true, heroPlayer.isActive());
        assertEquals(true, enemyPlayer.isActive());

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "╘►     ☼" +
                "×>     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼#    ╘☻" +
                "☼#    ×☺" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        assertEquals(false, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(false, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        game.newGame(heroPlayer);  // это делает автоматом фреймворк потому что heroPlayer.!isAlive()
        game.newGame(enemyPlayer); // это делает автоматом фреймворк потому что enemyPlayer.!isAlive()

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(false, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(false, enemyPlayer.isActive());

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "~&     ☼" +
                "*ø     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        // последний тик победителя тут неуместен, все погибли
        // game.tick();

        // ждем перехода на третий уровень
        game.tick();

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        verifyAllEvents(
                "listener(0) => [START, [Round 3]]\n" +
                "listener(1) => [START, [Round 3]]\n");

        assertEquals(true, heroPlayer.isActive());
        assertEquals(true, enemyPlayer.isActive());

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "╘►     ☼" +
                "×>     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼#   ╘►☼" +
                "☼#   ×>☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        assertEquals(false, heroPlayer.shouldLeave());
        assertEquals(false, enemyPlayer.shouldLeave());

        game.tick();

        assertEquals(true, heroPlayer.shouldLeave());
        assertEquals(true, enemyPlayer.shouldLeave());

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼#    ╘☻" +
                "☼#    ×☺" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        assertEquals(false, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(false, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        game.remove(heroPlayer);  // это делает автоматом фреймворк потому что heroPlayer.shouldLeave()
        game.remove(enemyPlayer); // это делает автоматом фреймворк потому что enemyPlayer.shouldLeave()

    }

    // Лобовое столкновение змеек - одна под fury
    @Test
    public void shouldDie_whenHeadAttack() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼    ® ☼" +
                "☼    ˄ ☼" +
                "☼    ¤ ☼" +
                "☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼    ® ☼" +
                "☼    ˄ ☼" +
                "☼    ¤ ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼    ® ☼" +
                "☼    ▲ ☼" +
                "☼    ╙ ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        verifyAllEvents("listener(1) => [FURY]\n");

        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[3], WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╘═☻ ☼" +
                "☼    ¤ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ×─☺ ☼" +
                "☼    ╙ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼    ♣ ☼" +
                "☼    ¤ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼    ♥ ☼" +
                "☼    ╙ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    // Змейка с одной только головой не живет
    // идея в том, чтоб под таблеткой ярости откусить конец хвоста, оставив только голову
    @Test
    public void shouldDie_whenEatAllTailOtherSnake_with1Length() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼   ®  ☼" +
                "☼   ˄  ☼" +
                "☼   ¤  ☼" +
                "☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼   ®  ☼" +
                "☼   ˄  ☼" +
                "☼   ¤  ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼   ®  ☼" +
                "☼   ▲  ☼" +
                "☼   ╙  ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        verifyAllEvents(
                "listener(1) => [FURY]\n");

        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[3], WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╘♣☻ ☼" +
                "☼   ¤  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ×♥☺ ☼" +
                "☼   ╙  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ♣  ☼" +
                "☼   ¤  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼   ♥  ☼" +
                "☼   ╙  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    @Test
    public void shouldCutTail_whenFury() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼  ®   ☼" +
                "☼  ˄   ☼" +
                "☼  ¤   ☼" +
                "☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼  ®   ☼" +
                "☼  ˄   ☼" +
                "☼  ¤   ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼  ®   ☼" +
                "☼  ▲   ☼" +
                "☼  ╙   ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(1) => [FURY]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ╘═►  ☼" +
                "☼  ♣   ☼" +
                "☼  ¤   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ×─>  ☼" +
                "☼  ♥   ☼" +
                "☼  ╙   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");


        game.tick();

        verifyAllEvents(
                "listener(1) => [EAT[1]]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ♣╘► ☼" +
                "☼  ¤   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ♥×> ☼" +
                "☼  ╙   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  ♣   ☼" +
                "☼  ¤ ╘►☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  ♥   ☼" +
                "☼  ╙ ×>☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    @Test
    public void shouldCutLongTail_whenFury() {
        givenFl("☼☼☼☼☼☼☼☼☼☼" +
                "☼╔══════╗☼" +
                "☼║╔════╕║☼" +
                "☼║╚═════╝☼" +
                "☼╚═══►   ☼" +
                "☼    ®   ☼" +
                "☼    ˄   ☼" +
                "☼    ¤   ☼" +
                "☼        ☼" +
                "☼☼☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼☼☼" +
                "☼╔══════╗☼" +
                "☼║╔════╕║☼" +
                "☼║╚═════╝☼" +
                "☼╚═══►   ☼" +
                "☼    ®   ☼" +
                "☼    ˄   ☼" +
                "☼    ¤   ☼" +
                "☼        ☼" +
                "☼☼☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼┌──────┐☼\n" +
                "☼│┌────ö│☼\n" +
                "☼│└─────┘☼\n" +
                "☼└───>   ☼\n" +
                "☼    ®   ☼\n" +
                "☼    ▲   ☼\n" +
                "☼    ╙   ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        verifyAllEvents(
                "listener(1) => [FURY]\n");

        assertH("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼╔══════╗☼\n" +
                "☼║╔═══╕ ║☼\n" +
                "☼║╚═════╝☼\n" +
                "☼╚════►  ☼\n" +
                "☼    ♣   ☼\n" +
                "☼    ¤   ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼┌──────┐☼\n" +
                "☼│┌───ö │☼\n" +
                "☼│└─────┘☼\n" +
                "☼└────>  ☼\n" +
                "☼    ♥   ☼\n" +
                "☼    ╙   ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");


        game.tick();

        verifyAllEvents(
                "listener(1) => [EAT[27]]\n");

        assertH("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼    ♣╘► ☼\n" +
                "☼    ¤   ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼    ♥×> ☼\n" +
                "☼    ╙   ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼    ♣   ☼\n" +
                "☼    ¤ ╘►☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼    ♥   ☼\n" +
                "☼    ╙ ×>☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents("");
    }

    // с помощью этой команды можно самоуничтожиться
    // при этом на месте старой змейки появится много яблок :)
    @Test
    public void shouldDieAndLeaveApples_whenAct0() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═══╕ ☼" +
                "☼║     ☼" +
                "☼╚═══╗ ☼" +
                "☼  ©◄╝ ☼" +
                "☼      ☼" +
                "☼×>    ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();
        verifyAllEvents(
                "listener(0) => [FLYING]\n");

        hero.up();
        game.tick();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔╕    ☼" +
                "☼║ ♠   ☼" +
                "☼╚═║═╗ ☼" +
                "☼  ╚═╝ ☼" +
                "☼      ☼" +
                "☼   ×> ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌ö    ☼" +
                "☼│ ♦   ☼" +
                "☼└─│─┐ ☼" +
                "☼  └─┘ ☼" +
                "☼      ☼" +
                "☼   ╘► ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.act(0);
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼○○    ☼" +
                "☼○ ○   ☼" +
                "☼○○○○○ ☼" +
                "☼  ○○○ ☼" +
                "☼      ☼" +
                "☼    ×>☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼○○    ☼" +
                "☼○ ○   ☼" +
                "☼○○○○○ ☼" +
                "☼  ○○○ ☼" +
                "☼      ☼" +
                "☼    ╘►☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    // Кейз когда змейки сталкиваются головами
    @Test
    public void shouldCase6() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼◄══╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        hero.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[4], WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻>    ☼" +
                "☼╚═╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺►    ☼" +
                "☼└─ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    @Test
    public void shouldCase6_2() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼<──ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        enemy.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[4], WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺►    ☼" +
                "☼└─ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻>    ☼" +
                "☼╚═╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    @Test
    public void shouldCase6_3() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼◄════╕☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        hero.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[4], WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼└☺    ☼" +
                "☼╚═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼╚☻    ☼" +
                "☼└───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼▲     ☼" +
                "☼╙     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼˄     ☼" +
                "☼¤     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    @Test
    public void shouldCase7() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼◄══╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[4], WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻     ☼" +
                "☼╚═╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺     ☼" +
                "☼└─ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    // Когда две змейки сталкиваются на объекте, сначала должно быть обработано столкновение, а уж
    // затем съедение объекта выжившей змейкой (если такая есть).
    @Test
    public void firstFightThenEatFury() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼®◄═══╕☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[5], FURY, WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼♣     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼♥     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertTrue(game.getFuryPills().isEmpty());
        verifyAllEvents("");
    }

    @Test
    // Предыдущий тест в инвертированных ролях. Эти два теста вместе показывают что порядок игроков
    // в списке List<Player> больше не влияет на результат таких столкновений.
    public void firstFightThenEatFuryInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼®<───ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[5], FURY, WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼♥     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼♣     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertTrue(game.getFuryPills().isEmpty());
        verifyAllEvents("");
    }

    // Тестируем что обработка столкновений происходите до съедения яблока.
    // Если бы яблоко "съелось" короткой змейкой до обработки столкновения, обе змейки умерли бы.
    @Test
    public void firstFightThenEatApple() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼○◄═══╕☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[5], APPLE, WIN]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertTrue(game.getApples().isEmpty());
        verifyAllEvents("");
    }

    // Тест зеркальный предыдущему чтобы показать что порядок игроков на сервере не влияет на исход столкновения.
    @Test
    public void firstFightThenEatAppleInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼○<───ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[5], APPLE, WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertTrue(game.getApples().isEmpty());
        verifyAllEvents("");
    }

    // Тестируем что обработка столкновений происходите до съедения камня.
    // Если бы камень "съелся" короткой змейкой до обработки столкновения, она умерла бы не повредив длинную.
    @Test
    public void firstFightThenEatStone() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼●◄╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[2], STONE, WIN]\n");

        // несмотря на то что на сервере столкновение обрабатывается до съедения камня,
        // съедение камня приводит к мгновенной утрате длинны (на 3),
        // тогда как убийство соперника приводит к утрате длинны только на следующий тик.
        // лично я бы апдейтил длинну выжившей змейки тоже сразу и не показывал бы части умерших змеек.
        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻╕    ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺ö    ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals("[]", game.getStones().toString());
        verifyAllEvents("");
    }

    // Тест зеркальный предыдущему, показывает что порядок игроков на сервере не влият на исход столкновения.
    @Test
    public void firstFightThenEatStoneInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼●<ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[2], STONE, WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺ö    ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻╕    ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals("[]", game.getStones().toString());
        verifyAllEvents("");
    }

    // Тестируем случай когда одна змейка с Fury идет на клетку где был хвост второй в момент когда вторая кушает яблоко.
    @Test
    public void eatTailThatGrows_Fury() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼˄®║   ☼" +
                "☼¤ ▼ ○ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        hero.right();
        game.tick();

        verifyAllEvents(
                "listener(1) => [FURY]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×♣╓   ☼" +
                "☼  ╚►○ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘♥æ   ☼" +
                "☼  └>○ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [APPLE]\n" +
                "listener(1) => [EAT[1]]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ×♣   ☼" +
                "☼  ╘═► ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ╘♥   ☼" +
                "☼  ×─> ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ×♣  ☼" +
                "☼   ╘═►☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╘♥  ☼" +
                "☼   ×─>☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertTrue(game.getApples().isEmpty());
        verifyAllEvents("");
    }

    // Тестируем врезание в отросший хвост.
    @Test
    public void eatTailThatGrows() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×>╓   ☼" +
                "☼  ╚►○ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [APPLE, EAT[2], WIN]\n" +
                "listener(1) => [DIE]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ×☺   ☼" +
                "☼  ╚═► ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ╘☻   ☼" +
                "☼  └─> ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╘══►☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ×──>☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertTrue(game.getApples().isEmpty());
        verifyAllEvents("");
    }

    @Test
    public void fightCase1() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼ <───ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[5], WIN]\n" +
                "listener(1) => [DIE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void fightCase2() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼<────ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☺☻───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☻☺═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void fightCase3() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼▼<───ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺───ö ☼" +
                "☼▼     ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻═══╕ ☼" +
                "☼˅     ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[5], WIN]\n" +
                "listener(1) => [DIE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼☻☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼☺☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n");
    }

    @Test
    public void fightCase4() {
        givenFl("☼☼☼☼☼☼☼☼☼" +
                "☼╔═╗    ☼" +
                "☼║ ╙    ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼║<───ö ☼" +
                "☼▼      ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼" +
                "☼╔═╕    ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼☺───ö  ☼" +
                "☼║      ☼" +
                "☼▼      ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼┌─ö    ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼☻═══╕  ☼" +
                "☼│      ☼" +
                "☼˅      ☼" +
                "☼☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[5], WIN]\n" +
                "listener(1) => [DIE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼" +
                "☼╔╕     ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼║      ☼" +
                "☼☻☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼┌ö     ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼│      ☼" +
                "☼☺☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n");
    }

    // если тиков для победы недостаточно, то WIN ты не получишь
    @Test
    public void shouldNoWin_whenIsNotEnoughTicksForWin() {
        settings.integer(ROUNDS_MIN_TICKS_FOR_WIN, 10);

        givenFl("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼◄══╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        enemy.right();
        hero.up();
        game.tick();

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[4]]\n");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻>    ☼" +
                "☼╚═╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺►    ☼" +
                "☼└─ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼×─>   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╘═►   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents("");
    }

    // с таблеткой полета и яростью я не откусываю хвост врага
    @Test
    public void shouldCase12_furyPlusFlying() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ┌──>           ☼\n" +
                "☼#○           ¤♥  ○          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚══╕         ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");

        hero.eatFlying(); // добрали таблетку полета

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ×♠──>          ☼\n" +
                "☼#○            ║  ○          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚═╕          ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.right();
        enemy.down();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ×╔♠─┐          ☼\n" +
                "☼#○            ║  ˅          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚╕           ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();

        hero.count();
        hero.count();
        hero.count();
        hero.count();
        assertEquals(2, hero.getFuryCount());
        assertEquals(2, hero.getFlyingCount());
        hero.count();
        assertEquals(1, hero.getFuryCount());
        assertEquals(1, hero.getFlyingCount());

        enemy.left();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼             ╔═×┐►         ☼\n" +
                "☼#○            ║  │          ☼\n" +
                "☼☼         ©  ╔╝  │          ☼\n" +
                "☼☼        ☼☼☼ ╚╕ <┘          ☼\n" +
                "☼☼       ☼  ☼                ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(1) => [APPLE]\n");
    }

    // с таблеткой ярости я отгрызаю хвосты,
    // даже те которые под яблоком вырастают
    @Test
    public void shouldCase12_justFury() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ┌──>           ☼\n" +
                "☼#○           ¤♥  ○          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚══╕         ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼             ♥×─>          ☼\n" +
                "☼#○            ║  ○          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚═╕          ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.right();
        enemy.down();
        game.tick();

        verifyAllEvents(
                "listener(0) => [EAT[2], EAT[1]]\n" +
                "listener(1) => [APPLE]\n");

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼             ╔♥×┐          ☼\n" +
                "☼#○            ║  ˅          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚╕           ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        verifyAllEvents("");

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼             ╔═♥æ          ☼\n" +
                "☼#○            ║  │          ☼\n" +
                "☼☼         ©  ╔╝  ˅          ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╙            ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

    }

    // с таблеткой полета я пархаю над врагом
    // пока полет не закончится - там змея моя
    // погибнет и враг заберет очки
    @Test
    public void shouldCase12_justFlying() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ┌──>           ☼\n" +
                "☼#○           ¤♠  ○          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚══╕         ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ×♠──>          ☼\n" +
                "☼#○            ║  ○          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚═╕          ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.right();
        enemy.down();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼            ×╔♠─┐          ☼\n" +
                "☼#○            ║  ˅          ☼\n" +
                "☼☼         ©  ╔╝             ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╚╕           ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        verifyAllEvents(
                "listener(1) => [APPLE]\n");

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼             ╔═♠┐          ☼\n" +
                "☼#○            ║  │          ☼\n" +
                "☼☼         ©  ╔╝  ˅          ☼\n" +
                "☼☼        ☼☼☼ ╚═╗            ☼\n" +
                "☼☼       ☼  ☼   ╙            ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.count();
        hero.count();
        hero.count();
        hero.count();
        hero.count();
        hero.count();
        assertEquals(1, hero.getFlyingCount());
        hero.count();
        assertEquals(0, hero.getFlyingCount());

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼      ○                    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼         ○       ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼                ☼   ●      ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼             ╔×─☻          ☼\n" +
                "☼#○            ║  │          ☼\n" +
                "☼☼         ©  ╔╝  │          ☼\n" +
                "☼☼        ☼☼☼ ╚═╕ ˅          ☼\n" +
                "☼☼       ☼  ☼                ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼   ○ ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                  ®    ©   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[10], WIN]\n");

    }

    @Test
    public void shouldChangeHeadSprite() {
        givenFl("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼╘►     ☼" +
                "☼       ☼" +
                "☼×>     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼╘►     ☼" +
                "☼       ☼" +
                "☼×>     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼×>     ☼" +
                "☼       ☼" +
                "☼╘►     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.eatFlying();
        enemy.eatFury();

        assertH("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼╘♠     ☼\n" +
                "☼       ☼\n" +
                "☼×♣     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼×♦     ☼" +
                "☼       ☼" +
                "☼╘♥     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.eatFury();
        enemy.eatFlying();

        assertH("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼╘♠     ☼\n" +
                "☼       ☼\n" +
                "☼×♦     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼×♦     ☼" +
                "☼       ☼" +
                "☼╘♠     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.removeFlying();
        enemy.removeFury();

        assertH("☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼╘♥     ☼\n" +
                "☼       ☼\n" +
                "☼×♦     ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼×♣     ☼" +
                "☼       ☼" +
                "☼╘♠     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        hero.removeFury();
        enemy.removeFlying();

        assertH("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼╘►     ☼" +
                "☼       ☼" +
                "☼×>     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼×>     ☼" +
                "☼       ☼" +
                "☼╘►     ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldCase13() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                         ● ☼\n" +
                "☼#        ○                  ☼\n" +
                "☼☼       ●                   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼                 ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼        $       ☼          ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼     ●                     ☼\n" +
                "☼#                           ☼\n" +
                "☼☼ ●                         ☼\n" +
                "☼☼ ● ○    ☼☼☼                ☼\n" +
                "☼☼       ☼  ☼                ☼\n" +
                "☼☼      ☼☼☼☼#    ●☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼    ○         ×─┐          ☼\n" +
                "☼☼                └─┐        ☼\n" +
                "☼#                  └┐     ● ☼\n" +
                "☼☼               ┌───┘╓      ☼\n" +
                "☼☼               └───♣╚► ©   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");

        hero.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                         ● ☼\n" +
                "☼#        ○                  ☼\n" +
                "☼☼       ●                   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼                 ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼        $       ☼          ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼     ●                     ☼\n" +
                "☼#                           ☼\n" +
                "☼☼ ●                         ☼\n" +
                "☼☼ ● ○    ☼☼☼                ☼\n" +
                "☼☼       ☼  ☼                ☼\n" +
                "☼☼      ☼☼☼☼#    ●☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼    ○          ×┐          ☼\n" +
                "☼☼                └─┐        ☼\n" +
                "☼#                  └┐     ● ☼\n" +
                "☼☼               ┌───┘ ▲     ☼\n" +
                "☼☼               └────♣╙ ©   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼                         ● ☼\n" +
                "☼#        ○                  ☼\n" +
                "☼☼       ●                   ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼     ☼☼☼☼☼                 ☼\n" +
                "☼☼     ☼                     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼#      ☼\n" +
                "☼☼     ☼          ☼   ☼  ●   ☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#      ☼\n" +
                "☼☼        $       ☼          ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼     ●                     ☼\n" +
                "☼#                           ☼\n" +
                "☼☼ ●                         ☼\n" +
                "☼☼ ● ○    ☼☼☼                ☼\n" +
                "☼☼       ☼  ☼                ☼\n" +
                "☼☼      ☼☼☼☼#    ●☼☼   ☼#    ☼\n" +
                "☼☼      ☼   ☼     ☼ ☼ ☼ ☼    ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                ☼     ☼    ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼    ○           æ          ☼\n" +
                "☼☼                └─┐        ☼\n" +
                "☼#                  └┐ ▲   ● ☼\n" +
                "☼☼               ┌───┘ ╙     ☼\n" +
                "☼☼               └─────♣ ©   ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(1) => [EAT[1]]\n");
    }

    @Test
    public void headStrikeDisplay() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼˅     ☼" +
                "☼ ◄══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼☺══╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼☻──ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[2], WIN]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void headStrikeDisplayInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼▼     ☼" +
                "☼ <──ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼☻──ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼☺══╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[2], WIN]\n");
    }

    @Test
    public void headStrikeDisplay_sameSize() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼ ◄═╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼☻═╕   ☼" + // TODO тут рисуется не та голова, как в 90% случаев
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼☺─ö   ☼" +  // TODO тут рисуется не та голова, как в 90% случаев
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void headStrikeDisplayInverted_sameSize() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼ <─ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼☻─ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼☺═╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void headStrikeDisplay2() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼˅     ☼" +
                "☼ ◄╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼☻╕    ☼" + // TODO тут рисуется не та голова, как в 90% случаев
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼☺ö    ☼" + // TODO тут рисуется не та голова, как в 90% случаев
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void headStrikeDisplayInverted2() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼▼     ☼" +
                "☼ <ö   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼☺ö    ☼" + // TODO тут рисуется не та голова, как в 90% случаев
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼☻╕    ☼" + // TODO тут рисуется не та голова, как в 90% случаев
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void bodyStrikeDisplay() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼  ˅   ☼" +
                "☼ ◄══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼◄═☺╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼<─☻ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [EAT[2], WIN]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void bodyStrikeDisplayInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼  ▼   ☼" +
                "☼ <──ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼<─☻ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼◄═☺╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [EAT[2], WIN]\n");
    }

    @Test
    public void flightOverBodyDisplay() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼  ˅   ☼" +
                "☼  ©   ☼" +
                "☼  ◄══╕☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(1) => [FLYING]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼◄═♦╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼<─♠ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☻══æ   ☼\n" +
                "☼  ♦   ☼\n" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☺──╓   ☼\n" +
                "☼  ♠   ☼\n" +
                "☼☼☼☼☼☼☼☼");

        verifyAllEvents(
                "listener(0) => [DIE]\n" +
                "listener(1) => [WIN]\n");
    }

    @Test
    public void flightOverBodyDisplayInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼  ▼   ☼" +
                "☼  ©   ☼" +
                "☼  <──ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [FLYING]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼<─♠ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼◄═♦╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☺──╓   ☼\n" +
                "☼  ♠   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☻══æ   ☼\n" +
                "☼  ♦   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [WIN]\n" +
                "listener(1) => [DIE]\n");
    }

    @Test
    public void flightOverHeadDisplay() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼  ˅   ☼" +
                "☼  ©   ☼" +
                "☼    ◄╕☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(1) => [FLYING]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼  ♦╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼  ♠ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ◄æ   ☼\n" +
                "☼  ♦   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ <╓   ☼\n" +
                "☼  ♠   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void flightOverHeadDisplayInverted() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼  ▼   ☼" +
                "☼  ©   ☼" +
                "☼    <ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        verifyAllEvents(
                "listener(0) => [FLYING]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼  ♠ö  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  æ   ☼" +
                "☼  ♦╕  ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ <╓   ☼\n" +
                "☼  ♠   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ◄æ   ☼\n" +
                "☼  ♦   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void headTwoFlyingSnakes_drawSmallerOnTop_start() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼           ☼" +
                "☼           ☼" +
                "☼           ☼" +
                "☼           ☼" +
                "☼           ☼" +
                "☼   ┌─> ◄══╕☼" +
                "☼   │       ☼" +
                "☼   │       ☼" +
                "☼   └───ö   ☼" +
                "☼           ☼" +
                "☼           ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");

        hero.eatFlying();
        enemy.eatFlying();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ┌──♠══╕ ☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   └──ö    ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ╔══♦──ö ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ╚══╕    ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ┌─♠══╕  ☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   └─ö     ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ╔═♦──ö  ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ╚═╕     ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ┌♠══╕♦  ☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   └ö      ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ╔♦──ö♠  ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ╚╕      ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ♠══╕──♦ ☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   ¤       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ♦──ö══♠ ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ╙       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        hero.down();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ╔═╕────♦☼\n" +
                "☼   ♠       ☼\n" +
                "☼   ¤       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼   ┌─ö════♠☼\n" +
                "☼   ♦       ☼\n" +
                "☼   ╙       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void headTwoFlyingSnakes_drawSmallerOnTop_finishWithFlying() {
        headTwoFlyingSnakes_drawSmallerOnTop_start();

        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ♦☼\n" +
                "☼   ╔╕─────┘☼\n" +
                "☼   ║       ☼\n" +
                "☼   ♠       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ♠☼\n" +
                "☼   ┌ö═════╝☼\n" +
                "☼   │       ☼\n" +
                "☼   ♦       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ♦☼\n" +
                "☼          │☼\n" +
                "☼   ╓──────┘☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ♠       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ♠☼\n" +
                "☼          ║☼\n" +
                "☼   æ══════╝☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   ♦       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ♦☼\n" +
                "☼          │☼\n" +
                "☼          │☼\n" +
                "☼    ×─────┘☼\n" +
                "☼   ╓       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ♠       ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ♠☼\n" +
                "☼          ║☼\n" +
                "☼          ║☼\n" +
                "☼    ╘═════╝☼\n" +
                "☼   æ       ☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   ♦       ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void headTwoFlyingSnakes_drawSmallerOnTop_finishWithoutFlying() {
        headTwoFlyingSnakes_drawSmallerOnTop_start();

        hero.removeFlying();
        enemy.removeFlying();

        assertEquals(0, hero.getFlyingCount());
        assertEquals(0, enemy.getFlyingCount());

        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ˄☼\n" +
                "☼   ╔╕─────┘☼\n" +
                "☼   ║       ☼\n" +
                "☼   ▼       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ▲☼\n" +
                "☼   ┌ö═════╝☼\n" +
                "☼   │       ☼\n" +
                "☼   ˅       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ˄☼\n" +
                "☼          │☼\n" +
                "☼   ╓──────┘☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ▼       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ▲☼\n" +
                "☼          ║☼\n" +
                "☼   æ══════╝☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   ˅       ☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ˄☼\n" +
                "☼          │☼\n" +
                "☼          │☼\n" +
                "☼    ×─────┘☼\n" +
                "☼   ╓       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ║       ☼\n" +
                "☼   ▼       ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼           ☼\n" +
                "☼           ☼\n" +
                "☼          ▲☼\n" +
                "☼          ║☼\n" +
                "☼          ║☼\n" +
                "☼    ╘═════╝☼\n" +
                "☼   æ       ☼\n" +
                "☼   │       ☼\n" +
                "☼   │       ☼\n" +
                "☼   ˅       ☼\n" +
                "☼           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void twoFlyingSnakes_andOneApple_whoIsFirst() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼ ┌─>○◄═╕ ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼         ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼");

        hero.eatFlying();
        enemy.eatFlying();
        game.tick();

        verifyAllEvents(
                "listener(0) => [APPLE]\n");

        assertH("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ×──♠══╕ ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ╘══♦──ö ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼  ×♠══╕  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼  ╘♦──ö  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼  ♠══╕♦  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼  ♦──ö♠  ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ♠══╕──♦ ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼ ♦──ö══♠ ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼♠══╕ ×──♦☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼♦──ö ╘══♠☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldCase14() {
        givenFl("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼            ○              ☼\n" +
                "☼#   ©  ○                    ☼\n" +
                "☼☼       ●       ○         ○ ☼\n" +
                "☼☼                      ○    ☼\n" +
                "☼☼   ○   ●   ●               ☼\n" +
                "☼☼●    ☼☼☼☼☼                 ☼\n" +
                "☼☼  ●  ☼               ○     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼# ○    ☼\n" +
                "☼☼  ○  ☼          ☼○○®☼  ●  ○☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#○     ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼    ●                      ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼       $☼☼☼           ●    ☼\n" +
                "☼☼       ☼  ☼        ╘════╗  ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼# ║  ☼\n" +
                "☼☼      ☼   ☼   ● ☼ ☼ ☼ ☼ ║  ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼ ║╔╗☼\n" +
                "☼☼                ☼     ☼ ╚╝║☼\n" +
                "☼☼     ●  ●   æ   ☼    ●☼   ║☼\n" +
                "☼☼            └──────┐      ║☼\n" +
                "☼☼              ┌────┘      ║☼\n" +
                "☼☼             ●│           ♥☼\n" +
                "☼#             ┌┘    ┌─────┐ ☼\n" +
                "☼☼             └─────┘    <┘ ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");

        // hero идет по инерции вниз
        // enemy идет по инерции влево
        game.tick();

        hero.left();
        // enemy идет по инерции влево
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☼            ○              ☼\n" +
                "☼#   ©  ○                    ☼\n" +
                "☼☼       ●       ○         ○ ☼\n" +
                "☼☼                      ○    ☼\n" +
                "☼☼   ○   ●   ●               ☼\n" +
                "☼☼●    ☼☼☼☼☼                 ☼\n" +
                "☼☼  ●  ☼               ○     ☼\n" +
                "☼#     ☼☼☼        ☼☼☼☼# ○    ☼\n" +
                "☼☼  ○  ☼          ☼○○®☼  ●  ○☼\n" +
                "☼☼     ☼☼☼☼#      ☼☼☼☼#○     ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼                ☼          ☼\n" +
                "☼☼    ●                      ☼\n" +
                "☼#                           ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼       $☼☼☼           ●    ☼\n" +
                "☼☼       ☼  ☼          ╘══╗  ☼\n" +
                "☼☼      ☼☼☼☼#     ☼☼   ☼# ║  ☼\n" +
                "☼☼      ☼   ☼   ● ☼ ☼ ☼ ☼ ║  ☼\n" +
                "☼#      ☼   ☼     ☼  ☼  ☼ ║╔╗☼\n" +
                "☼☼                ☼     ☼ ╚╝║☼\n" +
                "☼☼     ●  ●       ☼    ●☼   ║☼\n" +
                "☼☼                          ║☼\n" +
                "☼☼                          ║☼\n" +
                "☼☼             ●            ║☼\n" +
                "☼#                         ♥╝☼\n" +
                "☼☼                      <──ö ☼\n" +
                "☼☼                           ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [EAT[30]]\n");
    }

    // была бага, когда откусывали от змейки два раза, то она второй раз не давалась
    // попутно в этом тесте тестируется что будет, если во время откусывания по fury скушать яблоко
    @Test
    public void eatTwiceSameSnake() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼  ╓   ☼" +
                "☼○ ▼   ☼" +
                "☼○ ®®  ☼" +
                "☼○○<──ö☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼○ ╓   ☼\n" +
                "☼○ ♥®  ☼\n" +
                "☼○<───ö☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼○ æ   ☼\n" +
                "☼○ ♣®  ☼\n" +
                "☼○◄═══╕☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [FURY]\n" +
                "listener(1) => [APPLE]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼○     ☼\n" +
                "☼○ ╓®  ☼\n" +
                "☼<ö♥   ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼○     ☼\n" +
                "☼○ æ®  ☼\n" +
                "☼◄╕♣   ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [EAT[3], EAT[1]]\n" +
                "listener(1) => [APPLE]\n");

        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼○     ☼\n" +
                "☼˄  ®  ☼\n" +
                "☼└ö╓   ☼\n" +
                "☼  ♥   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼○     ☼\n" +
                "☼▲  ®  ☼\n" +
                "☼╚╕æ   ☼\n" +
                "☼  ♣   ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼˄     ☼\n" +
                "☼│  ®  ☼\n" +
                "☼└ö    ☼\n" +
                "☼  ╘♥  ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼▲     ☼\n" +
                "☼║  ®  ☼\n" +
                "☼╚╕    ☼\n" +
                "☼  ×♣  ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        enemy.right();
        hero.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼┌>    ☼\n" +
                "☼│  ®  ☼\n" +
                "☼¤  ♥  ☼\n" +
                "☼   ╙  ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼╔►    ☼\n" +
                "☼║  ®  ☼\n" +
                "☼╙  ♣  ☼\n" +
                "☼   ¤  ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼┌─>   ☼\n" +
                "☼¤  ♥  ☼\n" +
                "☼   ╙  ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼╔═►   ☼\n" +
                "☼╙  ♣  ☼\n" +
                "☼   ¤  ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [FURY]\n" +
                "listener(1) => [APPLE, APPLE]\n");

        hero.left();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼×──>  ☼\n" +
                "☼  ♥╕  ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼╘══►  ☼\n" +
                "☼  ♣ö  ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ♥×> ☼\n" +
                "☼  ╙   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ♣╘► ☼\n" +
                "☼  ¤   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [EAT[2]]\n");
    }

    @Test
    public void eat() {
        givenFl("☼☼☼☼☼☼☼☼☼☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼   ╔♥   ☼" +
                "☼   ║┌>  ☼" +
                "☼  ╔╝└─┐ ☼" +
                "☼  ╙ ×─┘ ☼" +
                "☼        ☼" +
                "☼        ☼" +
                "☼☼☼☼☼☼☼☼☼☼");

        hero.down();
        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼   ╔╗˄  ☼\n" +
                "☼   ║♥¤  ☼\n" +
                "☼  ╘╝    ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼   ┌┐▲  ☼\n" +
                "☼   │♣╙  ☼\n" +
                "☼  ×┘    ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        verifyAllEvents(
                "listener(0) => [EAT[6]]\n");

        game.tick();

        assertH("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼     ˄  ☼\n" +
                "☼   ╔╗¤  ☼\n" +
                "☼   ║║   ☼\n" +
                "☼   ╙♥   ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");

        assertE("☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        ☼\n" +
                "☼     ▲  ☼\n" +
                "☼   ┌┐╙  ☼\n" +
                "☼   ││   ☼\n" +
                "☼   ¤♣   ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼\n");
    }
}

