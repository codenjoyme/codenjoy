package com.codenjoy.dojo.snake.battle.model.board;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.snake.battle.model.Player;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;
import com.codenjoy.dojo.snake.battle.model.level.LevelImpl;
import com.codenjoy.dojo.snake.battle.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Kors
 */
public class SnakeBoardTest {

    private SnakeBoard game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        List<Hero> heroes = level.getHero();
        Hero hero = heroes.isEmpty() ? null : heroes.get(0);

        game = new SnakeBoard(level, dice);
        game.debugMode = true;
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        if (hero != null) {
            player.setHero(hero);
            hero.init(game);
        }
        this.hero = game.getHeroes().get(0);
        this.hero.setActive(true);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // карта со своей змейкой
    @Test
    public void testStartField() {
        // самый простой начальный случай
        testField("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
    }

    // старт змейки из "стартового бокса"
    @Test
    public void startFromBox() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "→►     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼→►    ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼☼" +
                "☼☼     ☼" +
                "☼#→►   ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼     ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    // карта с яблоками, камнями, пилюлями полёта, пилюлями ярости, деньгами
    @Test
    public void testStartFieldWithApples() {
        testField("☼☼☼☼☼☼☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼ $ @ ☼" +
                "☼  ●  ☼" +
                "☼  % ○☼" +
                "☼☼☼☼☼☼☼");
    }

    private void testField(String field) {
        givenFl(field);
        assertE(field);
    }


    // тест продолжения движения без дополнительных указаний
    @Test
    public void moveAfterStart() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼   →►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
    }


    // тест движения в заданную сторону
    @Test
    public void moveByCommand() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼→►   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼ ↓   ☼" +
                "☼ ▼   ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.right();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼  ▲  ☼" +
                "☼  ↑  ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.left();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼ ◄←  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
    }


    // тест роста
    // заодно, тест отображения тела змейки
    @Test
    public void growUpTest() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→►○  ☼" +
                "☼     ☼" +
                "☼  ○  ☼" +
                "☼  ○  ☼" +
                "☼☼☼☼☼☼☼");
        hero.right();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼→═►  ☼" +
                "☼     ☼" +
                "☼  ○  ☼" +
                "☼  ○  ☼" +
                "☼☼☼☼☼☼☼");
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →╗  ☼" +
                "☼  ▼  ☼" +
                "☼  ○  ☼" +
                "☼  ○  ☼" +
                "☼☼☼☼☼☼☼");
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →╗  ☼" +
                "☼  ║  ☼" +
                "☼  ▼  ☼" +
                "☼  ○  ☼" +
                "☼☼☼☼☼☼☼");
        hero.left();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼  ║  ☼" +
                "☼ ◄╝  ☼" +
                "☼  ○  ☼" +
                "☼☼☼☼☼☼☼");
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼ ╔╝  ☼" +
                "☼ ▼○  ☼" +
                "☼☼☼☼☼☼☼");
        hero.right();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ↓  ☼" +
                "☼ ╔╝  ☼" +
                "☼ ╚►  ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ╔←  ☼" +
                "☼ ╚═► ☼" +
                "☼☼☼☼☼☼☼");
    }

    // тест смерти маленькой змейки об камень
    @Test
    public void dieByStone() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼→►●  ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        verify(listener).event(Events.STONE);
        verify(listener).event(Events.DIE);
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ →☻  ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
    }

    // тест смерти об стену
    @Test
    public void dieByWall() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ →►○ ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        game.tick(); // удлинняем змею (иначе будет не ясно исчезла змея или просто вся вошла в стену)
        verify(listener).event(Events.APPLE);
        game.tick();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   →═☻" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        verify(listener).event(Events.DIE);
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
    }

    // змея не может разворачиваться в противоположную сторону
    @Test
    public void deniedMovingBack() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.left();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.up();
        game.tick();
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼   ▲ ☼" +
                "☼   ↑ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.left();
        game.tick();
        hero.right();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼ ◄←  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
        hero.down();
        game.tick();
        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ↓   ☼" +
                "☼ ▼   ☼" +
                "☼     ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼");
    }

    // съедая пилюлю полёта, змейка перелетает камни
    @Test
    public void flyingOverStones() {
        givenFl("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►% ☼" +
                "☼   ● ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        game.tick();
        assertEquals("Змейка не съела пилюлю полёта!", 10, hero.getFlyingCount());
        hero.down();
        game.tick();
        assertEquals("Змейка съела камень в полёте!", 0, hero.getStonesCount());
        assertTrue("Змейка умерла от камня в полёте!", hero.isAlive());
        game.tick();
        game.tick();
        // камень остался на месте
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ● ☼" +
                "☼   ↓ ☼" +
                "☼   ▼ ☼" +
                "☼☼☼☼☼☼☼");
    }
}
