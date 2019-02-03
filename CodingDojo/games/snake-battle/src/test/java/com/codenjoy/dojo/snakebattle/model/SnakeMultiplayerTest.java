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
import com.codenjoy.dojo.services.settings.SimpleParameter;
import com.codenjoy.dojo.snakebattle.model.board.SnakeBoard;
import com.codenjoy.dojo.snakebattle.model.board.Timer;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.level.LevelImpl;
import com.codenjoy.dojo.snakebattle.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * ®author Kors
 */
public class SnakeMultiplayerTest {
    private SnakeBoard game;
    private Dice dice;

    private Hero hero;
    private EventListener heroEvents;
    private Player heroPlayer;

    private Hero enemy;
    private EventListener enemyEvents;
    private Player enemyPlayer;

    private PrinterFactory printer = new PrinterFactoryImpl();
    private SimpleParameter<Integer> timer;
    private SimpleParameter<Integer> roundsPerMatch;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        timer = new SimpleParameter<>(0);
        roundsPerMatch = new SimpleParameter<>(5);
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        game = new SnakeBoard(level, dice,
                new Timer(timer),
                new Timer(new SimpleParameter<>(300)),
                roundsPerMatch,
                new SimpleParameter<>(10),
                new SimpleParameter<>(10),
                new SimpleParameter<>(3));

        Hero hero = level.getHero();
        hero.setActive(true);
        heroEvents = mock(EventListener.class);
        heroPlayer = new Player(heroEvents);
        game.newGame(heroPlayer);
        heroPlayer.setHero(hero);
        hero.init(game);
        this.hero = game.getHeroes().get(0);

        Hero enemy = level.getEnemy();
        enemy.setActive(true);
        enemyEvents = mock(EventListener.class);
        enemyPlayer = new Player(enemyEvents);
        game.newGame(enemyPlayer);
        enemyPlayer.setHero(enemy);
        enemy.init(game);
        this.enemy = game.getHeroes().get(1);
    }

    // проверяем что соперник отображается на карте
    @Test
    public void shouldheroWithEnemyOnField() {
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[2], WIN]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");

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

        verifyEvents(heroEvents, "[EAT[3], WIN]");
        verifyEvents(enemyEvents, "[DIE]");

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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[3], WIN]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");

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
        hero.down();
        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ▼  ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ♦  ☼" +
                "☼  ╓  ☼" +
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

        verifyEvents(enemyEvents, "[APPLE, APPLE]");

        hero.down();
        enemy.up();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ╓☼" +
                "☼    ♥☼" +
                "☼  ×─┘☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyEvents(heroEvents, "[EAT[4], WIN]");
        verifyEvents(enemyEvents, "[DIE]");

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ╓☼" +
                "☼    ♥☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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
        verifyEvents(heroEvents, "[APPLE]");
        enemy.up();
        game.tick();
        verifyEvents(heroEvents, "[APPLE]");
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
    }

    private void assertH(String expected) {
        assertBoard(expected, heroPlayer);
    }

    private void assertBoard(String expected, Player player) {
        assertEquals(TestUtils.injectN(expected),
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

        verifyEvents(enemyEvents, "[DIE]");
        verifyEvents(heroEvents, "[WIN]");

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

    // змейка не стартует сразу если стоит таймер
    @Test
    public void shouldWaitTillTimer_thenStart() {
        timer.update(4);

        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼╘►   ☼" +
                "☼     ☼" +
                "☼×>   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        // ждем 4 тика
        game.tick();

        verifyEvents(heroEvents, "[[...3...]]");
        verifyEvents(enemyEvents, "[[...3...]]");

        game.tick();

        verifyEvents(heroEvents, "[[..2..]]");
        verifyEvents(enemyEvents, "[[..2..]]");

        game.tick();

        verifyEvents(heroEvents, "[[.1.]]");
        verifyEvents(enemyEvents, "[[.1.]]");

        game.tick();

        verifyEvents(heroEvents, "[START, [Round 1]]");
        verifyEvents(enemyEvents, "[START, [Round 1]]");

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
        timer.update(1);
        roundsPerMatch.update(3);

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

        verifyEvents(heroEvents, "[START, [Round 1]]");
        verifyEvents(enemyEvents, "[START, [Round 1]]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[WIN]");
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
        assertEquals(false, enemyPlayer.isActive());

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "~&     ☼" +
                "*ø     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        // ждем перехода на второй уровень
        game.tick();

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        verifyEvents(heroEvents, "[START, [Round 2]]");
        verifyEvents(enemyEvents, "[START, [Round 2]]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");
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

        // ждем перехода на третий уровень
        game.tick();

        assertEquals(true, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        verifyEvents(heroEvents, "[START, [Round 3]]");
        verifyEvents(enemyEvents, "[START, [Round 3]]");

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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[DIE]");
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        assertEquals(false, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(false, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        game.remove(heroPlayer);  // это делает автоматом фреймворк потому что heroPlayer.shouldLeave()
        game.remove(enemyPlayer); // это делает автоматом фреймворк потому что enemyPlayer.shouldLeave()

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
        game.tick();

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[3], WIN]");

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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[]");
        verifyEvents(enemyEvents, "[EAT[1]]");

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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[WIN]");

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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[4], WIN]");

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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
    }

    public static void verifyEvents(EventListener events, String expected) {
        if (expected.equals("[]")) {
            verify(events, never()).event(any(Events.class));
        } else {
            ArgumentCaptor<Events> captor = ArgumentCaptor.forClass(Events.class);
            verify(events, atLeast(1)).event(captor.capture());
            assertEquals(expected, captor.getAllValues().toString());
        }
        reset(events);
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

        verifyEvents(heroEvents, "[EAT[4], WIN]");
        verifyEvents(enemyEvents, "[DIE]");

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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[EAT[4], WIN]");
        verifyEvents(enemyEvents, "[DIE]");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼▲☺    ☼" +
                "☼╚═══╕ ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼˄☻    ☼" +
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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[4], WIN]");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼☻     ☼" +
                "☼˅═╕   ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼☺     ☼" +
                "☼▼─ö   ☼" +
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

        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[5], WIN]");

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
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[EAT[5], WIN]");
        verifyEvents(enemyEvents, "[DIE]");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╕   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼♥───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─ö   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼♣═══╕ ☼" +
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
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[5], APPLE, WIN]");

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
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[EAT[5], APPLE, WIN]");
        verifyEvents(enemyEvents, "[DIE]");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼╔═╗   ☼" +
                "☼║ ╙   ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼▼───ö ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼┌─┐   ☼" +
                "☼│ ¤   ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼˅═══╕ ☼" +
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
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[DIE]");
        verifyEvents(enemyEvents, "[EAT[2], STONE, WIN]");

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

        assertTrue(game.getStones().isEmpty());
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[EAT[2], STONE, WIN]");
        verifyEvents(enemyEvents, "[DIE]");

        assertH("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼╓     ☼" +
                "☼║     ☼" +
                "☼║     ☼" +
                "☼▼ö    ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼æ     ☼" +
                "☼│     ☼" +
                "☼│     ☼" +
                "☼˅╕    ☼" +
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

        assertTrue(game.getStones().isEmpty());
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[APPLE]");
        verifyEvents(enemyEvents, "[EAT[1]]");

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
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
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

        verifyEvents(heroEvents, "[APPLE, EAT[2], WIN]");
        verifyEvents(enemyEvents, "[DIE]");

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
        verifyNoMoreInteractions(heroEvents);
        verifyNoMoreInteractions(enemyEvents);
    }
}

