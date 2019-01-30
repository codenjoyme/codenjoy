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


import com.codenjoy.dojo.services.CustomMessage;
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

import static org.junit.Assert.assertEquals;
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
                roundsPerMatch);

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

        verify(heroEvents).event(Events.DIE);

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

        verify(heroEvents).event(Events.DIE);
        verify(enemyEvents).event(Events.WIN);
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
                "☼ ╘☻► ☼" +
                "☼  ¤  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×☺> ☼" +
                "☼  ╙  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verify(heroEvents).event(Events.WIN);
        verify(enemyEvents).event(Events.DIE);
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╘═►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ×─>☼" +
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
                "☼ ×☻> ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  æ  ☼" +
                "☼ ╘☺► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verify(heroEvents).event(Events.DIE);
        verify(enemyEvents).event(Events.WIN);

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ×─>☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ╘═►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что змейка умирает, когда врезается в хвост другой змейки
    @Test
    public void shouldDie_whenTailCrashToOtherSnake_enemyDie() {
        // когда в игрока врезается противник
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

        enemy.up();
        game.tick();

        verify(heroEvents).event(Events.WIN);
        verify(enemyEvents).event(Events.DIE);

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ☻► ☼" +
                "☼ ×┘  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ☺> ☼" +
                "☼ ╘╝  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        game.tick();

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

    // такой же тест, но врезается игрок в противника
    // (последовательность героев в списке может оказывать значение на результат)
    @Test
    public void shouldDie_whenTailCrashToOtherSnake_heroDie() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ╘►  ☼" +
                "☼ ×>  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ×>  ☼" +
                "☼ ╘►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        hero.down();
        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ╓  ☼" +
                "☼  ☻> ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  æ  ☼" +
                "☼  ☺► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        verify(heroEvents).event(Events.DIE);
        verify(enemyEvents).event(Events.WIN);

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ×>☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");

        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ╘►☼" +
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

        game.tick();

        assertH("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ╓☼" +
                "☼    ♥☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
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
        verify(heroEvents).event(Events.APPLE);
        enemy.up();
        game.tick();
        verify(heroEvents, times(2)).event(Events.APPLE);
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

        verify(enemyEvents).event(Events.DIE);
        verify(heroEvents).event(Events.WIN);

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

        verify(heroEvents).event(new CustomMessage("...3..."));
        verify(enemyEvents).event(new CustomMessage("...3..."));

        game.tick();

        verify(heroEvents).event(new CustomMessage("..2.."));
        verify(enemyEvents).event(new CustomMessage("..2.."));

        game.tick();

        verify(heroEvents).event(new CustomMessage(".1."));
        verify(enemyEvents).event(new CustomMessage(".1."));

        game.tick();

        verify(heroEvents).event(Events.START);
        verify(enemyEvents).event(Events.START);

        verify(heroEvents).event(new CustomMessage("Round 1"));
        verify(enemyEvents).event(new CustomMessage("Round 1"));

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

        verify(heroEvents).event(Events.START);
        verify(enemyEvents).event(Events.START);

        verify(heroEvents).event(new CustomMessage("Round 1"));
        verify(enemyEvents).event(new CustomMessage("Round 1"));
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

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

        verify(heroEvents).event(Events.DIE);
        verify(enemyEvents).event(Events.WIN);
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
        assertEquals(false, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(false, enemyPlayer.isActive());

        verify(heroEvents).event(Events.START);
        verify(enemyEvents).event(Events.START);

        verify(heroEvents).event(new CustomMessage("Round 2"));
        verify(enemyEvents).event(new CustomMessage("Round 2"));
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        readyGo();

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

        verify(heroEvents).event(Events.DIE);
        verify(enemyEvents).event(Events.DIE);
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
        assertEquals(false, heroPlayer.isActive());

        assertEquals(true, enemyPlayer.isAlive());
        assertEquals(false, enemyPlayer.isActive());

        verify(heroEvents).event(Events.START);
        verify(enemyEvents).event(Events.START);

        verify(heroEvents).event(new CustomMessage("Round 3"));
        verify(enemyEvents).event(new CustomMessage("Round 3"));
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        // пока змейку не мувнут, они не двигаются
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertH("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "~&     ☼" +
                "*ø     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");

        readyGo();

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

        verify(heroEvents).event(Events.DIE);
        verify(enemyEvents).event(Events.DIE);
        verifyNoMoreInteractions(heroEvents, enemyEvents);
        reset(heroEvents, enemyEvents);

        assertEquals(false, heroPlayer.isAlive());
        assertEquals(true, heroPlayer.isActive());

        assertEquals(false, enemyPlayer.isAlive());
        assertEquals(true, enemyPlayer.isActive());

        game.remove(heroPlayer);  // это делает автоматом фреймворк потому что heroPlayer.shouldLeave()
        game.remove(enemyPlayer); // это делает автоматом фреймворк потому что enemyPlayer.shouldLeave()

    }

    private void readyGo() {
        assertEquals(false, heroPlayer.isActive());
        assertEquals(false, enemyPlayer.isActive());
        heroPlayer.getHero().act(); // первое движение змейки заставляет ее начинать движение
        assertEquals(true, heroPlayer.isActive());
        assertEquals(false, enemyPlayer.isActive());
        enemyPlayer.getHero().act(); // первое движение змейки заставляет ее начинать движение
        assertEquals(true, heroPlayer.isActive());
        assertEquals(true, enemyPlayer.isActive());
    }
}

