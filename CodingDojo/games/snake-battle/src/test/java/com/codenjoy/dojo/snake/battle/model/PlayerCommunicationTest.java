package com.codenjoy.dojo.snake.battle.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.snake.battle.model.board.SnakeBoard;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;
import com.codenjoy.dojo.snake.battle.model.level.LevelImpl;
import com.codenjoy.dojo.snake.battle.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Kors
 */
public class PlayerCommunicationTest {
    private SnakeBoard game;
    private Hero hero;
    private Hero enemy;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    private String simpleField =
            "☼☼☼☼☼☼☼" +
            "☼     ☼" +
            "☼ →►  ☼" +
            "☼     ☼" +
            "☼ ⇒>  ☼" +
            "☼     ☼" +
            "☼☼☼☼☼☼☼";

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);
        hero.setActive(true);
        Hero enemy = level.getEnemy().get(0);
        enemy.setActive(true);
        game = new SnakeBoard(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        Player player2 = new Player(mock(EventListener.class));
        game.newGame(player);
        game.newGame(player2);
        player2.setHero(enemy);
        player.setHero(hero);
        hero.init(game);
        enemy.init(game);
        this.hero = game.getHeroes().get(0);
        this.enemy = game.getHeroes().get(1);
    }

    // проверяем что соперник отображается на карте
    @Test
    public void enemyOnField() {
        givenFl(simpleField);
        assertE(simpleField);
    }

    // спящие змеи
    @Test
    public void testSleepingSnake() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼ ⇒>  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        hero.setActive(false);
        enemy.setActive(false);
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ᓕ⬢  ☼" +
                "☼     ☼" +
                "☼ ᓚ⬡  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ᓕ⬢  ☼" +
                "☼     ☼" +
                "☼ ᓚ⬡  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что обе змейки умирают, когда врезаются в равного соперника
    // и получаем оповещение о смерти
    @Test
    public void diedBothHeroes() {
        givenFl(simpleField);
        hero.down();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ☻  ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        verify(listener).event(Events.DIE);
        game.tick();
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
    public void diedSmallerHero() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼ ⇒>○○☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        game.tick();
        hero.down();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ↓☼" +
                "☼    ☻☼" +
                "☼  ⇒—╜☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        verify(listener).event(Events.DIE);
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ∧☼" +
                "☼    ⇑☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼    ∧☼" +
                "☼    ⇑☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что змейка умирает, когда врезается в тело другой змейки
    @Test
    public void diedByBody() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►○  ☼" +
                "☼⇒>   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        verify(listener).event(Events.APPLE);
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →═► ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        verify(listener).event(Events.ALIVE);
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  →═►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        // такой же тест, но врезается игрок в противника
        // (последовательность героев в списке может оказывать значение на результат)
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►   ☼" +
                "☼⇒>○  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼ ⇒☻> ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        verify(listener).event(Events.DIE);
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ⇒—>☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // проверяем что змейка умирает, когда врезается в хвост другой змейки
    @Test
    public void diedByTail() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►   ☼" +
                "☼⇒>○  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  →► ☼" +
                "☼ ⇒╜  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼   →►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        // такой же тест, но врезается игрок в противника
        // (последовательность героев в списке может оказывать значение на результат)
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►   ☼" +
                "☼⇒>   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ☻> ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        verify(listener).event(Events.DIE);
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ⇒>☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // в полёте змейки не вредят друг-другу (летишь сам)
    @Test
    public void flyOverEnemy() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►%  ☼" +
                "☼     ☼" +
                "☼⇒>   ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        hero.down();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ⊖  ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ∧  ☼" +
                "☼  ↓  ☼" +
                "☼  ⊖  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼  ∧  ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ⊖  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // в полёте змейки не вредят друг-другу (летит враг)
    @Test
    public void flyOverHero() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►   ☼" +
                "☼     ☼" +
                "☼⇒>%  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        hero.down();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ⊘  ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼  ⊘  ☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ▼  ☼" +
                "☼☼☼☼☼☼☼");
    }

    // в случае ярости, змея может съесть другую
    @Test
    public void eatEnemy() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►@ ☼" +
                "☼     ☼" +
                "☼ ⇒>○○☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        game.tick();
        hero.down();
        enemy.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼    ↓☼" +
                "☼    ⊕☼" +
                "☼  ⇒—╜☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ↓☼" +
                "☼    ⊕☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // в случае ярости, можно откусить кусок змейки соперника
    @Test
    public void eatEnemyTail() {
        // когда в игрока врезается противник
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►○○ ☼" +
                "☼     ☼" +
                "☼⇒>@  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        verify(listener).event(Events.APPLE);
        enemy.up();
        game.tick();
        verify(listener, times(2)).event(Events.APPLE);
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ⊗→►☼" +
                "☼  ⇑  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        // такой же тест, но врезается игрок в противника
        // (последовательность героев в списке может оказывать значение на результат)
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►@  ☼" +
                "☼     ☼" +
                "☼⇒>○  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        hero.down();
        game.tick();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ⊕⇒>☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }


    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }
}
