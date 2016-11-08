package com.codenjoy.dojo.snake.battle.model;

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


import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.snake.battle.services.Events;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: Kors
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
        Hero hero = level.getHero().get(0);

        game = new SnakeBoard(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
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

    // карта с яблоками
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

//        String longSnakeField =
//                "☼☼☼☼☼☼☼" +
//                "☼ ↓   ☼" +
//                "☼ ╚═╗ ☼" +
//                "☼   ║ ☼" +
//                "☼  ╔╝ ☼" +
//                "☼  ▼  ☼" +
//                "☼☼☼☼☼☼☼";
//        testField(longSnakeField);

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
        String after =
                "☼☼☼☼☼☼☼" +
                "☼  →► ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ●  ☼" +
                "☼    ○☼" +
                "☼☼☼☼☼☼☼";
        assertE(after);
    }
}
