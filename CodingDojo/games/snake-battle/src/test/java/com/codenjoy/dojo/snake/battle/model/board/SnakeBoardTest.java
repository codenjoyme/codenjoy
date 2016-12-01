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
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
        String simpleField =
                "☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼";
        testField(simpleField);
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

    // карта с яблоками и камнями
    @Test
    public void testStartFieldWithApples() {
        String applesField =
                "☼☼☼☼☼☼☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼";
        testField(applesField);
    }

    private void testField(String field){
        givenFl(field);
        assertE(field);
    }


    // тест продолжения движения без дополнительных указаний
    @Test
    public void moveAfterStart() {
        String before =
                "☼☼☼☼☼☼☼" +
                "☼ →►  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼";
        givenFl(before);
        game.tick();
        String after1 =
                "☼☼☼☼☼☼☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼";
        assertE(after1);
        game.tick();
        String after2 =
                "☼☼☼☼☼☼☼" +
                "☼   →►☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼";
        assertE(after2);
    }


    // тест движения в заданную сторону
    @Test
    public void moveByCommand() {
        String before =
                        "☼☼☼☼☼☼☼" +
                        "☼→►   ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼  ●  ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        givenFl(before);
        hero.down();
        game.tick();
        String after1 =
                        "☼☼☼☼☼☼☼" +
                        "☼ ↓   ☼" +
                        "☼ ▼   ☼" +
                        "☼     ☼" +
                        "☼  ●  ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after1);
        hero.right();
        game.tick();
        String after2 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ →►  ☼" +
                        "☼     ☼" +
                        "☼  ●  ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after2);
        hero.up();
        game.tick();
        String after3 =
                        "☼☼☼☼☼☼☼" +
                        "☼  ▲  ☼" +
                        "☼  ↑  ☼" +
                        "☼     ☼" +
                        "☼  ●  ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after3);
        hero.left();
        game.tick();
        String after4 =
                        "☼☼☼☼☼☼☼" +
                        "☼ ◄←  ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼  ●  ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after4);
    }


    // тест роста
    // заодно, тест отображения тела змейки
    @Test
    public void growUpTest() {
        String before =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼→►○  ☼" +
                        "☼     ☼" +
                        "☼  ○  ☼" +
                        "☼  ○  ☼" +
                        "☼☼☼☼☼☼☼";
        givenFl(before);
        hero.right();
        game.tick();
        String after1 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼→═►  ☼" +
                        "☼     ☼" +
                        "☼  ○  ☼" +
                        "☼  ○  ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after1);
        hero.down();
        game.tick();
        String after2 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ →╗  ☼" +
                        "☼  ▼  ☼" +
                        "☼  ○  ☼" +
                        "☼  ○  ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after2);
        hero.down();
        game.tick();
        String after3 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ →╗  ☼" +
                        "☼  ║  ☼" +
                        "☼  ▼  ☼" +
                        "☼  ○  ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after3);
        hero.left();
        game.tick();
        String after4 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼  ↓  ☼" +
                        "☼  ║  ☼" +
                        "☼ ◄╝  ☼" +
                        "☼  ○  ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after4);
        hero.down();
        game.tick();
        String after5 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼  ↓  ☼" +
                        "☼ ╔╝  ☼" +
                        "☼ ▼○  ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after5);
        hero.right();
        game.tick();
        String after6 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼  ↓  ☼" +
                        "☼ ╔╝  ☼" +
                        "☼ ╚►  ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after6);
        game.tick();
        String after7 =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼ ╔←  ☼" +
                        "☼ ╚═► ☼" +
                        "☼☼☼☼☼☼☼";
        assertE(after7);
    }

    // тест смерти маленькой змейки об камень
    @Test
    public void dieByStone() {
        String before =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼→►●  ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        givenFl(before);
        game.tick();
        String afterEatStone =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼ →☻  ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(afterEatStone);
        game.tick();
        String afterDead =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(afterDead);
    }

    // тест смерти об стену
    @Test
    public void dieByWall() {
        String before =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼ →►○ ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        givenFl(before);
        game.tick(); // удлинняем змею (иначе будет не ясно исчезла змея или просто вся вошла в стену)
        game.tick();
        game.tick();
        String afterCollision =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼   →═☻" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(afterCollision);
        game.tick();
        String afterDead =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(afterDead);
    }

    // змея не может разворачиваться в противоположную сторону
    @Test
    public void deniedMovingBack() {
        String before =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼ →►  ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        givenFl(before);
        hero.left();
        game.tick();
        String stillRight =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼  →► ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(stillRight);
        hero.up();
        game.tick();
        hero.down();
        game.tick();
        String stillUp =
                        "☼☼☼☼☼☼☼" +
                        "☼   ▲ ☼" +
                        "☼   ↑ ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(stillUp);
        hero.left();
        game.tick();
        hero.right();
        game.tick();
        String stillLeft =
                        "☼☼☼☼☼☼☼" +
                        "☼ ◄←  ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(stillLeft);
        hero.down();
        game.tick();
        hero.up();
        game.tick();
        String stillDown =
                        "☼☼☼☼☼☼☼" +
                        "☼     ☼" +
                        "☼ ↓   ☼" +
                        "☼ ▼   ☼" +
                        "☼     ☼" +
                        "☼    ○☼" +
                        "☼☼☼☼☼☼☼";
        assertE(stillDown);
    }
}
